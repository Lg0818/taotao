package com.taotao.service;

import com.taotao.pojo.EasyUITreeNode;
import com.taotao.pojo.TaotaoResult;

import java.util.List;

public interface ContentCategoryService {
    //后台内容管理分类展示
    public List<EasyUITreeNode> getContentCategoryList(long parentId);
    //添加内容分类
    TaotaoResult addContentCategory(long parentId, String name);
    //修改分类名称
    TaotaoResult updateContentCategory(long id, String name);
}
