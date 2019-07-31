package com.ck.miaosha.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {

    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String src){
        if(StringUtils.isEmpty(src)){
            return false;
        }
        Matcher matcher = mobile_pattern.matcher(src);
        return matcher.matches();
    }

    /**
    public static void main(String[] args) {
        String str1 = "12334";
        String str2 = "15922056789";
        System.out.println(isMobile(str1));
        System.out.println(isMobile(str2));
    }*/


}
