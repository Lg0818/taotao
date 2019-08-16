package com.taotao.service;

import com.taotao.pojo.PictureResult;

public interface PictureService {
    /**
     * 我们的图片从controller（taotao-manager-web这边接受过来）
     * 然后taotao-manager-web调用service  service调用 dao
     * 但是呢  taotao-manager 在调用service的时候
     * 需要把图片对象传入到service里面，但是 dubbo不允许传入图片对象
     * dubbo可以传入二进制文件 ， 在taotao-manager-web这边吧图片变成
     * 二进制 传入到service 在service里面吧二进制变成图片
     */
    PictureResult uploadFile(byte[] bytes, String name);
}
