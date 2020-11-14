package edu.utdallas.cs6303.finalproject.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import edu.utdallas.cs6303.finalproject.services.oauth.OidcUserServiceImpl;
import edu.utdallas.cs6303.finalproject.services.user.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    OidcUserServiceImpl userService;

    @Autowired
    private CustomWebAuthenticationDetailsSource authenticationDetailsSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/login/qrCode").authenticated()
                .antMatchers("/css/**", "/fonts/**", "/images/**", "/js/**", "/").permitAll()
                .antMatchers("/home", "/Files/**", "/feedback", "/favicon.ico").permitAll()
                .antMatchers("/MomAndPop.png", "/google/**", "/login/**").permitAll()
                .antMatchers("/About", "/Contact", "/Store").permitAll()
                .antMatchers("/cgi-bin/test-cgi").denyAll()
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .authenticationDetailsSource(authenticationDetailsSource)
                .permitAll()
            .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                    .oidcUserService(userService)
                .and()
                .defaultSuccessUrl("/login/loginSuccess")
            .and()
                .logout().permitAll();

    }

    @Bean
	public DaoAuthenticationProvider authProvider(PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsService) {
        CustomAuthenticationProvider authenticationProvider = new CustomAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
	}
}