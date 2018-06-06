package com.cmazxiaoma.mallSeckill.controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author cmazxiaoma
 * @version V1.0
 * @Description: TODO
 * @date 2018/6/6 19:49
 */
public abstract class BaseController {

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected HttpSession httpSession;

    protected ModelMap modelMap;

    @ModelAttribute
    protected void init(HttpServletRequest request, HttpServletResponse response,
                        HttpSession httpSession, ModelMap modelMap) {
        this.request = request;
        this.response = response;
        this.httpSession = httpSession;
        this.modelMap = modelMap;
    }
}
