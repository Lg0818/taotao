package com.taotao.item.listener;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;
import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemParamService;
import com.taotao.service.ItemService;
import freemarker.core.ParseException;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyMessageListener implements MessageListener{
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemParamService itemParamService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    private BufferedWriter writer;
    @Override
    public void onMessage(Message message) {

        TextMessage textMessage = (TextMessage) message;
        try {
            String itemId = textMessage.getText();
            TbItem tbItem = itemService.findItemById(Long.valueOf(itemId));
            TbItemDesc itemDesc = itemService.findItemDescById(Long.valueOf(itemId));
            String itemParam = itemParamService.getItemParamByItemId(Long.valueOf(itemId));
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            freemarker.template.Template template = configuration.getTemplate("item.ftl");
            Map map = new HashMap();
            map.put("item", new Item(tbItem));
            map.put("itemDesc", itemDesc);
            map.put("itemParam", itemParam);
            writer  = new BufferedWriter(new FileWriter("D:\\"+itemId+".html"));
            template.process(map,writer);
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (MalformedTemplateNameException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (TemplateNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            if(writer!=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
