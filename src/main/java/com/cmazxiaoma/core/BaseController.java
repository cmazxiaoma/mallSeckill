package com.cmazxiaoma.core;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public abstract class BaseController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;
    protected ModelMap modelMap;

    @ModelAttribute
    protected void initSpringMvcApiModel(HttpServletRequest request, HttpServletResponse response,
                                         HttpSession session, ModelMap modelMap) {
        this.request = request;
        this.response = response;
        this.session = session;
        this.modelMap = modelMap;
    }
}
