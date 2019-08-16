package com.taotao.mapper;

import java.util.List;

import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TbItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface TbItemMapper {

    @Select("SELECT * FROM taotao.tbitem WHERE id=#{id}")
    TbItem findItemById(long id);
    @Select("SELECT * FROM taotao.tbitem")
    List<TbItem> findItem();
    @Delete("<script> DELETE FROM taotao.tbitem WHERE id IN <foreach collection='array' item='id' open='(' separator=',' close=')'>#{id}</foreach> </script>")
    int deleteItems(long[] ids);
    @Update("<script> UPDATE taotao.tbitem SET status= '2' WHERE id IN <foreach collection='array' item='id' open='(' separator=',' close=')'>#{id}</foreach> </script>")
    int instockItems(long[] ids);
    @Update("<script> UPDATE taotao.tbitem SET status= '1' WHERE id IN <foreach collection='array' item='id' open='(' separator=',' close=')'>#{id}</foreach> </script>")
    int reshelfItems(long[] ids);
    @Insert("INSERT INTO taotao.tbitem(id, title, sellPoint, price, num, barcode, image, cid, created, updated) VALUE(#{id},#{title},#{sellPoint},#{price},#{num},#{barcode},#{image},#{cid},#{created},#{updated})")
    int addItems(TbItem tbItem);
    @Update("UPDATE taotao.tbitem SET id =#{id},title=#{title},sellPoint=#{sellPoint},price=#{price},num=#{num},barcode=#{barcode},image=#{image},cid=#{cid},created=#{created},updated=#{updated}")
    int updateItems(TbItem tbItem);
}