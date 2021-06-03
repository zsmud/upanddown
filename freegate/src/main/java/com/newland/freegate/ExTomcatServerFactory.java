package com.newland.freegate;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.util.StringUtils;

import java.io.File;

/**
 * Created by wot_zhengshenming on 2021/5/31.
 */
@Deprecated
public class ExTomcatServerFactory extends TomcatServletWebServerFactory {

    @Value("${download.path}")
    private String downloadPath;

    /**
     * 映射请求路径为虚拟文件夹路径,映射/download请求路径到本地文件系统文件夹中去。
     * @param context
     */
    protected void postProcessContext(Context context) {
        try {
            if(StringUtils.hasText(downloadPath)) {
                File file = new File(this.downloadPath);
                if(file.exists()){
                    context.getResources().createWebResourceSet(
                            WebResourceRoot.ResourceSetType.POST, "/download", file.toURI().toURL(), "/");
                }

            }else{
                System.out.println("===> no set download path <===");
            }

        }catch(Exception e){
            e.printStackTrace();

        }
    }
}
