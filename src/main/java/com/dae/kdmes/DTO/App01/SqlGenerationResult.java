package com.dae.kdmes.DTO.App01;

import lombok.Getter;
import lombok.Setter;
///26.01.20 14시
@Getter
@Setter
public class SqlGenerationResult {
    public String sql;
    public String answer;
    private boolean sqlMode;
    private String content;

    public SqlGenerationResult(boolean sqlMode, String content) {
        this.sqlMode = sqlMode;
        this.content = content;
    }

    public boolean isSqlMode() {
        return sqlMode;
    }

    public String getContent() {
        return content;
    }
}

