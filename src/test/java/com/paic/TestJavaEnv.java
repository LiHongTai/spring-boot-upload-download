package com.paic;

import java.net.InetAddress;
import java.util.Map;

public class TestJavaEnv {

    public static void main(String[] args) throws Exception{
        Map<String, String> map = System.getenv();
        String userName = map.get("USERNAME");// 获取用户名
        System.out.println(userName);

        System.out.println(InetAddress.getLocalHost().getHostAddress());
    }

}
