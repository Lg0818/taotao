package com.taotao.mapper;

import java.util.List;

import com.taotao.pojo.TbItemCat;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

public interface TbItemCatMapper {
    @Select("SELECT * FROM taotao.tbitemcat WHERE parentId = #{id}")
    List<TbItemCat> findTbItemCatByParentId(Long id);
    @Delete("<script> DELETE FROM taotao.tbitemdesc WHERE itemId IN <foreach collection='array' item='id' open='(' separator=',' close=')'>#{itemId}</foreach> </script>")
    int deleteItemDesc(long[] itemId);
}