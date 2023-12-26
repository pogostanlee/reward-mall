package com.rewardmall.config;


import com.rewardmall.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Date;

//配置拦截器
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("/user/login");
//        strings.add("/admin/exportCustomer");
        //登录接口和注册接口不拦截
        registry.addInterceptor(loginInterceptor).excludePathPatterns(strings);

    }
    //配置日期格式化
    @Bean
    Converter<String, Date> dateConvert() {
        return new com.rewardmall.convert.DateConvert();
    }


//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        FormHttpMessageConverter formConverter = new FormHttpMessageConverter();
//        converters.add(formConverter);
//    }

}
