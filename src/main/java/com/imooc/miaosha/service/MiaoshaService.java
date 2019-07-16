package com.imooc.miaosha.service;

import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.redis.MiaoshaKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;


    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goodsVo){
        //减库存 下2个订单
        boolean flag = goodsService.reduceStock(goodsVo);
        //if(flag){
            return orderService.createOrder(user, goodsVo);
        //}else{
            //setGoodsOver(goodsVo.getId());
           // return null;
        //}
    }

    private void setGoodsOver(Long goodsId){
        redisService.set(MiaoshaKey.isGoodsOver, ""+goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver, ""+goodsId);
    }















}
