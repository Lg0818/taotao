package com.taotao.mapper;


import com.taotao.pojo.TbContentCategory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface TbContentCategoryMapper {
    @Select("SELECT * FROM taotao.tbcontentcategory WHERE parentId =#{parentId}")
    List<TbContentCategory> findTbContentCategoryById(long parentId);

    @Select("SELECT * FROM taotao.tbcontentcategory WHERE id = #{id}")
    TbContentCategory findContentCategoryById(Long parentId);

    @Update("UPDATE taotao.tbcontentcategory SET isParent = #{isParent} WHERE id = #{id}")
    void updateContentCategory(TbContentCategory category);

    @Insert("INSERT INTO taotao.tbcontentcategory(parentId, name, status, sortOrder, isParent, created, updated) VALUE (#{parentId},#{name},#{status},#{sortOrder},#{isParent},#{created},#{updated})")
    void addContentCategory(TbContentCategory contentCategory);
    @Update("UPDATE taotao.tbcontentcategory SET name = #{name} WHERE id = #{id}")
    int updateContentCategoryName(TbContentCategory category);
}