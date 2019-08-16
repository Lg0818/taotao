package com.taotao.mapper;


import com.taotao.pojo.TbContent;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface TbContentMapper {
    @Select("SELECT * FROM taotao.tbcontent WHERE categoryId = #{categoryId}")
    List<TbContent> findTbContentAll(long categoryId);
    @Insert("INSERT INTO taotao.tbcontent(categoryId, title, subTitle, titleDesc, url, pic, pic2, content, created, updated) VALUE (#{categoryId},#{title},#{subTitle},#{titleDesc},#{url},#{pic},#{pic2},#{content},#{created},#{updated})")
    void addContent(TbContent tbContent);
    @Delete("<script> DELETE FROM taotao.tbcontent WHERE id IN <foreach collection='array' item='id' open='(' separator=',' close=')'>#{id}</foreach> </script>")
    void deleteContent(long[] ids);
    @Update("UPDATE taotao.tbcontent SET categoryId=#{categoryId},title=#{title},subTitle=#{subTitle},titleDesc=#{titleDesc},url=#{url},pic=#{pic},pic2=#{pic2},content=#{content},created=#{created},updated=#{updated} WHERE id=#{id}")
    void updateContentById(TbContent content);
}