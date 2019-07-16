package com.imooc.miaosha.service;


import com.imooc.miaosha.dao.OrderDao;
import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.redis.OrderKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RedisService redisService;

    //查是否秒杀成功
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId){
        //return orderDao.getOrderByUserIdByGoodsId(userId, goodsId);
        return redisService.get(OrderKey.getMiaoshaOrderByUseridGoodsid, ""+userId+"_"+goodsId, MiaoshaOrder.class);
    }

    //下订单
    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goodsVo){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goodsVo.getId());
        orderInfo.setGoodsName(goodsVo.getGoodsName());
        orderInfo.setGoodsPrice(goodsVo.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderDao.insertOrderInfo(orderInfo);

        //给user_id和goods_id加唯一索引，虽然我们后期会加入验证码，使一个用户不会同时发出不同的请求，但是还是要防止
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goodsVo.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);

        //生成订单以后，把订单加入缓存中
        redisService.set(OrderKey.getMiaoshaOrderByUseridGoodsid, ""+user.getId()+"_"+goodsVo.getId(), MiaoshaOrder.class);

        return orderInfo;
    }

    public OrderInfo getOrderById(long orderId){
        return orderDao.getOrderById(orderId);
    }

}
















