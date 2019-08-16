package com.taotao.service;


import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItemParam;

public interface ItemParamService {
    /**
     * 根据分类id查询数据库中模板表是否存在
     * @param itemCatId 分类id
     * @return 200表示存在
     */
    TaotaoResult getItemParamByCid(long itemCatId);

    TaotaoResult addItemParam(TbItemParam tbItemParam);

    String getItemParamByItemId(Long itemId);
}
