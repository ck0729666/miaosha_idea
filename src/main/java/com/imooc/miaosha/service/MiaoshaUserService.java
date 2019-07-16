package com.imooc.miaosha.service;

import com.imooc.miaosha.dao.MiaoshaUserDao;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.exception.GlobalException;
import com.imooc.miaosha.redis.MiaoshaUserKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.util.UUIDUtil;
import com.imooc.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {


    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private MiaoshaUserDao miaoshaUserDao;

    @Autowired
    private RedisService redisService;



    public MiaoshaUser getUserById(long id){

        MiaoshaUser user = redisService.get(MiaoshaUserKey.getUserById, ""+id, MiaoshaUser.class);

        if(user != null){
            return user;
        }

        user = miaoshaUserDao.getUserById(id);

        redisService.set(MiaoshaUserKey.getUserById, ""+id, user);

        return user;
    }

    public boolean updatePassword(String token, long id, String formPass){
        //取user
        MiaoshaUser user = redisService.get(MiaoshaUserKey.getUserById, ""+id, MiaoshaUser.class);
        if(user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库
        MiaoshaUser toUpdateUser = new MiaoshaUser();
        toUpdateUser.setId(id);
        toUpdateUser.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        int flag = miaoshaUserDao.updatePwd(toUpdateUser);
        //处理缓存
        redisService.delete(MiaoshaUserKey.getUserById, ""+id);
        user.setPassword(toUpdateUser.getPassword());
        redisService.set(MiaoshaUserKey.token, token, user);
        return flag > 0?true:false;
    }


    public MiaoshaUser getByToken(HttpServletResponse response, String token){
        if(StringUtils.isEmpty(token)){
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        //延长有效期
        if(user != null){
            addCookie(response,token,user);
        }
        return user;
    }


    public String login(HttpServletResponse response, LoginVo loginVo){
        if(loginVo == null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);

        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getUserById(Long.parseLong(mobile));
        if(user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPass = user.getPassword();
        String salt = user.getSalt();
        String inputDbPass = MD5Util.formPassToDBPass(formPass, salt);


        if(!dbPass.equals(inputDbPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成token
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);

        return token;
    }

    public void addCookie(HttpServletResponse response, String token, MiaoshaUser user){
        /**
         * 把用户和token值保存到redis中
         * 把token保存到cookie中
         */
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}








