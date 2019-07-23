package com.imocc.miaosha.dao;

import com.imocc.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MiaoshaUserDao {

    @Select("SELECT * FROM miaosha_user where id = #{id}")
    MiaoshaUser getUserById(@Param("id")long id);

    @Update("update miaosha_user set password = #{password} where id = #{id}")
    int updatePwd(MiaoshaUser user);
}
