package com.taotao.item.controller;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller

public class ItemController {
    @Autowired
    private ItemService itemService;
    //展示商品基本信息
    @RequestMapping("/item/{itemId}")
    public String showItem(@PathVariable Long itemId, Model model){
        TbItem tbItem = itemService.findItemById(itemId);
        //因为页面需要一个images的东西 而 只有Item对象才有
        Item item = new Item(tbItem);
        model.addAttribute("item",item);
        return "item";
    }
    //展示商品描述信息
    @RequestMapping("/item/desc/{itemId}")
    @ResponseBody
    public String showItemDesc(@PathVariable Long itemId, Model model){
        TbItemDesc tbItemDesc = itemService.findItemDescById(itemId);
        return tbItemDesc.getItemDesc();
    }

}
