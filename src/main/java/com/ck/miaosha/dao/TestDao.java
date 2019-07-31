package com.ck.miaosha.dao;

import com.ck.miaosha.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface TestDao {

    @Select("SELECT * FROM miaosha_order WHERE goods_id = '1'")
    Map<String, Object> findAllOrders();

    @Select("SELECT * FROM user WHERE id = #{id}")
    User select(@Param(value="id")int id);

    @Insert("insert into user values(#{id}, #{name})")
    int insertUser(User user);
}


