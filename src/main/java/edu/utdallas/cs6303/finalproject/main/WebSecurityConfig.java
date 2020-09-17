package edu.utdallas.cs6303.finalproject.main;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/css/**", "/fonts/**", "/images/**", "/js/**", "/", "/home", "/Files/**", "/feedback").permitAll().antMatchers("/cgi-bin/test-cgi").denyAll()
                // .anyRequest().authenticated()
                .anyRequest()
                .permitAll()
                .and().
            formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .oauth2Login()
            .loginPage("/login")
            .defaultSuccessUrl("/login/loginSuccess")
            .and().logout().permitAll();

        // https://localhost:8443/oauth2/authorization/google
    }

    @Bean
    public PasswordEncoder passwordEncoderj() {
        return new BCryptPasswordEncoder();
    }
}