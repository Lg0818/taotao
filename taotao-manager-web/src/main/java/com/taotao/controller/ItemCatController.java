package com.taotao.controller;


import com.taotao.pojo.EasyUITreeNode;

import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping("/item/cat/list")
    @ResponseBody
    //接受参数 名称为id 默认值为 0 赋值给parentId
    public List<EasyUITreeNode> getItemCatList(@RequestParam(value="id", defaultValue="0")Long parentId){
        System.out.println(parentId);
        List<EasyUITreeNode> result = itemCatService.getCatList(parentId);
        return result;
    }
}
