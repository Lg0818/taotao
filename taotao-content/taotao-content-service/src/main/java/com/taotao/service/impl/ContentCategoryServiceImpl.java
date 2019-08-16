package com.taotao.service.impl;

import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.EasyUITreeNode;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContentCategory;
import com.taotao.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService{
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;


    @Override
    public List<EasyUITreeNode> getContentCategoryList(long parentId) {
        //根据分类id查询分类
        List<TbContentCategory> tbContentCategorys = tbContentCategoryMapper.findTbContentCategoryById(parentId);
        //创建返回结果集
        List<EasyUITreeNode> result = new ArrayList<EasyUITreeNode>();
        //遍历并且填充返回结果集
        for (TbContentCategory tbContentCategory : tbContentCategorys) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setText(tbContentCategory.getName());
            node.setState(tbContentCategory.getIsParent()?"closed":"open");

            result.add(node);
        }
        return result;

    }

    @Override
    public TaotaoResult addContentCategory(long parentId, String name) {
        System.out.println("111111111111111111111111111");
        TbContentCategory contentCategory = new TbContentCategory();
        Date date = new Date();
        // 默认没有父亲的
        contentCategory.setIsParent(false);
        //需要添加的节点名称
        contentCategory.setName(name);
        //关联父节点
        contentCategory.setParentId(parentId);
        //排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
        contentCategory.setSortOrder(1);
        //状态。可选值:1(正常),2(删除)
        contentCategory.setStatus(1);
        contentCategory.setCreated(date);
        contentCategory.setUpdated(date);
        //应该添加分类了
        System.out.println(contentCategory);
        tbContentCategoryMapper.addContentCategory(contentCategory);

        //是否需要判断？是否为父节点，不管是否 ，都需要修改isparent



        //根据parentId查询 分类对象 判断这个分类对象的isParent是否为false 如果为false 改为true
        TbContentCategory category = tbContentCategoryMapper.findContentCategoryById(parentId);
        System.out.println(category);
        //代表他本身不是父类节点
        if(!category.getIsParent()){
            category.setIsParent(true);
            tbContentCategoryMapper.updateContentCategory(category);
        }
        return TaotaoResult.ok(contentCategory);

    }

    @Override
    public TaotaoResult updateContentCategory(long id, String name) {
        System.out.println(name);
        TbContentCategory category = new TbContentCategory();
        category.setId(id);
        category.setName(name);
        int i = tbContentCategoryMapper.updateContentCategoryName(category);
        System.out.println(i);
        return TaotaoResult.ok();
    }
}
