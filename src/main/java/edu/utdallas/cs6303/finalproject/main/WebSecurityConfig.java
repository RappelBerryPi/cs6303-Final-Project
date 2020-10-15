package edu.utdallas.cs6303.finalproject.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import edu.utdallas.cs6303.finalproject.services.oauth.OidcUserServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    OidcUserServiceImpl userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/css/**", "/fonts/**", "/images/**", "/js/**", "/", "/home", "/Files/**", "/feedback").permitAll()
                .antMatchers("/cgi-bin/test-cgi").denyAll()
                .anyRequest().permitAll()
            .and()
            .formLogin()
                .loginPage("/login").permitAll()
            .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                    .oidcUserService(userService)
                .and()
                .defaultSuccessUrl("/login/loginSuccess")
            .and()
                .logout().permitAll();

        // https://localhost:8443/oauth2/authorization/google
    }
}