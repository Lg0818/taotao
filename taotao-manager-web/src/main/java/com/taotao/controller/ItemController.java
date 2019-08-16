package com.taotao.controller;

import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("/rest/item-edit")
    @ResponseBody
    public TbItem findItem(long[] ids){
        System.out.println(ids[0]);
        return  itemService.findItemById(ids[0]);
    }
    //展示列表
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page, Integer rows){
        EasyUIDataGridResult result = itemService.getItemList(page, rows);
        return result;
    }
    //rest/item/delete 删除
    @RequestMapping("/rest/item/delete")
    @ResponseBody
    public TaotaoResult  deleteItemList(long[] ids){
        TaotaoResult result = itemService.deleteItems(ids);
        return  result;
    }
    //更新
    @RequestMapping("/rest/page/item-edit/{ids}")
    public void showUpdate(@PathVariable Integer ids){
        System.out.println(ids);
    }
    //下架
    @RequestMapping("/rest/item/instock")
    @ResponseBody
    public TaotaoResult  instockItemList(long[] ids){
        TaotaoResult result = itemService.instockItems(ids);
        return  result;
    }
    //下架
    @RequestMapping("/rest/item/reshelf")
    @ResponseBody
    public TaotaoResult  reshelfItemList(long[] ids){
        TaotaoResult result = itemService.reshelfItems(ids);
        return  result;
    }
    //添加商品
    @RequestMapping("/item/save")
    @ResponseBody
    public TaotaoResult  addItemList(TbItem tbItem, String desc,String itemParams){
        TaotaoResult result = itemService.addItem(tbItem,desc,itemParams);
        return  result;
    }
    ///rest/item/update
    //修改商品
    @RequestMapping("/rest/item/update")
    @ResponseBody
    public TaotaoResult  updateItemList(TbItem tbItem, String desc){
        TaotaoResult result = itemService.updateItem(tbItem,desc);
        return  result;
    }
}
