package com.imooc.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;


/**
 * http在网络上明文传递，所以对密码进行md5加密
 */
public class MD5Util {

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    public static String inputPassFormPass(String inputPass){
        String str = "" + salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    /**
     * 在得到一次加密值后，为了安全，在进行一次加密存到数据库中
     * 如果数据库被盗，反查md5得到的是inputFormPass(inputPass)+一个salt
     * 得不到真正的密码
     * @param inputPass
     * @param salt
     * @return
     */
    public static String formPassToDBPass(String inputPass, String salt){
        String str = "" + salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass, String salt){
        String formPass = inputPassFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, salt);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPassToDBPass("123456","1a2b3c4d"));
        System.out.println(formPassToDBPass("d3b1294a61a07da9b49b6e22b2cbd7f9","1a2b3c"));
    }



}
