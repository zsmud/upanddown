package com.newland.freegate.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by wot_zhengshenming on 2021/5/26.
 */
@Component
public class WebConfiguration implements WebMvcConfigurer {

    @Value("${download.path}")
    private String downloadPath;

    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/").setViewName("forward:/index");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        WebMvcConfigurer.super.addViewControllers(registry);
    }

//    public void addResourceHandlers(ResourceHandlerRegistry registry){
//        registry.addResourceHandler("/download/**")
//                .addResourceLocations("file:/"+ downloadPath + File.separator)
////                .resourceChain(false)
////                .addResolver(null)
//        ;
//    }
}
