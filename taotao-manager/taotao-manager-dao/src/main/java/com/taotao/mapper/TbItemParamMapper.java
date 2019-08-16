package com.taotao.mapper;


import com.taotao.pojo.TbItemParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface TbItemParamMapper {

    @Select("SELECT * FROM taotao.tbitemparam WHERE itemCatId = #{itemCatId}")
    TbItemParam findItemParamByCatId(long itemCatId);
    @Insert("INSERT INTO taotao.tbitemparam(itemCatId, paramData, created, updated) VALUE (#{itemCatId},#{paramData},#{created},#{updated})")
    void addItemParam(TbItemParam tbItemParam);
}