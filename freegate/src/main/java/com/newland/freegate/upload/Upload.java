package com.newland.freegate.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;

/**
 * Created by wot_zhengshenming on 2021/5/26.
 */
@Controller
public class Upload extends AbstractController {
    @Value("${upload.path}")
    private String path;

    @RequestMapping(value="/upload",method = RequestMethod.POST)
    public String upload(@RequestParam("file") CommonsMultipartFile file){
        this.getRequest().setAttribute("type","自动化测试案例上传成功");
        File destFile = new File(path + File.separator+file.getOriginalFilename());

        try{
            if(file!=null && !file.isEmpty()){
                file.transferTo(destFile);
            }else{
                this.getRequest().setAttribute("type","上传文件为空");
                return "error";
            }
        }catch(Exception e){
            this.getRequest().setAttribute("type","上传失败"+e.getMessage());
            e.printStackTrace();
            return "error";
        }

        return "success";
    }

    @RequestMapping("/index")
    public String gotoIndex(){
        return "index";
    }
}
