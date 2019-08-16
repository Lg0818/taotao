package com.taotao.order.controller;


import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Value("${TT_CART}")
    private String TT_CART;
    @RequestMapping("/order/order-cart")
    public String showOrder(HttpServletRequest request, Model model){
        List<TbItem> itemList = getItemList(request);
        model.addAttribute("cartList",itemList);
        return "order-cart";
    }
    private List<TbItem> getItemList(HttpServletRequest request){
        //cookie(key=TT_CART,value=商品json数据)
        String json = CookieUtils.getCookieValue(request, TT_CART, true);
        if(StringUtils.isNotBlank(json)){
            List<TbItem> reuslt = JsonUtils.jsonToList(json, TbItem.class);
            return reuslt;
        }
        return new ArrayList<>();
    }

    @RequestMapping(value = "/order/create",method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo, HttpServletRequest request){
        TbUser user = (TbUser) request.getAttribute("user");
        orderInfo.setUserId(user.getId());
        orderInfo.setBuyerNick(user.getUserName());
        // 3、调用Service创建订单。
        TaotaoResult result = orderService.createOrder(orderInfo);
        //取订单号
        String orderId = result.getData().toString();
        System.out.println(result.getStatus());
        // a)需要Service返回订单号
        request.setAttribute("orderId", orderId);
        request.setAttribute("payment", orderInfo.getPayment());
        // b)当前日期加三天。
        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusDays(3);
        request.setAttribute("date", dateTime.toString("yyyy-MM-dd"));
        return "success";
    }

}
