package com.taotao.mapper;

import java.util.List;

import com.taotao.pojo.TbItem;
import org.apache.ibatis.annotations.Select;

public interface TbItemMapper {

    @Select("SELECT * FROM tbitem WHERE id=#{id}")
    TbItem findItemById(long id);
}