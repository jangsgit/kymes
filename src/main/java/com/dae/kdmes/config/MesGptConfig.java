package com.dae.kdmes.config;

import com.dae.kdmes.controller.analysis.util.TableSpecLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
///26.01.20 14시
@Configuration
public class MesGptConfig {

    @Bean
    public Map<String, List<Map<String, String>>> gptMesTableSpec(TableSpecLoader loader) {
        return loader.loadFromExcel("templates/_tablelist/DataBaseExcel.xlsx");
    }
}

