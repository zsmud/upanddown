package com.newland.freegate;

import java.io.File;
import java.net.URL;

/**
 * Created by wot_zhengshenming on 2021/5/31.
 */
public class Test {
    public static void main(String[] args) throws Exception{
        File file = new File("d:\\cmd");
        System.out.println(file.toURI().getAuthority());
        System.out.println(file.toURI());
        URL url = new URL(file.toURI().toString());
        System.out.println(file.toURI().toURL().getAuthority());
    }
}
