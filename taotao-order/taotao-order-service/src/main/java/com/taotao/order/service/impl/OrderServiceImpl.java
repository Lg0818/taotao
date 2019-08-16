package com.taotao.order.service.impl;

import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.dao.OrderMapper;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import com.taotao.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private RedisUtil jedisClient;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;
    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;
    @Value("${ORDER_GEN_KEY}")
    private String ORDER_GEN_KEY;
    @Value("${ORDER_ID_BEGIN}")
    private String ORDER_ID_BEGIN;
    @Value("${ORDER_ITEM_ID_GEN_KEY}")
    private String ORDER_ITEM_ID_GEN_KEY;

    @Override
    public TaotaoResult createOrder(OrderInfo orderInfo) {
        if(!jedisClient.exists(ORDER_GEN_KEY)){
            jedisClient.set(ORDER_GEN_KEY,ORDER_ID_BEGIN);
        }
        //使用redis的自增长方法  100544  变成 100545
        String orderId = jedisClient.incr(ORDER_GEN_KEY).toString();
        //设置订单id
        orderInfo.setOrderId(orderId);
        //设置邮寄费用
        orderInfo.setPostFee("0");
        //1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        orderInfo.setStatus(1);
        Date date = new Date();
        orderInfo.setCreateTime(date);
        orderInfo.setUpdateTime(date);
        // 3、向订单表插入数据。
        orderMapper.addOrderInfo(orderInfo);
        //得到订单表中的所有订单项
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        //订单明细 都是从页面传递过来的  商品id 商品图片 商品数量 商品金额
        for (TbOrderItem orderItem:orderItems) {
            //生成明细id
            Long orderItemId = jedisClient.incr(ORDER_ITEM_ID_GEN_KEY);
            //设置本身的id
            orderItem.setId(orderItemId.toString());
            //设置订单的id
            orderItem.setOrderId(orderId);
            int orderItemCount = tbOrderItemMapper.addOrderItem(orderItem);
        }
        //地址明细
        // 5、向订单物流表插入数据。
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(date);
        orderShipping.setUpdated(date);
        int tbOrderShippingCount = tbOrderShippingMapper.addOrderShipping(orderShipping);

        
        //页面想要看到 订单号
        return TaotaoResult.ok(orderId);
    }
}
