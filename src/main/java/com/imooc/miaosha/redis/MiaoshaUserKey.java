package com.imooc.miaosha.redis;

public class MiaoshaUserKey extends BasePrefix{

    public static final int TOKEN_EXPIRE = 24*3600;

    private MiaoshaUserKey(int expireSeconds, String prefix){
        super(expireSeconds, prefix);
    }

    public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE, "tk");
    public static MiaoshaUserKey getUserById = new MiaoshaUserKey(0, "id");

}
