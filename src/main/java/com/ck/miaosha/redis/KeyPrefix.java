package com.ck.miaosha.redis;

public interface KeyPrefix {

    int expireSeconds();

    String getPrefix();
}
