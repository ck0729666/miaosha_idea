package com.ck.miaosha.service;


import com.ck.miaosha.domain.MiaoshaGoods;
import com.ck.miaosha.vo.GoodsVo;
import com.ck.miaosha.dao.GoodsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId){
        return goodsDao.getGoodsVolById(goodsId);
    }

    public boolean reduceStock(GoodsVo goodsVo){
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goodsVo.getId());
        int flag = goodsDao.reduceStock(g);
        return flag > 0;
    }

    public void resetStock(List<GoodsVo> goodsList) {
        for(GoodsVo goods : goodsList) {
            MiaoshaGoods g = new MiaoshaGoods();
            g.setGoodsId(goods.getId());
            g.setStockCount(goods.getStockCount());
            goodsDao.resetStock(g);
        }
    }

    /**
    public MiaoshaGoods showGoodDetailById(Model model, Long id, MiaoshaUser user){

        model.addAttribute("user", user);

        GoodsVo goods = goodsDao.getGoodsVolById(id);
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

    }*/









}
