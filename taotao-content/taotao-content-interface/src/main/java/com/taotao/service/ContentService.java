package com.taotao.service;

import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

public interface ContentService {
    //查询内容结果
    List<TbContent> findContentAll(long categoryId);
    //添加内容
    TaotaoResult addContent(TbContent tbContent);
    //删除内容
    TaotaoResult deleteContentById(long[] ids);
    //修改内容
    TaotaoResult updateContentById(TbContent content);


}
