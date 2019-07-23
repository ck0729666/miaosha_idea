package com.imocc.miaosha.dao;

import com.imocc.miaosha.domain.MiaoshaOrder;
import com.imocc.miaosha.domain.OrderInfo;
import org.apache.ibatis.annotations.*;


@Mapper
public interface OrderDao {

    /**
    @Select("select stock_count from miaosha_goods where id = #{id}")
    int selectMiaoshaGoodsStockCount(@Param(value = "id") long id);

    @Update("update miaosha_goods set stock_count=stock_count-1 where id = #{id}")
    int delStockCount(@Param(value="id") long id);*/

    //给userId和goodsId加唯一索引
    @Select("select * from miaosha_order where user_id = #{userId} and goods_id = #{goodsId}")
    MiaoshaOrder getOrderByUserIdByGoodsId(@Param(value="userId")long userId, @Param(value="goodsId")long goodsId);

    @Insert(("insert into miaosha_order (user_id, goods_id, order_id) value(#{userId}, #{goodsId}, #{orderId})"))
    int insertMiaoshaOrder(MiaoshaOrder order);

    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    int insertOrderInfo(OrderInfo orderInfo);


    @Select("select * from order_info where id = #{orderId}")
    OrderInfo getOrderById(@Param(value = "orderId")long orderId);

    @Select("delete from order_info")
    void deleteOrders();

    @Delete("delete from miaosha_order")
    void deleteMiaoshaOrders();
}
