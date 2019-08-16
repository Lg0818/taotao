package com.taotao.mapper;

import com.taotao.pojo.SearchItem;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SearchItemMapper {
    @Select("SELECT a.id,a.title,a.sellPoint,a.price,a.image,b.`name` categoryName,c.itemDesc FROM taotao.tbitem a LEFT JOIN taotao.tbitemcat b ON a.cid=b.id LEFT JOIN taotao.tbitemdesc c ON a.id=c.itemId WHERE a.`status` = 1")
    List<SearchItem> getItemList();
    @Select("SELECT a.id,a.title,a.sellPoint,a.price,a.image,b.name categoryName,c.itemDesc FROM taotao.tbitem a INNER JOIN taotao.tbitemcat b ON a.cid = b.id INNER JOIN taotao.tbitemdesc c ON a.id = c.itemId WHERE a.id = #{itemId}")
    SearchItem getItemById(Long itemId);
}
