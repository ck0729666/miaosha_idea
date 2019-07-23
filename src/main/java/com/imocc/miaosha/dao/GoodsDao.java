package com.imocc.miaosha.dao;


import com.imocc.miaosha.domain.MiaoshaGoods;
import com.imocc.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsDao {

    @Select("select goods.*, mg.stock_count, mg.miaosha_price, mg.start_date, mg.end_date FROM miaosha_goods mg INNER JOIN goods ON mg.goods_id = goods.id")
    List<GoodsVo> listGoodsVo();


    @Select("select goods.*, mg.stock_count, mg.miaosha_price, mg.start_date, mg.end_date FROM miaosha_goods mg INNER JOIN goods ON mg.goods_id = goods.id where goods.id = #{id}")
    GoodsVo getGoodsVolById(@Param(value = "id") long id);

    //stock_count>0保证不会超卖
    @Update("update miaosha_goods set stock_count = stock_count-1 where goods_id = #{id} and stock_count>0")
    int reduceStock(MiaoshaGoods g);

    @Update("update miaosha_goods set stock_count = #{stockCount} where goos_id = #{goodsId}")
    int resetStock(MiaoshaGoods g);
}

