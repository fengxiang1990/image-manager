package com.fxa.image.client;

import com.fxa.image.client.inteceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;

import java.util.List;


@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(new ProtobufHttpMessageConverter());
//        converters.add(new MappingJackson2HttpMessageConverter());
//    }

    @Bean
    public LoginInterceptor getLoginInterceptor() {
        return new LoginInterceptor();
    }


    private static final String[] sNeedInterceptorUrls = new String[]{
            "/content_publish",
            "/like",
            "/unlike",
            "/comment_publish",
            "/comment_like",
            "/comment_unlike",
            "/follow",
            "/unfollow",
            "/content_collect",
            "/content_un_collect"
    };
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLoginInterceptor())
                //拦截评论点赞发布收藏关注私信
                .addPathPatterns(sNeedInterceptorUrls) // 拦截所有请求
                .excludePathPatterns("/login"); // 排除登录接口，避免死循环拦截
    }

    //客户端vue项目的端口是8082
    //后台VUE是8080
    //后台服务是8081
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8082")
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}