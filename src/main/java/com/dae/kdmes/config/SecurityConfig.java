package com.dae.kdmes.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity   //활성화  : 스프링시큐리티 필터가 스프링 필터체인에 등록이 됩니다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)      //secured 어노테이션 활성화 prePost,preAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //해당 메서드의 리턴되는 오브제그를 ioc로 등록해준다.
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.headers()
                        .frameOptions().sameOrigin();
        http.authorizeRequests()
                .antMatchers("/members/user/**").authenticated()        //인증만되면 들어갈수있는 주소 .
                .antMatchers("/members/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/members/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                    .loginPage("/members/loginForm")
                    .loginProcessingUrl("/login") //login 주소가 호출이되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
                    .defaultSuccessUrl("/members/user")
                    .failureUrl("/members/login/error")
                .and()
                .logout()
                    .logoutUrl("/members/logout")
    //                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                    .logoutSuccessUrl("/members/logout");


        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        ;


    }

}
