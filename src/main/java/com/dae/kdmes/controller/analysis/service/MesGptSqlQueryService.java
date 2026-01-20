package com.dae.kdmes.controller.analysis.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import com.dae.kdmes.controller.analysis.util.TablePromptUtil;
import com.dae.kdmes.DTO.App01.SqlGenerationResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
///26.01.20 14시
@Service
@RequiredArgsConstructor
public class MesGptSqlQueryService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JdbcTemplate jdbcTemplate;

    // 기억된 테이블 명세 (GPT가 이해할 수 있도록 system prompt로 활용)
    private final Map<String, List<Map<String, String>>> gptMesTableSpec;

    protected Log log =  LogFactory.getLog(this.getClass());


    public static class SqlGenerationResult02 {
        public String sql;
        public String answer;

    }

    /**
     * 1. 사용자 질문을 기반으로 GPT가 SQL 쿼리 또는 자연어 응답 생성
     */
    public SqlGenerationResult generateSqlFromPrompt(String prompt) throws InterruptedException {
        try {
            String schemaPrompt = TablePromptUtil.buildSystemPromptFromSpec(gptMesTableSpec);

            String fullPrompt = createPrompt(prompt);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> body = Map.of(
                    "model", "gpt-4.1-mini-2025-04-14",
                    "messages", List.of(
                            Map.of("role", "system", "content",  schemaPrompt),
                            Map.of("role", "user", "content", fullPrompt)
                    )
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            RestTemplate restTemplate = new RestTemplate();
            Map<?, ?> response = restTemplate.postForObject("https://api.openai.com/v1/chat/completions", entity, Map.class);

            if (response == null || !response.containsKey("choices")) {
                log.warn("GPT 응답 없음");
                return null;
            }

            List<?> choices = (List<?>) response.get("choices");
            Map<?, ?> message = (Map<?, ?>) ((Map<?, ?>) choices.get(0)).get("message");
            String content = (String) message.get("content");
            return extractFromGptResponse(content);
        }catch (HttpClientErrorException.TooManyRequests e) {
            log.warn("Rate limit 초과 - 일정 시간 대기 후 재시도");
            Thread.sleep(5000); // 5초 대기
            return generateSqlFromPrompt(prompt); // 재시도
        }

    }

    // GPT 응답에서 sql 또는 answer 추출
    private SqlGenerationResult extractFromGptResponse(String gptResponse) {
        try {
            if (gptResponse == null) {
                return new SqlGenerationResult(false, "GPT 응답이 비어 있습니다.");
            }

            String cleaned = gptResponse.trim();

            // 1) 코드펜스 제거 (```json ... ``` 형태 대비)
            cleaned = cleaned.replaceAll("```(?:json|sql)?\\s*", "");
            cleaned = cleaned.replaceAll("\\s*```", "");
            cleaned = cleaned.trim();

            // 2) 앞에 텍스트가 붙어도 { ... }만 잘라낸다
            String candidate = cleaned;
            int s = cleaned.indexOf('{');
            int e = cleaned.lastIndexOf('}');
            if (s >= 0 && e > s) {
                candidate = cleaned.substring(s, e + 1).trim();
            }

            // 3) JSON 파싱 먼저 시도
            try {
                Map<String, Object> map = objectMapper.readValue(candidate, new TypeReference<>() {});
                Object sqlObj = map.get("sql");
                if (sqlObj != null) {
                    return new SqlGenerationResult(true, sqlObj.toString().trim());
                }
                Object ansObj = map.get("answer");
                if (ansObj != null) {
                    return new SqlGenerationResult(false, ansObj.toString().trim());
                }
            } catch (Exception ignore) {
                // JSON이 엄밀하지 않을 수 있으니 regex로 sql만 뽑는다
            }

            // 4) JSON이 깨진 경우에도 "sql": "...." 만 강제로 추출 (줄바꿈 포함 대응)
            java.util.regex.Pattern pSql =
                    java.util.regex.Pattern.compile("(?s)\"sql\"\\s*:\\s*\"(.*?)\"\\s*(,|})");
            java.util.regex.Matcher mSql = pSql.matcher(candidate);
            if (mSql.find()) {
                String sql = mSql.group(1)
                        .replace("\\n", "\n")
                        .replace("\\t", "\t")
                        .replace("\\\"", "\"")
                        .trim();
                return new SqlGenerationResult(true, sql);
            }

            // 5) 최후 fallback: select로 시작할 때만 SQL로 인정 (contains 절대 금지)
            String lower = cleaned.trim().toLowerCase();
            if (lower.startsWith("select")) {
                return new SqlGenerationResult(true, cleaned.trim());
            }

            return new SqlGenerationResult(false, cleaned.trim());

        } catch (Exception e) {
            log.error("GPT 응답 파싱 실패", e);
            return new SqlGenerationResult(false, "GPT 응답을 해석할 수 없습니다.");
        }
    }




    /**
     * 2. 생성된 SQL을 실행하고 결과 반환
     */
    public List<Map<String, Object>> executeGeneratedSql(String sql) {
        try {
            String s = (sql == null ? "" : sql.trim());
            if (s.startsWith("{") || !s.toLowerCase().startsWith("select")) {
                throw new IllegalArgumentException("실행 불가: SQL이 SELECT 문이 아니거나 JSON이 섞여 있습니다: " + s);
            }
            log.info("[OpenAI sql] =====> " + s);
            return jdbcTemplate.queryForList(s);
        } catch (Exception e) {
            log.error("SQL 실행 오류", e);
            return null;
        }
    }

    /**
     * 3. 사용자 질문을 기반으로 전체 흐름 처리
     */
    public String run(String prompt) throws InterruptedException {
        SqlGenerationResult result = generateSqlFromPrompt(prompt);

        if (result == null) {
            throw new RuntimeException("GPT 처리 실패: 응답이 null입니다.");
        } else if (result.isSqlMode()) {
            List<Map<String, Object>> rows = executeGeneratedSql(result.getContent());
            return formatRowsAsReadableText(rows);  // 👉 여기에 결과 포맷
        } else if (result.getContent() != null) {
            return result.getContent();  // 자연어 응답
        } else {
            throw new RuntimeException("GPT가 유효한 SQL이나 답변을 반환하지 않았습니다.");
        }
    }
    private String formatRowsAsReadableText(List<Map<String, Object>> rows) {
        if (rows == null || rows.isEmpty()) {
            return "하지만 결과 데이터가 없습니다.";
        }

        Map<String, Object> row = rows.get(0);
        StringBuilder sb = new StringBuilder("분석 결과:\n");

        for (Map.Entry<String, Object> entry : row.entrySet()) {
            sb.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        return sb.toString();
    }

    /**

     * 5. GPT에게 보낼 prompt 생성

     */
    public String createPrompt(String param) {
        /**String schemaPrompt = TablePromptUtil.buildSystemPromptFromSpec(gptMesTableSpec); */

        return    """
                 [ROLE]
                   너는 '경덕산업' MES 데이터 분석용 SQL 생성기이자 결과 요약 도우미다.
                   사용자 질문을 해석해 MSSQL(SQL Server)용 SELECT 쿼리를 만들고, 실행 결과를 표로 요약한다.
                   단, "쿼리만" 출력하는 것이 목적이 아니라, 서버가 쿼리를 실행해 결과를 반환하면 이를 이해하기 쉬운 표/요약으로 재구성하는 것이 목적이다.
                                     
                  [COMPANY CONTEXT - 경덕산업]
                  - 회사명: 경덕산업
                  - 사업: 금형설계/금형개발, 사출성형(이중사출 포함), 부품조립(원라인 공정)
                  - 주거래처: KCC (자동차 유리 결합용 부품 생산: Grip Holder, Location Pin(Stopper), Base Bracket(Integration), Spacer 등)
                  - MES 질문은 사출 → 검사 → 조립 → 출하 흐름을 기준으로 해석한다.
                  - 분석 시 금형/설비/LOT/공정/불량/검사를 핵심 축으로 사용한다.
                   
                   [DB RULE - MSSQL]
                   - DBMS: Microsoft SQL Server (T-SQL)
                   - LIMIT 금지. 상위 N은 TOP 사용.
                   - 날짜: GETDATE(), DATEADD 사용.
                   - 날짜 비교는 컬럼 타입에 맞게 처리 (문자 yyyymmdd면 CONVERT(date, col, 112) 패턴 사용 가능).
                   - NULL 처리: ISNULL
                   - 0 나눗셈 방지: NULLIF(분모,0)
                   - 페이징 필요 시: ORDER BY + OFFSET/FETCH 사용(단, 질문에 필요할 때만).
                   
                  [SECURITY / SAFETY]
                  - 절대 INSERT/UPDATE/DELETE/TRUNCATE/DROP/ALTER 등 DML/DDL을 생성하지 않는다.
                  - 항상 읽기 전용 SELECT만 생성한다.
                  - 절대 비밀정보(API Key, DB password 등)나 시스템 경로를 출력하지 않는다.
                  - 사용자가 요청하더라도 개인/비밀정보는 마스킹한다.
                                              
                  
                  [QUERY GENERATION RULES]
                  1) 질문을 MES 관점(사출/검사/조립/출하/금형/설비/LOT)으로 재해석한다.
                  2) 필요한 테이블/컬럼을 추정하되, 확실치 않으면 "가정"을 명시한다.
                  3) 결과는 사용자가 바로 이해할 수 있도록 컬럼 별칭을 한글로 지정한다.
                  4) 기본 정렬은 최신순/중요도순 (예: 최근 생산계획=등록일자/계획번호 DESC).
                  5) 기본 조회 범위가 불명확하면 최근 1년 또는 최근 3개월 중 질문 맥락에 맞게 선택한다(불량/품질은 보통 최근 1년).
                  6) 데이터가 많을 수 있는 경우 TOP 10/50 등 제한을 둔다(질문에 따라 조정).
                  
                  [OUTPUT FORMAT - 반드시 JSON으로만 출력]
                  너의 응답은 반드시 아래 JSON 중 하나의 형태여야 한다. 다른 텍스트(“🧠 GPT 분석 결과” 같은 접두어) 절대 금지.
                         
                                
                (1) SQL 생성 성공
                {
                  "type": "sql",
                  "sql": "SELECT ...",
                  "expects_result": true,
                  "result_render": {
                    "format": "table",
                    "max_rows": 50,
                    "summary_rules": [
                      "표 상단에 기간/조건 요약 1줄",
                      "핵심 지표 1~3줄 요약"
                    ]
                  }
                }
                                
                (2) SQL 대신 질문이 모호한 경우(추가정보 요청)
                {
                  "type": "clarify",
                  "questions": [
                    "기간은 언제부터 언제까지로 볼까요?",
                    "대상 공정(사출/조립/검사) 중 어디를 기준으로 볼까요?"
                  ]
                }
                                
                (3) 일반 설명만 필요한 경우
                {
                  "type": "answer",
                  "answer": "..."
                }
                                
                [IMPORTANT - 실행/결과 표시 방식]
                - 너는 DB를 직접 실행하지 않는다.
                - 서버가 sql을 실행한 뒤 rows/columns를 다시 너에게 전달하면,
                  너는 반드시 "table" 형식으로 정리하고 지표/해석을 제공한다.
                - 따라서 1차 응답은 SQL JSON, 2차 응답은 결과 요약 JSON을 사용한다.
                                
                [SECOND RESPONSE FORMAT - 실행 결과를 받았을 때]
                서버가 다음 형태로 결과를 주면:
                {
                  "type": "db_result",
                  "sql": "...",
                  "columns": ["col1","col2",...],
                  "rows": [[...],[...],...],
                  "meta": {"rowCount": n}
                }
                너는 아래 형태로만 응답한다:
                {
                  "type": "report",
                  "table": {
                    "columns": [...],
                    "rows": [...],
                    "max_rows_shown": 50
                  },
                  "insights": [
                    "핵심 발견 1",
                    "핵심 발견 2"
                  ],
                  "next_actions": [
                    "추가로 보면 좋은 분석/조회 1",
                    "추가로 보면 좋은 분석/조회 2"
                  ]
                }         
                         
                         [COLUMN MAPPING STRICT RULE]
                         - 업무 용어(예: "불량수량")를 물리 컬럼으로 변환할 때는 반드시 "대상 테이블"을 먼저 확정한다.
                         - 대상 테이블이 TB_FPLAN_WBAD이면 "불량수량"은 항상 wbqty를 사용한다. (wbdqt 사용 금지)
                         - 다른 테이블에서는 해당 테이블 명세에 정의된 컬럼만 사용한다. 동일 의미라도 타 테이블의 컬럼명을 절대 가져오지 않는다.
                         - SQL 생성 후 자체 검증을 수행한다:
                           1) FROM/JOIN에 실제로 포함된 테이블(alias)만 참조했는지
                           2) 각 alias에 대해 명세에 존재하는 컬럼만 사용했는지
                           3) 유사 컬럼명(wbqty/wbdqt 등)을 추측 치환하지 않았는지
                         - 검증 실패 또는 명세 불확실 시 SQL을 생성하지 말고 type=clarify로 필요한 정보를 질문한다.
                         
                         
                         
                         [MSSQL(T-SQL) SQL 생성 규칙]
                         1) MSSQL에서는 테이블명/컬럼명 식별자는 기본적으로 대소문자 구분을 하지 않지만,
                            예약어/공백/특수문자/혼동 가능성이 있으면 반드시 대괄호([])로 감싼다.
                            - 예: SELECT [misdate] AS [전표일자] FROM [tb_salesment]
                            - 가능하면 모든 컬럼/테이블에 []를 적용하는 것을 권장한다.
                            - PostgreSQL의 쌍따옴표(") 규칙은 사용하지 않는다.
                         
                         2) 0으로 나누는 오류 방지를 위해 분모에는 항상 NULLIF(..., 0)을 사용한다.
                            - 예: SUM([A]) / NULLIF(SUM([B]), 0)
                         
                         3) 정수/정수 나눗셈은 MSSQL에서 정수 결과가 될 수 있으므로,
                            비율/평균/퍼센트 계산 시 반드시 소수 계산이 되게 CAST/CONVERT 또는 100.0을 사용한다.
                            - 예: SUM([DefectQty]) * 100.0 / NULLIF(SUM([GoodQty] + [DefectQty]), 0)
                            - 또는: CAST(SUM([x]) AS decimal(18,4)) / NULLIF(SUM([y]), 0)
                         
                         4) ROUND는 MSSQL에서 ROUND(숫자, 소수자리수)를 바로 지원한다.
                            - 예: ROUND( SUM([A]) * 100.0 / NULLIF(SUM([B]), 0), 2 )
                            (PostgreSQL처럼 ::NUMERIC 캐스팅 규칙은 적용하지 않는다.)
                         
                         5) 날짜/월별 필터링은 MSSQL 방식으로 처리한다.
                            - DATE/DATETIME 컬럼:\s
                              * 월 기준: FORMAT([ProductionDate], 'yyyy-MM') = '2025-06'   (간단하지만 느릴 수 있음)
                                또는 (권장) 범위조건: [ProductionDate] >= '2025-06-01' AND [ProductionDate] < '2025-07-01'
                            - 문자열(YYYYMMDD) 컬럼인 경우:
                              * CONVERT(date, [misdate], 112) 로 date 변환 후 사용 (112=YYYYMMDD)
                                예: FORMAT(CONVERT(date, [misdate], 112), 'yyyy-MM') = '2025-06'
                                또는 범위조건:
                                CONVERT(date, [misdate], 112) >= '2025-06-01' AND CONVERT(date, [misdate], 112) < '2025-07-01'
                         
                         6) NULL 처리:
                            - MSSQL에서는 ISNULL(expr, 대체값) 또는 COALESCE 사용 가능
                            - 예: ISNULL([remarkaa], '')
                         
                         7) 페이징/상위 N개:
                            - 상위 N개는 TOP 사용
                              예: SELECT TOP 10 ... FROM ...
                            - ORDER BY가 필요한 경우 반드시 함께 명시
                         
                         8) 두 개 이상 테이블 조인 시:
                            - SELECT/WHERE/ORDER BY에서 사용하는 모든 컬럼에는 반드시 테이블 별칭을 붙인다.
                            - JOIN 조건은 반드시 서로 다른 테이블의 FK 관계 기반으로 작성한다(자기 자신 컬럼끼리 조인 금지).
                         
                         9) 쿼리 결과를 사용자에게 보여줄 때,
                            SELECT 절에는 명세서에 정의된 컬럼의 “설명/한글명”을 AS 별칭으로 반드시 제공한다.
                            예:
                            SELECT a.[misdate] AS [전표일자], a.[cltcd] AS [거래처코드]
                            FROM [tb_salesment] a
                         
                         [TB_CA510 com_cls 코드그룹 매핑 규칙(강제)]
                            - TB_CA510.com_cls는 업무 분류를 나타내는 코드그룹 값이다.
                            - com_cls에는 'BAD', '공정', '불량' 같은 문자열을 절대 사용하지 말고,
                              아래 매핑 코드값만 사용한다. 
                            
                        [매핑 사전]
                        - 공정코드 = '001'
                        - 사원코드 = '002'
                        - 가동중단사유 = '004'
                        - 불량구분 = '005'
                        - 금형구분 = '006'
                        - Door1 = '102'
                        - Door2 = '103'
                        - 유형 = '104'
                        - 색상 = '105'
                        - 유리두께 = '106'
                        - 금형보관위치 = '107'
                        - 예: 불량사유명은 CA.com_rem1, 조인조건은 CA.com_cls='005' AND CA.com_code=...
                        
                        [조인 규칙]
                        - 공정명 조회: TB_CA510.com_cls='001'
                        - 사원명 조회: TB_CA510.com_cls='002'
                        - 가동중단사유명 조회: TB_CA510.com_cls='004'
                        - 불량사유/불량구분명 조회: TB_CA510.com_cls='005'
                        - 금형구분명 조회: TB_CA510.com_cls='006'
                        - Door1/2/유형/색상/유리두께/보관위치 조회는 각각 지정 코드 사용

                        [TB_CA510(공통코드) 표시명 컬럼 사용 규칙 - 강제]
                        - TB_CA510은 공통코드 테이블이며, 코드명/표시명 컬럼의 의미가 다르다.
                        - com_cnam = 대분류명(그룹명) 이다. 상세 코드의 표시명(사유명/유형명 등)으로 사용 금지.
                        - com_rem1 = 상세분류명(상세 코드의 표시명) 이다. 실제 화면/리포트에 표시할 명칭은 반드시 com_rem1을 사용한다.
                        - 따라서 TB_CA510을 조인하여 “사유명/유형명/색상명/공정명/사원명/금형구분명” 등 ‘상세 코드명’을 출력할 때는
                          반드시 TB_CA510.com_rem1을 SELECT에 사용한다.
                        - 예시(불량사유명):
                          SELECT MAX(CA.[com_rem1]) AS [불량사유명]
                          FROM ... LEFT JOIN TB_CA510 CA ON CA.[com_cls]='005' AND CA.[com_code]=bad.[wcode]
                        - com_cnam을 상세명으로 사용하는 SQL은 금지한다.
                         
                         
                         
                         [월별 비교 조건 추가 규칙]
                         - 사용자가 "전월 대비", "월별 비교", "매출 증감률" 같은 질의를 할 경우:
                           - 가능하면 WITH/JOIN을 남발하지 말고 CASE WHEN으로 한 쿼리에서 비교한다.
                           - 예시:
                             SELECT
                               SUM(CASE WHEN FORMAT([ShipDate], 'yyyy-MM') = '2025-07' THEN [TotalPrice] + [TotalVat] ELSE 0 END) AS [7월매출금액],
                               SUM(CASE WHEN FORMAT([ShipDate], 'yyyy-MM') = '2025-06' THEN [TotalPrice] + [TotalVat] ELSE 0 END) AS [6월매출금액],
                               ROUND(
                                 (
                                   SUM(CASE WHEN FORMAT([ShipDate], 'yyyy-MM') = '2025-07' THEN [TotalPrice] + [TotalVat] ELSE 0 END)
                                   - SUM(CASE WHEN FORMAT([ShipDate], 'yyyy-MM') = '2025-06' THEN [TotalPrice] + [TotalVat] ELSE 0 END)
                                 ) * 100.0
                                 / NULLIF(SUM(CASE WHEN FORMAT([ShipDate], 'yyyy-MM') = '2025-06' THEN [TotalPrice] + [TotalVat] ELSE 0 END), 0),
                                 2
                               ) AS [전월대비증감률]
                             FROM [shipment_head]
                             WHERE [ShipDate] >= '2025-06-01' AND [ShipDate] < '2025-08-01'
                         
                         ✅ 결과는 JSON 형식으로 다음 중 하나로 반환해야 합니다:
                         - SQL이 필요한 경우:
                           { "sql": "SELECT ..." }
                         - 자연어로 대답할 경우:
                           { "answer": "..." }
                         
                         사용자 질문은 다음과 같습니다:
                         
                """ + "\n" + param;
    }



    public static class GptNaturalResponseException extends RuntimeException {

        public GptNaturalResponseException(String message) {

            super(message);

        }

    }
}
