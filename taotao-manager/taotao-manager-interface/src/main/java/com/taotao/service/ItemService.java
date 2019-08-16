package com.taotao.service;

import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

public interface ItemService {
    TbItem findItemById(long id);

    TbItemDesc findItemDescById(long id);

    EasyUIDataGridResult getItemList(Integer page, Integer rows);

    TaotaoResult deleteItems(long[] ids);

    TaotaoResult instockItems(long[] ids);

    TaotaoResult reshelfItems(long[] ids);

    TaotaoResult addItem(TbItem tbItem,String desc,String itemParams);

    TaotaoResult updateItem(TbItem tbItem, String desc);
}
