package com.ck.miaosha.redis;

public class OrderKey extends BasePrefix{


    public OrderKey(String prefix){
        super(prefix);
    }

    public static OrderKey getMiaoshaOrderByUseridGoodsid = new OrderKey("mobug");


}
