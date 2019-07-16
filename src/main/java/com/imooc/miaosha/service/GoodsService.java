package com.imooc.miaosha.service;


import com.imooc.miaosha.dao.GoodsDao;
import com.imooc.miaosha.domain.MiaoshaGoods;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.exception.GlobalException;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    public List<GoodsVo> showGoodList(){
        return goodsDao.showGoodList();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId){
        return goodsDao.showGoodsDetailById(goodsId);
    }

    public boolean reduceStock(GoodsVo goodsVo){
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goodsVo.getId());
        int flag = goodsDao.reduceStock(g);
        return flag > 0;
    }

    public MiaoshaGoods showGoodDetailById(Model model, Long id, MiaoshaUser user){

        model.addAttribute("user", user);

        GoodsVo goods = goodsDao.showGoodsDetailById(id);
        model.addAttribute("goods", goods);
        if(goods==null){
            throw  new GlobalException(CodeMsg.ORDER_NOT_EXIST);
        }
        //SimpleDateFormat sdf = new SimpleDateFormat("");
        long startDate = goods.getStartDate().getTime();
        long endDate = goods.getEndDate().getTime();
        long nowTime = System.currentTimeMillis();

        long remainSeconds = 0;

        int miaoshaStatus = 0;

        if(startDate > nowTime){
            miaoshaStatus = 0;
            remainSeconds = (startDate-nowTime)/1000;
        }else if(nowTime > endDate){
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else{
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        return null;

    }









}
