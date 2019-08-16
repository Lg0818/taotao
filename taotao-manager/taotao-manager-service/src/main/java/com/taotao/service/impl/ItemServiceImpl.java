package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.*;
import com.taotao.service.ItemService;
import com.taotao.utils.IDUtils;
import com.taotao.utils.JsonUtils;
import com.taotao.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private  TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;
    /*@Autowired
    private TbItemCatMapper tbItemCatMapper;
    @Autowired
    private SolrServer solrServer;
    @Autowired
    private SearchItemMapper searchItemMapper;*/
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination topicDestination;
    @Value("ITEM_INFO_PRE")
    private  String ITEM_INFO_PRE;
    @Value("${ITEM_INFO_EXPIRE}")
    private  Integer ITEM_INFO_EXPIRE;


    @Override
    public TbItem findItemById(long id) {
        try {
            //查询缓存
            String json = redisUtil.get(ITEM_INFO_PRE + ":" + id + ":BASE");
            if (StringUtils.isNotBlank(json)) {
                //把json转换为java对象
                TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
                redisUtil.expire(ITEM_INFO_PRE + ":" + id + ":BASE", ITEM_INFO_EXPIRE);
                return item;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItem item = tbItemMapper.findItemById(id);
        //这一步，将结果集设置进缓存
        redisUtil.set(ITEM_INFO_PRE + ":" + id + ":BASE",JsonUtils.objectToJson(item));
        redisUtil.expire(ITEM_INFO_PRE + ":" + id + ":BASE", ITEM_INFO_EXPIRE);
        System.out.println("数据库中获取");
        return item;
    }

    @Override
    public TbItemDesc findItemDescById(long id) {
        try {
            //查询缓存
            String json = redisUtil.get(ITEM_INFO_PRE + ":" + id + ":DESC");
            if (StringUtils.isNotBlank(json)) {
                //把json转换为java对象
                TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                redisUtil.expire(ITEM_INFO_PRE + ":" + id + ":DESC", ITEM_INFO_EXPIRE);
                return itemDesc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItemDesc itemDesc = tbItemDescMapper.findItemDescById(id);
        //这一步，将结果集设置进缓存
        redisUtil.set(ITEM_INFO_PRE + ":" + id + ":DESC",JsonUtils.objectToJson(itemDesc));
        redisUtil.expire(ITEM_INFO_PRE + ":" + id + ":DESC", ITEM_INFO_EXPIRE);
        return itemDesc;
    }


    @Override
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        //初始化分页设置 他的底层原理是 自动拼接limit语句
        System.out.println("impl");
        PageHelper.startPage(page, rows);
        //查询所有商品信息
        List<TbItem> items = tbItemMapper.findItem();
        //取分页信息
        PageInfo<TbItem> pageInfo = new PageInfo<>(items);
        //ctrl+alt+L  整理代码
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        //使用插件得到总记录条数
        result.setTotal(pageInfo.getTotal());
        //被分页插件处理以后得到的结果集了集合对象
        result.setRows(items);
        return result;
    }

    @Override
    public TaotaoResult deleteItems(long[] ids) {
        int i = tbItemMapper.deleteItems(ids);

        if (i != 0) {
            return TaotaoResult.ok();
        }
        return null;
    }



    @Override
    public TaotaoResult instockItems(long[] ids) {
        int i = tbItemMapper.instockItems(ids);

        if (i != 0) {
            return TaotaoResult.ok();
        }
        return null;
    }

    @Override
    public TaotaoResult reshelfItems(long[] ids) {
        int i = tbItemMapper.reshelfItems(ids);

        if (i != 0) {
            return TaotaoResult.ok();
        }
        return null;
    }

    @Override
    public TaotaoResult addItem(TbItem tbItem, String desc,String itemParams) {
        //绑定数据、调用dao添加数据库
        final Long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        tbItem.setStatus((byte) 1);
        Date date = new Date();
        tbItem.setCreated(date);
        tbItem.setUpdated(date);
        //所有数据准备完毕才能添加商品信息
        int itemCount = tbItemMapper.addItems(tbItem);
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(itemId);
        itemDesc.setCreated(date);
        itemDesc.setUpdated(date);
        //准备了描述信息的所有数据才能添加描述信息
        int itemDescCount = tbItemDescMapper.addItemDesc(itemDesc);
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setItemId(itemId);
        tbItemParamItem.setParamData(itemParams);
        tbItemParamItem.setCreated(date);
        tbItemParamItem.setUpdated(date);
        //存入规格参数
        int itemParamConut = tbItemParamItemMapper.addTbitemParamItem(tbItemParamItem);

        if(itemCount!=0&&itemDescCount!=0&&itemParamConut!=0){
            //商品对象的json
            jmsTemplate.send(topicDestination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    TextMessage message = session.createTextMessage(itemId+"");
                    return message;
                }
            });
            return TaotaoResult.ok();
        }
        return TaotaoResult.build(500,"添加商品有误，请重新输入");
    }
    //tbItem对象值绑定有错
    @Override
    public TaotaoResult updateItem(TbItem tbItem, String desc) {
        //绑定数据、调用dao添加数据
        System.out.println(tbItem);
        System.out.println(desc);
        Long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        tbItem.setStatus((byte) 1);
        Date date = new Date();
        tbItem.setCreated(date);
        tbItem.setUpdated(date);
        //所有数据准备完毕才能添加商品信息
        System.out.println(tbItem);
        int itemCount = tbItemMapper.updateItems(tbItem);
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(date);
        itemDesc.setUpdated(date);
        //准备了描述信息的所有数据才能添加描述信息
        int itemDescCount = tbItemDescMapper.updateItemDesc(itemDesc);
        if(itemCount!=0&&itemDescCount!=0){
            return TaotaoResult.ok();
        }
        return TaotaoResult.build(500,"添加商品有误，请重新输入");
    }

}
