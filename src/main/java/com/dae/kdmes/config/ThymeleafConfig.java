package com.dae.kdmes.config;


import nz.net.ultraq.thymeleaf.LayoutDialect;   // ⬅ 요 패키지 주의! (layoutdialect 아님)
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThymeleafConfig {

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }
}
