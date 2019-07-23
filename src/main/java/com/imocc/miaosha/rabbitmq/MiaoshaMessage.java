package com.imocc.miaosha.rabbitmq;

import com.imocc.miaosha.domain.MiaoshaUser;

public class MiaoshaMessage {

    private Long goodsId;
    private MiaoshaUser user;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }
}