package com.taotao.mapper;

import com.taotao.pojo.TbOrderItem;
import org.apache.ibatis.annotations.Insert;

public interface TbOrderItemMapper {
    @Insert("INSERT INTO taotao.tborderitem(id, itemId, orderId, num, title, price, totalFee, picPath) VALUE (#{id},#{itemId},#{orderId},#{num},#{title},#{price},#{totalFee},#{picPath})")
    int addOrderItem(TbOrderItem orderItem);
}