package com.taotao.order.interceptor;


import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import com.taotao.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor{
    @Value("${TT_TOKEN}")
    private String TT_TOKEN;
    @Value("${SSO_LOGIN_URL}")
    private String SSO_LOGIN_URL;
    @Autowired
    private UserService userService;

    //请求之前 走到controller里面的方法执行之前
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        String token = CookieUtils.getCookieValue(httpServletRequest, TT_TOKEN, true);
        //取当前请求的url

        // false 会被拦截 不会走到下面的地方去
        if(StringUtils.isBlank(token)){
            String url = httpServletRequest.getRequestURL().toString();
            //SSO_LOGIN_URL=  http://lcalhost:8088/page/login.html?redirectUrl=当前页面
            String jumpUrl = SSO_LOGIN_URL+"?redirectUrl="+url;
            httpServletResponse.sendRedirect(jumpUrl);
            return false;
        }
        TaotaoResult result = userService.getUserByToken(token);
        //redis中帮我们保存用户信息为 30分钟  你在这个页面挂着 等着个 100分钟 redis消失不见了
        if(result.getStatus() != 200){
            String url = httpServletRequest.getRequestURL().toString();
            String jumpUrl = SSO_LOGIN_URL+"?redirectUrl="+url;
            httpServletResponse.sendRedirect(jumpUrl);
            return false;
        }
        //走到这里 表示用户一定登录
        TbUser tbUser = (TbUser) result.getData();
        httpServletRequest.setAttribute("user",tbUser);
        return true;
    }
    //请求之后 走到controller里面的方法执行以后 但是return之前 全局数据处理
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    // return之后 统计 做日志管理
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
