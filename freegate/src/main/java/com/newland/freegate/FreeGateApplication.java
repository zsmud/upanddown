package com.newland.freegate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * Created by wot_zhengshenming on 2021/5/26.
 */
@SpringBootApplication
public class FreeGateApplication {
    public static void main(String[] args){
        SpringApplication.run(FreeGateApplication.class, args);
    }

    /**
     * 注册上传文件解析器，设置最大内存块和上传文件大小以及文件编码，需要在application.properties中排除自动配置
     */
    @Bean
    public MultipartResolver multipartResolver(){
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("utf-8");
        resolver.setResolveLazily(true);
        resolver.setMaxInMemorySize(40960);
        resolver.setMaxUploadSize(50*1024*1024);
        return resolver;
    }

    /**
     * 注册servlet组件，并开启http展示文件列表服务，以便下载
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        ServletRegistrationBean registration = new ServletRegistrationBean();
        registration.setServlet(new ExDefaultServlet());
        registration.addUrlMappings("/download/*");
        registration.addInitParameter("listings","true");
        return registration;
    }

    /**
     * 由于内嵌的tomcat不支持通过配置的方式来实现虚拟路径的配置，故实现自定义的tomcat，为了实现虚拟路径配置
     */
//    @Bean
//    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
//        TomcatServletWebServerFactory tomcatServletWebServerFactory = new ExTomcatServerFactory();
//        return tomcatServletWebServerFactory;
//    }
}
