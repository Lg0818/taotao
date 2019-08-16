package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    @RequestMapping("/page/register")
    public String register(){
        return "register";
    }
    //当前页面的 url地址
    @RequestMapping("/page/login")
    public String login(){
        return "login";
    }
}
