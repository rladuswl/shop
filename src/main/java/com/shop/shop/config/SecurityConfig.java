package com.shop.shop.config;

import com.shop.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    // configure(HttpSecurity) 메소드를 통해 로그인 및 로그아웃 URL 지정
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // HttpServletRequest에 대해서 security 처리를 수행
        // anyRequest() - 위에 존재하는 url patterns 들을 제외한 나머지 요청들
        http.authorizeRequests()
                // 해당 url patterns은 모두에게 허용
                .mvcMatchers("/", "/member/**", "/item/**", "/images/**").permitAll()
                // 해당 url patterns(/admin/**)은 ADMIN에게만 허용
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();

        http.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        http.formLogin()
                .loginPage("/member/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/member/login/fail")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                .logoutSuccessUrl("/");

    }


    // static 디렉터리의 하위 파일은 인증을 무시하도록 설정
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

    // AuthenticationManagerBuilder 를 통해 AuthenticationManager 를 생성하여 인증 처리 수행
    // memberService 객체를 이용하여 (UserDetailsService 인터페이스를 구현하고, loadUserByUsername 메소드를 오버라이딩 하고 있음)
    // User 객체를 얻어낸 뒤, 지정된 비밀번호 암호화 방식으로
    // 비밀번호가 일치하는지 검증
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
