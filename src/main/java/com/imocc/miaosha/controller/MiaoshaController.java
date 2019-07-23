package com.imocc.miaosha.controller;


import com.imocc.miaosha.access.AccessLimit;
import com.imocc.miaosha.domain.MiaoshaUser;
import com.imocc.miaosha.rabbitmq.MiaoshaMessage;
import com.imocc.miaosha.redis.MiaoshaKey;
import com.imocc.miaosha.redis.OrderKey;
import com.imocc.miaosha.redis.RedisService;
import com.imocc.miaosha.result.CodeMsg;
import com.imocc.miaosha.service.GoodsService;
import com.imocc.miaosha.service.MiaoshaService;
import com.imocc.miaosha.vo.GoodsVo;
import com.imocc.miaosha.domain.MiaoshaOrder;
import com.imocc.miaosha.rabbitmq.MQSender;
import com.imocc.miaosha.redis.GoodsKey;
import com.imocc.miaosha.result.Result;
import com.imocc.miaosha.service.OrderService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.omg.CORBA.TRANSACTION_MODE;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

    @Autowired
    private MiaoshaService miaoshaService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender sender;

    private HashMap<Long, Boolean> localOverMap = new HashMap<>();

    /**
     * 系统初始化
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception{
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if(goodsVoList == null){
            return;
        }
        for(GoodsVo goods: goodsVoList){
            redisService.set(GoodsKey.getMiaoshaGoodsStock, ""+goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        }
    }

    @RequestMapping(value="/reset", method=RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        for(GoodsVo goods : goodsList) {
            goods.setStockCount(10);
            redisService.set(GoodsKey.getMiaoshaGoodsStock, ""+goods.getId(), 10);
            localOverMap.put(goods.getId(), false);
        }
        redisService.delete(OrderKey.getMiaoshaOrderByUseridGoodsid);
        redisService.delete(MiaoshaKey.isGoodsOver);
        miaoshaService.reset(goodsList);
        return Result.success(true);

    }

    /**
     * qps:900
     * 5000 * 10
     * qps:2086
     */
    @RequestMapping(value = "/{path}/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(Model model, MiaoshaUser user, @RequestParam("goodsId")long goodsId, @PathVariable("path")String path){

        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //验证path
        boolean check = miaoshaService.checkPath(user, goodsId, path);
        if(!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //库存内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if(over) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, ""+goodsId);
        if(stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order!=null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        sender.sendMiaoshaMessage(mm);
        return Result.success(0);

        /**第二版
        model.addAttribute("user", user);
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //判断库存
        Long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, ""+goodsId);
        if(stock < 0){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否秒杀到
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        sender.sendMiaoshamessage(mm);
        return Result.success(0);
         */


        /**第一版：判断库存
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);//要防止同一用户发出不同请求
        System.out.println(goodsVo.getGoodsName()+"----"+user.getNickname());
        int stockCount = goodsVo.getStockCount();
        if(stockCount<=0){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否秒杀到
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        //OrderInfo orderInfo = orderService.createOrder(user, goodsVo);
        //减库存，下两个订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goodsVo);
        System.out.println("---------------");
        orderInfo.setId(1L);
        System.out.println(orderInfo.getId());
        /**
         model.addAttribute("orderInfo", orderInfo);
         model.addAttribute("goods", goodsVo);

        return Result.success(orderInfo);
         */
    }

    /**
     * 返回值：orderId:成功 -1：秒杀失败 0：排队中
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, MiaoshaUser user, @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }

    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value="/path", method=RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request, MiaoshaUser user,
                                         @RequestParam("goodsId")long goodsId,
                                         @RequestParam(value="verifyCode", defaultValue ="0")int verifyCode) {

        if(user==null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if(!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path = miaoshaService.createMiaoshaPath(user, goodsId);
        return Result.success(path);
    }
    @RequestMapping(value="/vefiryCode", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCode(HttpServletResponse response, MiaoshaUser user, @RequestParam("goodsId")long goodsId) {
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image = miaoshaService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        }catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
    }




}





















