package com.imocc.miaosha.redis;

public interface KeyPrefix {

    int expireSeconds();

    String getPrefix();
}
