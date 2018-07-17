package com.cmazxiaoma.core.configure;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.cmazxiaoma.core.ResultCode;
import com.cmazxiaoma.core.ResultVo;
import com.cmazxiaoma.core.ResultVoGenerator;
import com.cmazxiaoma.core.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Spring MVC 配置
 */

@Slf4j
@Configuration
public class WebMvcConfigure extends WebMvcConfigurerAdapter {

    @Value("${spring.profiles.active}")
    private String env;//当前激活的配置文件

    //使用阿里 FastJson 作为JSON MessageConverter
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue,//保留空的字段
                SerializerFeature.WriteNullStringAsEmpty,//String null -> ""
                SerializerFeature.WriteNullNumberAsZero);//Number null -> 0
        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        converters.add(converter);
    }

    //定义登录入口
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/css/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/js/");
        registry.addResourceHandler("/fonts/**")
                .addResourceLocations("classpath:/fonts/");
        super.addResourceHandlers(registry);
    }

    //统一异常处理
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new HandlerExceptionResolver() {
            @Override
            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
                ResultVo.Builder resultVoBuilder = new ResultVo.Builder();
                ResultVo resultVo;
                //业务失败的异常，如"账号或密码错误"
                if (e instanceof ServiceException) {
                    resultVo = resultVoBuilder.code(ResultCode.FAIL.getCode()).msg(e.getMessage()).build();
                    log.info(e.getMessage());
                } else if (e instanceof HttpRequestMethodNotSupportedException) {
                    resultVo = ResultVoGenerator.genCustomResult(ResultCode.REQUEST_METHOD_ERROR);
                } else if (e instanceof HttpMessageNotReadableException | e instanceof JSONException) {
                    resultVo = ResultVoGenerator.genFailResult("JSON parse error, json格式错误!");
                } else if (e instanceof MethodArgumentTypeMismatchException) {
                    resultVo = ResultVoGenerator.genCustomResult(ResultCode.ARGUMENT_TYPE_MISMATCH_ERROR);
                } else if (e instanceof MissingServletRequestParameterException) {
                    resultVo = ResultVoGenerator.genCustomResult(ResultCode.REQUIRED_PARAM_EMPTY);
                } else if (e instanceof MethodArgumentNotValidException) {
                    resultVo = ResultVoGenerator.genCustomResult(ResultCode.PARAM_FORMAT_ERROR);
                } else if (e instanceof NoHandlerFoundException) {
                    resultVo = resultVoBuilder.code(ResultCode.NOT_FOUND.getCode()).msg("接口 [" + request.getRequestURI() + "] 不存在").data("null").build();
                } else if (e instanceof ServletException) {
                    resultVo = resultVoBuilder.code(ResultCode.FAIL.getCode()).msg(e.getMessage()).data("null").build();
                } else if (e instanceof AccessDeniedException) {
                    resultVo = resultVoBuilder.code(ResultCode.FORBIDDEN.getCode()).msg(e.getMessage()).data("null").build();
                } else {
                    resultVo = resultVoBuilder.code(ResultCode.INTERNAL_SERVER_ERROR.getCode()).msg("接口 [" + request.getRequestURI() + "] 内部错误，请联系管理员").data("null").build();
                    String message;

                    if (handler instanceof HandlerMethod) {
                        HandlerMethod handlerMethod = (HandlerMethod) handler;
                        message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                                request.getRequestURI(),
                                handlerMethod.getBean().getClass().getName(),
                                handlerMethod.getMethod().getName(),
                                e.getMessage());
                    } else {
                        message = e.getMessage();
                    }

                    log.error(message, e);
                }

                responseResult(response, resultVo);
                return new ModelAndView();
            }

        });
    }

    //解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //接口签名认证拦截器，该签名认证比较简单，实际项目中可以使用Json Web Token或其他更好的方式替代。
        //开发环境忽略签名认证
        if (!"dev".equals(env)) {
            registry.addInterceptor(new HandlerInterceptorAdapter() {
                @Override
                public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                    //验证签名
                    boolean pass = validateSign(request);

                    if (pass) {
                        return true;
                    } else {
                        log.warn("签名认证失败，请求接口：{}，请求IP：{}，请求参数：{}",
                                request.getRequestURI(), getIpAddress(request), JSON.toJSONString(request.getParameterMap()));

                        ResultVo resultVo = new ResultVo.Builder().code(ResultCode.UNAUTHORIZED.getCode()).msg("签名认证失败").build();
                        responseResult(response, resultVo);
                        return false;
                    }
                }
            });
        }
    }

    private void responseResult(HttpServletResponse response, ResultVo resultVo) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);

        try {
            response.getWriter().write(JSON.toJSONString(resultVo));
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    /**
     * 一个简单的签名认证，规则：
     * 1. 将请求参数按ASCII码排序
     * 2. 拼接为a=value&b=value...这样的字符串（不包含sign）
     * 3. 混合密钥（secret）进行md5获得签名，与请求的签名进行比较
     */
    private boolean validateSign(HttpServletRequest request) {
        String requestSign = request.getParameter("sign");//获得请求签名，如sign=19e907700db7ad91318424a97c54ed57

        if (StringUtils.isEmpty(requestSign)) {
            return false;
        }
        List<String> keys = new ArrayList<String>(request.getParameterMap().keySet());
        keys.remove("sign");//排除sign参数
        Collections.sort(keys);//排序

        StringBuffer sb = new StringBuffer();

        for (String key : keys) {
            sb.append(key).append("=").append(request.getParameter(key)).append("&");//拼接字符串
        }
        String linkString = sb.toString();
        linkString = StringUtils.substring(linkString, 0, linkString.length() - 1);//去除最后一个'&'

        String secret = "cmazxiaoma";//密钥，自己修改
        String sign = DigestUtils.md5Hex(linkString + secret);//混合密钥md5

        return StringUtils.equals(sign, requestSign);//比较
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，那么取第一个ip为客户端ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }

        return ip;
    }
}
