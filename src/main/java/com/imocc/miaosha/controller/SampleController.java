package com.imocc.miaosha.controller;

import com.imocc.miaosha.domain.User;
import com.imocc.miaosha.rabbitmq.MQSender;
import com.imocc.miaosha.result.Result;
import com.imocc.miaosha.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    private TestService testService;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","Joshua");
        return "hello";
    }
    @RequestMapping("/test1")
    public String testMysql(Model model){
        Map<String, Object> map =  testService.findAll();
        model.addAttribute("orderList",map);
        return "hello";

    }

    /**
     * @responseBody注解的作用是
     * 将controller的方法返回的对象通过适当的转换器转换为指定的格式之后，
     * 写入到response对象的body区，通常用来返回JSON数据或者是XML
     * @return
     */
    @RequestMapping("/test2")
    @ResponseBody
    public Result<User> testUser(){
        User user =testService.selectName(1);
        return Result.success(user);
    }

    @RequestMapping("/test3")
    @ResponseBody
    public boolean testTX(){
        boolean flag  =testService.insert();
        return flag;
    }

    @Autowired
    MQSender sender;

    @RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq(){
        sender.send("ckckck");
        return Result.success("Helloworld");
    }

    /**
    @RequestMapping("/mq/topic")
    @ResponseBody
    public Result<String> topic(){
        sender.sendTopic("hello imooc");
        return Result.success("Hello world");
    }*/





























}
















