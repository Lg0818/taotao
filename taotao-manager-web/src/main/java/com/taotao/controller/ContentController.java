package com.taotao.controller;

import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;

    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyUIDataGridResult findAllContentCategoryById(long categoryId){
        System.out.println(categoryId);
        List<TbContent> contents = contentService.findContentAll(categoryId);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(contents.size());
        result.setRows(contents);
        return result;
    }
    @RequestMapping("/content/save")
    @ResponseBody
    public TaotaoResult addContent(TbContent tbContent){
        System.out.println(tbContent.getCategoryId());
        TaotaoResult result = contentService.addContent(tbContent);
        return result;
    }
    @RequestMapping("/content/delete")
    @ResponseBody
    public TaotaoResult deleteContent(long[] ids){
        TaotaoResult result = contentService.deleteContentById(ids);
        return result;
    }
    ///rest/content/edit
    @RequestMapping("rest/content/edit")
    @ResponseBody
    public TaotaoResult updateContent(TbContent content){
        System.out.println(content);
        TaotaoResult result = contentService.updateContentById(content);
        return result;
    }
}
