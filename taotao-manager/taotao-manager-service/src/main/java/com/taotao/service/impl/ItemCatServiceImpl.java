package com.taotao.service.impl;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.*;
import com.taotao.service.ItemCatService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private TbItemCatMapper tbItemCatMapper;
    @Override
    public List<EasyUITreeNode> getCatList(Long id) {
        List<TbItemCat> tbItemCats = tbItemCatMapper.findTbItemCatByParentId(id);
        List<EasyUITreeNode> result = new ArrayList<EasyUITreeNode>();
        for (TbItemCat itemCat: tbItemCats) {
            EasyUITreeNode node = new EasyUITreeNode();
            //绑定id
            node.setId(itemCat.getId());
            //绑定分类名称
            node.setText(itemCat.getName());
            node.setState(itemCat.getIsParent()?"closed":"open");
            result.add(node);
        }
        return result;
    }

    @Override
    public TaotaoResult deleteItemDesc(long[] itemId) {
            int i = tbItemCatMapper.deleteItemDesc(itemId);

            if (i != 0) {
                return TaotaoResult.ok();
            }
            return null;
        }

    @Override
    public ItemCatResult getItemCatAll(long parentId) {
        ItemCatResult result = new ItemCatResult();
        result.setData(getItemCatList(parentId));
        return result;
    }
    private List getItemCatList(long parentId){
        List<TbItemCat> tbItemCats = tbItemCatMapper.findTbItemCatByParentId(parentId);
        List data = new ArrayList();
        int count = 0;
        //遍历结果集
        for (TbItemCat item:tbItemCats) {
            //创建返回结果集对象  u n i
            ItemCat itemCat = new ItemCat();
            if(item.getIsParent()){
                //设置第一层目录结构的url
                itemCat.setUrl("/products/"+item.getId()+".html");
                if(parentId == 0){
                    //设置第一层目录结构的name
                    itemCat.setName("<a href='/products/"+item.getId()+".html'>"+item.getName()+"</a>");
                }else {
                    //设置二层目录结构的name
                    itemCat.setName(item.getName());
                }
                itemCat.setItem(getItemCatList(item.getId()));
                //一定是最后一层了
                data.add(itemCat);
                count ++;
                if (parentId == 0 && count >= 14) {
                    break;
                }
            }else{
                //永远是最后一层
                data.add("/products/"+item.getId()+".html|"+item.getName());
            }
        }
        return data;
    }
}


    /*@Delete("DELETE FROM tbitemdesc WHERE itemId=#{itemId}")
    int deleteItemDesc(long[] id);*/

