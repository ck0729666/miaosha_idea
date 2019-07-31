package com.ck.miaosha.controller;

import com.ck.miaosha.domain.MiaoshaUser;
import com.ck.miaosha.redis.RedisService;
import com.ck.miaosha.result.Result;
import com.ck.miaosha.service.MiaoshaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MiaoshaUserService userService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> info(Model model, MiaoshaUser user){
        return Result.success(user);
    }

}
