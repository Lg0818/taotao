package com.taotao.portal.controller;

import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.ItemCatResult;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.Ad1Node;
import com.taotao.service.ContentService;
import com.taotao.service.ItemCatService;
import com.taotao.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private ContentService contentService;
    @Autowired
    private ItemCatService itemCatService;
    @Value("${AD1_CID}")
    private Long AD1_CID;
    @Value("${AD1_HEIGHT}")
    private Integer AD1_HEIGHT;
    @Value("${AD1_WIDTH}")
    private Integer AD1_WIDTH;
    @Value("${AD1_HEIGHT_B}")
    private Integer AD1_HEIGHT_B;
    @Value("${AD1_WIDTH_B}")
    private Integer AD1_WIDTH_B;


    @RequestMapping("/index")
    public String showIndex(Model model){
        List<TbContent> result = contentService.findContentAll(AD1_CID);

        //返回给页面的json格式的对象
        List<Ad1Node> ad1List = new ArrayList<>();
        //遍历集合 得到对象 组装数据
        for (TbContent tbContent : result) {
            Ad1Node node = new Ad1Node();
            node.setAlt(tbContent.getTitle());
            node.setHeight(AD1_HEIGHT);
            node.setHeightB(AD1_HEIGHT_B);
            node.setWidth(AD1_WIDTH);
            node.setWidthB(AD1_WIDTH_B);
            node.setHref(tbContent.getUrl());
            node.setSrc(tbContent.getPic());
            node.setSrcB(tbContent.getPic2());
            //添加到列表
            ad1List.add(node);
            System.out.println(tbContent);
        }
        //吧集合对象变成json格式 就可以搞定了

        model.addAttribute("ad1", JsonUtils.objectToJson(ad1List));


        return "index";
    }

    @RequestMapping("/item/cat/itemcat/all")
    @ResponseBody
    public String queryAll(String callback) {
        System.out.println("111111111111111");
        //因为这里跨域了
        ItemCatResult result = itemCatService.getItemCatAll(0L);
        String json = callback + "(" + JsonUtils.objectToJson(result)+");" ;
        System.out.println(json);
        return json;

    }

}
