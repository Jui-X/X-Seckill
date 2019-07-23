package com.juix.seckill.controller;

import com.juix.seckill.common.Result;
import com.juix.seckill.domain.User;
import com.juix.seckill.enums.SecKillGoodsEnums;
import com.juix.seckill.service.GoodsService;
import com.juix.seckill.service.UserService;
import com.juix.seckill.utils.RedisService;
import com.juix.seckill.utils.key.GoodsKey;
import com.juix.seckill.vo.GoodsDetailVo;
import com.juix.seckill.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 20:53
 **/
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value = "/list", produces="text/html")
    public <T> Result<T> list(HttpServletRequest request, HttpServletResponse response, Model model, User user) {
        // 页面缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return Result.ok(html);
        }

        List<GoodsVo> goodsList = goodsService.listGoodsVo();

        SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(),
                model.asMap(), applicationContext);
        // 手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }

        return Result.ok(goodsList);
    }

    @RequestMapping(value = "/detail/{goodsID}", produces="text/html")
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model,
                                        User user, @PathVariable("goodsID") long goodsID) {

        GoodsVo good = goodsService.getGoodsVoByGoodsID(goodsID);

        long startAt = good.getStartDate().getTime();
        long endAt = good.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int secKillStatus = 0;
        int remainSeconds = 0;
        if (now < startAt) {
            // 秒杀尚未开始...
            secKillStatus = SecKillGoodsEnums.SECKILL_NOT_START.getCode();
            remainSeconds = (int) (now - startAt);
        } else if (now > endAt) {
            // 秒杀已经结束
            secKillStatus = SecKillGoodsEnums.SECKILL_END.getCode();
            remainSeconds = -1;
        } else {
            // 秒杀进行中
            secKillStatus = SecKillGoodsEnums.SECKILL_LASTING.getCode();
            remainSeconds = 0;
        }
        good.setSecKillStatus(secKillStatus);
        good.setRemainSeconds(remainSeconds);

        GoodsDetailVo goodsDetail = new GoodsDetailVo();
        goodsDetail.setGoods(good);
        goodsDetail.setUser(user);
        goodsDetail.setMiaoshaStatus(secKillStatus);
        goodsDetail.setRemainSeconds(remainSeconds);

        return Result.ok(goodsDetail);
    }
}
