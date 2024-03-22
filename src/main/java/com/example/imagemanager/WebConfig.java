package com.example.imagemanager;

import com.example.imagemanager.inteceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Bean
    public LoginInterceptor getLoginInterceptor() {
        return new LoginInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLoginInterceptor())
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/admin/login"); // 排除登录接口，避免死循环拦截
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("*")
                .allowedHeaders("*");
    }

//    @Bean
//    public SecurityFilterChain web(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(authorize -> authorize
////                        .dispatcherTypeMatchers(FORWARD, ERROR).permitAll()
////                        .requestMatchers("/static/**", "/signup", "/about").permitAll()
////                        .requestMatchers("/admin/**").hasRole("ADMIN")
////                        .requestMatchers("/db/**").access(allOf(hasAuthority("db"), hasRole("ADMIN")))
////                        .anyRequest().denyAll()
//                        .anyRequest().authenticated()
//        );
//        return http.build();
//    }
}