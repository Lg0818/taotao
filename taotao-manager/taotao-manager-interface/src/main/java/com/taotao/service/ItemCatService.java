package com.taotao.service;



import com.taotao.pojo.EasyUITreeNode;
import com.taotao.pojo.ItemCatResult;
import com.taotao.pojo.TaotaoResult;

import java.util.List;

public interface ItemCatService {
    List<EasyUITreeNode> getCatList(Long id);

    TaotaoResult deleteItemDesc(long[] id);

    ItemCatResult getItemCatAll(long l);
}
