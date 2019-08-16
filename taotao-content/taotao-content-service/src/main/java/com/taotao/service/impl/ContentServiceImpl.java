package com.taotao.service.impl;

import com.alibaba.dubbo.common.json.JSONObject;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;
import com.taotao.utils.JsonUtils;
import com.taotao.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper tbContentMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Value("CONTENT_KEY")
    private String CONTENT_KEY;
    @Override
    public List<TbContent> findContentAll(long categoryId) {
        //查询前 先判断缓存中是否有需要的内容
        String value = redisUtil.get(CONTENT_KEY);
        if (StringUtils.isNotBlank(value)){
            System.out.println("缓存中获取");
            System.out.println(value);
            List<TbContent> result = JsonUtils.jsonToList(value, TbContent.class);
            System.out.println(result);
            return result;
        }
        List<TbContent> contents = tbContentMapper.findTbContentAll(categoryId);

        //这一步，将结果集设置进缓存
        redisUtil.set("CONTENT_KEY",JsonUtils.objectToJson(contents));
        System.out.println("数据库中获取");
        return contents;
    }

    @Override
    public TaotaoResult addContent(TbContent tbContent) {
        Date date = new Date();
        tbContent.setCreated(date);
        tbContent.setUpdated(date);

        tbContentMapper.addContent(tbContent);

        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteContentById(long[] ids) {
        tbContentMapper.deleteContent(ids);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult updateContentById(TbContent content) {
        Date date = new Date();
        content.setCreated(date);
        content.setUpdated(date);
        tbContentMapper.updateContentById(content);
        return TaotaoResult.ok();
    }
}
