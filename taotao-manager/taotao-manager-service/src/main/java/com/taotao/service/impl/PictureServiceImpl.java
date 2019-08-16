package com.taotao.service.impl;


import com.taotao.pojo.PictureResult;
import com.taotao.service.PictureService;
import com.taotao.utils.FtpUtil;
import com.taotao.utils.IDUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class PictureServiceImpl implements PictureService {
    //从resource.properties配置文件中根据key取value
    @Value("${FTP_ADDRESS}")
    private String FTP_ADDRESS;
    @Value("${FTP_PORT}")
    private Integer FTP_PORT;
    @Value("${FTP_USERNAME}")
    private String FTP_USERNAME;
    @Value("${FTP_PASSWORD}")
    private String FTP_PASSWORD;
    @Value("${FILI_UPLOAD_PATH}")
    private String FILI_UPLOAD_PATH;
    @Value("${IMAGE_BASE_URL}")
    private String IMAGE_BASE_URL;

    @Override
    public PictureResult uploadFile(byte[] bytes, String name) {
        PictureResult result = new PictureResult();
        /**
         * 1.吧二进制转化为图片
         * 2.吧图片上传到ftp
         * 3.吧图片url传回给页面
         * 4.吧成功和不成功的json封装好
         * 5.根据不同的逻辑返回不同的json
         */
        //name是图片的名称 1.jpg    name.substring(name.lastIndexOf(".")) =  随机数.jpg
        String newName = IDUtils.genImageName() + name.substring(name.lastIndexOf("."));
        //当前日期
        String filepath = new DateTime().toString("yyyy/MM/dd");
        //吧一个byte数组加入到 byte字节输入流中去
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        /**
         * 1.上传图片的地址
         * 2.上传图片的端口号
         * 3.账号
         * 4.密码
         * 5.上传的基础路径 /home/ftpuser/www/images
         * 6.上传以后的文件夹
         * 7.修改后的图片的名称
         * 8.
         */
        boolean b = FtpUtil.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD,
                FILI_UPLOAD_PATH, filepath, newName, bis);
        if(b){
            result.setError(0);
            result.setUrl(IMAGE_BASE_URL+"/"+filepath+"/"+newName);
        }else{
            result.setError(1);
            result.setMessage("图片上传失败");
        }
        return result;
    }
}
