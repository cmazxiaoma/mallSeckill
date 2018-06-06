package com.cmazxiaoma.mallSeckill.controller;

import com.cmazxiaoma.core.ResultCode;
import com.cmazxiaoma.mallSeckill.enums.SeckillStateEnum;
import com.cmazxiaoma.mallSeckill.model.Seckill;
import com.cmazxiaoma.mallSeckill.service.ISeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import java.awt.*;
import java.util.List;

/**
 * @author cmazxiaoma
 * @version V1.0
 * @Description: TODO
 * @date 2018/6/6 19:45
 */
@Controller
@RequestMapping("/fastJsonHttpMessageConverter")
public class FastJsonHttpMessageConverterController extends BaseController {

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ISeckillService seckillService;

    @GetMapping(value = "/list", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String list() {
        List<Seckill> list = seckillService.findAll();
        modelMap.put("list", list);
        SpringWebContext springWebContext = new SpringWebContext(request, response, request.getServletContext(),
                request.getLocale(), modelMap, applicationContext);
        String html = thymeleafViewResolver.getTemplateEngine().process("list", springWebContext);
        return html;
    }
}
