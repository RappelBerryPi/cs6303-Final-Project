package edu.utdallas.cs6303.finalproject.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import edu.utdallas.cs6303.finalproject.interceptors.DeviceImageInterceptor;
import edu.utdallas.cs6303.finalproject.interceptors.UserDetailsInterceptor;

@Configuration
public class MainWebMVCConfig implements WebMvcConfigurer {

    @Autowired
    DeviceImageInterceptor deviceImageInterceptor;

    @Autowired
    UserDetailsInterceptor userDetailsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(deviceImageInterceptor).excludePathPatterns("/Files/");
        registry.addInterceptor(userDetailsInterceptor).excludePathPatterns("/Files/");
    }

}