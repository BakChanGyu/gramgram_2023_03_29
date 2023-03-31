package com.ll.gramgram.base.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/member/login")
                                /** 관리자가 로그인한 경우
                                .successHandler(new SimpleUrlAuthenticationSuccessHandler() {
                                    @Override
                                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                        if (isAdmin(따로 구현)) {
                                            DefaultRedirectStrategy defaultRedirectStrategy = new DefaultRedirectStrategy();
                                            defaultRedirectStrategy.sendRedirect(request, response, "/adm");
                                        } else {
                                            super.onAuthenticationSuccess(request, response, authentication);
                                        }
                                    }
                                })
                                 */
                )
                .logout(
                        logout -> logout
                                .logoutUrl("/member/logout")
                );
                return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
