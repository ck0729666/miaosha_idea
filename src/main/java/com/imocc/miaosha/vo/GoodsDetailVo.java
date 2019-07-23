package com.imocc.miaosha.vo;

import com.imocc.miaosha.domain.MiaoshaUser;

public class GoodsDetailVo {

    private GoodsVo goods;
    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private MiaoshaUser user;

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;

    }

    public int getMiaoshaStatus() {
        return miaoshaStatus;
    }

    public void setMiaoshaStatus(int miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    /**
     public GoodsDetailVo() {
     }

     public GoodsDetailVo(GoodsVo goods, int miaoshaStatus, int remainSeconds, MiaoshaUser user) {
     this.goods = goods;
     this.miaoshaStatus = miaoshaStatus;
     this.remainSeconds = remainSeconds;
     this.user = user;
     }*/
}