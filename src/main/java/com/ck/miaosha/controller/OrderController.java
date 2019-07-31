package com.ck.miaosha.controller;


import com.ck.miaosha.domain.MiaoshaUser;
import com.ck.miaosha.domain.OrderInfo;
import com.ck.miaosha.result.CodeMsg;
import com.ck.miaosha.service.GoodsService;
import com.ck.miaosha.service.OrderService;
import com.ck.miaosha.vo.GoodsVo;
import com.ck.miaosha.result.Result;
import com.ck.miaosha.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, MiaoshaUser user, @RequestParam("orderId")long orderId){

        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if(orderInfo==null){
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = orderInfo.getGoodsId();

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setGoods(goods);
        orderDetailVo.setOrder(orderInfo);

        return Result.success(orderDetailVo);
    }






}
