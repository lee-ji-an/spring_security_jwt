package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity  //스프링 시큐리티 필터가 스프링 필터체인에 등록됨.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)  // secured 어노테이션 활성화, preAuthorize & postAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean  //리턴되는 오브젝트를 IoC로 등
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()        // user/** 주소로 들어오면 인증이 필요해! (인증만 되면 들어갈 수 있음)
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                // manager/** , admin/** 로 들어오면 인증뿐 아니라 다음과 같은 access 권한이 있는 사람만 들어갈 수 있게
                .anyRequest().permitAll()
                // 이외의 모든 접근은 허용
                .and()
                .formLogin()
                .loginPage("/loginForm")   // 권한이 없는 페이지 접근시 login 페이지로 자동으로 연결되게
                .loginProcessingUrl("/login")  // login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행해줌. (컨트롤러에 /login 을 안 만들어도 됨.)
                .defaultSuccessUrl("/")       // 로그인 완료된 후, '/' 를 반환함 (특정 페이지를 들어가려다 login 페이지로 들어왔을 경우, 해당 페이지로 갈 수 있게 해줌)
                .and()
                .oauth2Login()
                .loginPage("/loginForm");   // 구글 로그인이 완료된 뒤의 후처리가 필요
    }

}
