package com.dae.kdmes.controller.analysis.util;


import java.util.List;
import java.util.Map;
///26.01.20 14시
public class TablePromptUtil {

    public static String buildSystemPromptFromSpec(Map<String, List<Map<String, String>>> tableSpecMap) {
        StringBuilder sb = new StringBuilder();

        sb.append("너는 MES 데이터를 분석하는 AI 전문가다. 아래는 테이블 구조이다:\n\n");

        for (String tableName : tableSpecMap.keySet()) {
            sb.append("테이블: ").append(tableName).append("\n");
            List<Map<String, String>> columns = tableSpecMap.get(tableName);
            for (Map<String, String> column : columns) {
                String colId = column.getOrDefault("id", column.getOrDefault("column", "unknown"));
                colId = "[" + colId + "]"; // MSSQL
                String type = column.getOrDefault("type", column.getOrDefault("데이터타입", "TEXT"));
                String comment = column.getOrDefault("comment", column.getOrDefault("설명", "없음"));

                sb.append("- ")
                        .append(colId)
                        .append(" (").append(type).append(")")
                        .append(": ").append(comment)
                        .append("\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}


