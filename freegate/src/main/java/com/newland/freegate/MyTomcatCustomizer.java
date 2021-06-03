package com.newland.freegate;

import org.apache.catalina.WebResourceRoot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;

/**
 * Created by wot_zhengshenming on 2021/6/3.
 *
 * 通过实现WebServerFactoryCustomizer接口，并注册为bean组件
 * 原理是ServletWebServerFactoryAutoConfiguration中会引入webServerFactoryCustomizerBeanPostProcessor
 * 而webServerFactoryCustomizerBeanPostProcessor会注册实现了WebServerFactoryCustomizer接口的类
 *
 * 而实现了ConfigurableWebServerFactory接口的工厂类会回调customize方法
 *
 */
@Component
public class MyTomcatCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Value("${download.path}")
    private String downloadPath;

    /**
     * 由于内嵌的tomcat不支持通过配置的方式来实现虚拟路径的配置，故扩展tomcat，为了实现虚拟路径配置
     */
    @Override
    public void customize(TomcatServletWebServerFactory factory) {

            if(StringUtils.hasText(downloadPath)) {
                File file = new File(this.downloadPath);
                if(file.exists()){
                    factory.addContextCustomizers(context->{
                        try {
                        context.getResources().createWebResourceSet(
                            WebResourceRoot.ResourceSetType.POST, "/download", file.toURI().toURL(), "/");
                        }catch(Exception e){
                            e.printStackTrace();

                        }
                        });

                }
            }else{
                System.out.println("===> no set download path <===");
            }

    }
}
