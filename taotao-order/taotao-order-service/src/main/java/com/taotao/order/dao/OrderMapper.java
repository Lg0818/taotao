package com.taotao.order.dao;

import com.taotao.order.pojo.OrderInfo;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Component;

@Component
public interface OrderMapper {
    @Insert("INSERT INTO tborder(orderId, payment, paymentType, postFee, status, createTime, updateTime, paymentTime, consignTime, endTime, closeTime, shippingName, shippingCode, userId, buyerMessage, buyerNick, buyerRate) VALUE (#{orderId},#{payment},#{paymentType},#{postFee},#{status},#{createTime},#{updateTime},#{paymentTime},#{consignTime},#{endTime},#{closeTime},#{shippingName},#{shippingCode},#{userId},#{buyerMessage},#{buyerNick},#{buyerRate})")
    void addOrderInfo(OrderInfo orderInfo);
}
