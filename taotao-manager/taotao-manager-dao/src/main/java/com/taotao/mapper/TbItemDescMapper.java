package com.taotao.mapper;

import com.taotao.pojo.TbItemDesc;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface TbItemDescMapper {
    @Insert("INSERT INTO taotao.tbitemdesc (itemId, itemDesc, created, updated) VALUE (#{itemId},#{itemDesc},#{created},#{updated})")
    int addItemDesc(TbItemDesc itemDesc);
    @Update("UPDATE taotao.tbitemdesc SET itemId=#{itemId},itemDesc=#{itemDesc},created=#{created},updated=#{updated}")
    int updateItemDesc(TbItemDesc itemDesc);
    @Select("SELECT * FROM taotao.tbitemdesc WHERE itemId = #{id}")
    TbItemDesc findItemDescById(long id);
}