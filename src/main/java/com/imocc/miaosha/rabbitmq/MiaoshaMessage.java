package com.imocc.miaosha.rabbitmq;

import com.imocc.miaosha.domain.MiaoshaUser;

public class MiaoshaMessage {
    private MiaoshaUser user;
    private long goodsId;
    public MiaoshaUser getUser() {
        return user;
    }
    public void setUser(MiaoshaUser user) {
        this.user = user;
    }
    public long getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
