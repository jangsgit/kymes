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
         당신은 MES 시스템 분석 도우미입니다.
                 아래는 MES 시스템에서 사용하는 실제 테이블 및 컬럼 명세입니다.
                 ✅ 반드시 아래 테이블/컬럼만 사용해서 SQL을 작성하세요.
                 ❌ 존재하지 않는 테이블명이나 컬럼명을 사용하면 안 됩니다.
                 ✅ SELECT 문만 작성하세요. INSERT/UPDATE/DELETE는 작성하지 마세요.
                 
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
