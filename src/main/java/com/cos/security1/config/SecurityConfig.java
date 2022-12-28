package com.cos.security1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity  //스프링 시큐리티 필터가 스프링 필터체인에 등록됨.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()        // user/** 주소로 들어오면 인증이 필요해!
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                // manager/** , admin/** 로 들어오면 인증뿐 아니라 다음과 같은 access 권한이 있는 사람만 들어갈 수 있게
                .anyRequest().permitAll()
                // 이외의 모든 접근은 허용
                .and()
                .formLogin()
                .loginPage("/login");   // 권한이 없는 페이지 접근시 login 페이지로 자동으로 연결되게
    }

}
