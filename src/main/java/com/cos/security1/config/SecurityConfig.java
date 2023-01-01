package com.cos.security1.config;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    /* 카카오 로그인 api
        1. 코드받기 (인증 성공, 정상적으로 api 에서 로그인이 됐다)
        2. 액세스토큰 받기 (권한이 생김)
        3. 사용자 프로필 가져옴
        4-1. 그 정보를 토대로 회원가입을 자동으로 진행. (추가 정보 필요 없을 시)
        4-2. 추가적인 구성정보를 요구하는 페이지로 (추가 정보 필요할 때)
     */

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

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
                .loginPage("/loginForm")   // 구글 로그인이 완료된 뒤의 후처리가 필요  Tip. 구글 로그인 api 는? (oauth-client 만의 특징?) 코드 X, 액세스 토큰 + 사용자프로필정보 O
                .userInfoEndpoint()
                .userService(principalOauth2UserService);   // 후처리
    }

}
