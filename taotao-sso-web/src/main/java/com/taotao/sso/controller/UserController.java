package com.taotao.sso.controller;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Value("${TT_TOKEN}")
    private String TT_TOKEN;

    @RequestMapping(value = "/check/{param}/{type}",produces = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
    @ResponseBody
    public String checkUserData(@PathVariable String param,@PathVariable int type,String callback){
        TaotaoResult result = userService.checkData(param, type);
        if(callback!=null){
            //我们就认为 他是需要jsonp的
            return callback + "(" + JsonUtils.objectToJson(result) + ");";
        }
        return JsonUtils.objectToJson(result);
    }
    @RequestMapping(value = "/register",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
    @ResponseBody
    public String register(TbUser tbUser, String callback){
        TaotaoResult result = userService.createUser(tbUser);
        if(callback!=null){
            //我们就认为 他是需要jsonp的
            return callback + "(" + JsonUtils.objectToJson(result) + ");";
        }
        return JsonUtils.objectToJson(result);
    }
    @RequestMapping(value = "/login",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
    @ResponseBody
    public String login(String userName, String passWord, String callback, HttpServletRequest request, HttpServletResponse response){
        TaotaoResult result = userService.login(userName, passWord);
        //吧token存入到 cookie中
        CookieUtils.setCookie(request,response,TT_TOKEN,result.getData().toString());
        if(callback!=null){
            //我们就认为 他是需要jsonp的
            return callback + "(" + JsonUtils.objectToJson(result) + ");";
        }
        return JsonUtils.objectToJson(result);
    }
    @RequestMapping(value = "/token/{token}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
    @ResponseBody
    public String getUserByToken(@PathVariable String token,String callback){
        TaotaoResult result = userService.getUserByToken(token);
        if(callback!=null){
            //我们就认为 他是需要jsonp的
            return callback + "(" + JsonUtils.objectToJson(result) + ");";
        }
        return JsonUtils.objectToJson(result);
    }


    @RequestMapping(value = "/logout/{token}",method = RequestMethod.GET)
    public String logout(@PathVariable String token,String callback){
        TaotaoResult result = userService.logout(token);
        return "login";
    }
}

