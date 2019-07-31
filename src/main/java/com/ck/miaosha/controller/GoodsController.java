package com.ck.miaosha.controller;


import com.alibaba.druid.util.StringUtils;
import com.ck.miaosha.domain.MiaoshaUser;
import com.ck.miaosha.redis.RedisService;
import com.ck.miaosha.service.GoodsService;
import com.ck.miaosha.vo.GoodsDetailVo;
import com.ck.miaosha.vo.GoodsVo;
import com.ck.miaosha.service.MiaoshaUserService;
import com.ck.miaosha.redis.GoodsKey;
import com.ck.miaosha.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private MiaoshaUserService miaoshaUserService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * qps:1108
     * 5000*10
     * qps:2711
     * @param request
     * @param response
     * @param model
     * @param user
     * @return
     */
    @RequestMapping(value = "/to_list", produces = "text/html")
    //@RequestMapping(value="/to_list")
    @ResponseBody
    public String showGoods(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user) {
        model.addAttribute("user", user);
        //页面缓存
        //取缓存
        /**
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }*/

        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        //return "goods_list";

        SpringWebContext ctx = new SpringWebContext(request,response, request.getServletContext(),request.getLocale(),
                 model.asMap(), applicationContext);

        //手动渲染
        String html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    /**
    @RequestMapping("/to_detail/{id}")
    public String showGoodsDetail(Model model, @PathVariable(value="id")Long id, MiaoshaUser user){



        goodsService.showGoodDetailById(model, id, user);
        return "goods_detail";
    }*/
    ///goods_detail.htm?goodsId=

    public String detail2(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user, @PathVariable("goodsId")long goodsId) {
        model.addAttribute("user", user);
        String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
        if(StringUtils.isEmpty(html)) {
            return html;
        }
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now<startAt) {
            miaoshaStatus = 0;
            remainSeconds = (int)(startAt - now)/1000;
        }else if(now > endAt) {
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
        }
        return html;
    }


    @RequestMapping(value="/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(Model model, MiaoshaUser user, @PathVariable("goodsId")long goodsId) {
        //model.addAttribute("user", user);


        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        //model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        //GoodsDetailVo goodsDetailVo = new GoodsDetailVo(goods,miaoshaStatus,remainSeconds,user);
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setMiaoshaStatus(miaoshaStatus);
        vo.setRemainSeconds(remainSeconds);
        vo.setUser(user);

        //model.addAttribute("miaoshaStatus", miaoshaStatus);
        //model.addAttribute("remainSeconds", remainSeconds);
        //return "goods_detail";
        return Result.success(vo);
    }

















}
