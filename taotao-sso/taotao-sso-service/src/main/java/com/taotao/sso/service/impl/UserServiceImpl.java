package com.taotao.sso.service.impl;

import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import com.taotao.utils.JsonUtils;
import com.taotao.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.util.Date;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private RedisUtil RedisUtil;
    @Value("${USER_INFO}")
    private String USER_INFO;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;
    @Override
    public TaotaoResult checkData(String param, int type) {
        TbUser tbUser = null;
        if(type == 1){
            //校验用户名
            tbUser  = tbUserMapper.checkUserName(param);
            if(tbUser!=null){
                return TaotaoResult.ok(false);
            }
        }else if(type == 2){
            //校验手机号
            tbUser  = tbUserMapper.checkPhone(param);
            if(tbUser!=null){
                return TaotaoResult.ok(false);
            }
        }else if(type == 3){
            //校验邮箱
            tbUser  = tbUserMapper.checkEmail(param);
            if(tbUser!=null){
                return TaotaoResult.ok(false);
            }
        }else{
            //数据不合法
            return TaotaoResult.build(400,"数据不合法");
        }
        //数据可用
        return TaotaoResult.ok(true);
    }

    @Override
    public TaotaoResult createUser(TbUser tbUser) {
        /**
         * 凡是后台 都要做数据校验的
         *  页面要做数据校验
         *  后台也要做数据校验
         */
        TaotaoResult result = null;
        if(StringUtils.isBlank(tbUser.getUserName())){
            return TaotaoResult.build(400,"用户名不能为空");
        }
        if(StringUtils.isBlank(tbUser.getPassWord())){
            return TaotaoResult.build(400,"密码不能为空");
        }
        if(StringUtils.isBlank(tbUser.getPhone())){
            return TaotaoResult.build(400,"手机号不能为空");
        }
        if(StringUtils.isBlank(tbUser.getEmail())){
            return TaotaoResult.build(400,"邮箱不能为空");
        }
        //上面的代码是校验数据是否为空 ，下面的代码要校验数据是否存在
        result = checkData(tbUser.getUserName(), 1);
        if (!(boolean) result.getData()) {
            return TaotaoResult.build(400,"用户名重复");
        }
        result = checkData(tbUser.getPhone(), 2);
        if (!(boolean) result.getData()) {
            //注意这里不止要加入 验证是否重复 还要加入是否合法的验证
            return TaotaoResult.build(400,"手机号重复");
        }
        result = checkData(tbUser.getEmail(), 3);
        if (!(boolean) result.getData()) {
            return TaotaoResult.build(400,"邮箱重复");
        }
        //代码走到这里 代表 数据校验完毕 可以存入数据库了，补充没有的数据，id自增长不用管，时间要我们自己补充，密码要加密
        Date date = new Date();
        tbUser.setCreated(date);
        tbUser.setUpdated(date);
        //MD5加密
        tbUser.setPassWord(DigestUtils.md5DigestAsHex(tbUser.getPassWord().getBytes()));

        tbUserMapper.addTbUser(tbUser);
        //数据不可用
        return TaotaoResult.ok();


    }

    @Override
    public TaotaoResult login(String userName, String passWord) {
        if(StringUtils.isBlank(userName)){
            return TaotaoResult.build(400,"用户名不能为空");
        }
        if(StringUtils.isBlank(passWord)){
            return TaotaoResult.build(400,"密码不能为空");
        }
        TbUser tbUser = tbUserMapper.findUserByUserNameAndPassWord(userName,DigestUtils.md5DigestAsHex(passWord.getBytes()));
        if(tbUser==null){
            return TaotaoResult.build(400,"账号密码有误，请检查账号密码");
        }
        //走到这里 代表登录成功 但是要有一个token
        String token = UUID.randomUUID().toString().replace("-","");
        //缓存 不能缓存密码
        tbUser.setPassWord(null);
        //吧用户信息缓存到了redis中
        RedisUtil.set(USER_INFO + ":" + token, JsonUtils.objectToJson(tbUser));
        RedisUtil.expire(USER_INFO + ":" + token, SESSION_EXPIRE);
        return TaotaoResult.ok(token);
    }

    @Override
    public TaotaoResult getUserByToken(String token) {
        String json = RedisUtil.get(USER_INFO + ":" + token);
        if(StringUtils.isBlank(json)){
            // 3、如果查询不到数据。返回用户已经过期。
            return TaotaoResult.build(400, "用户登录已经过期，请重新登录。");
        }
        RedisUtil.expire(USER_INFO + ":" + token, SESSION_EXPIRE);
        TbUser tbUser = JsonUtils.jsonToPojo(json,TbUser.class);

        return TaotaoResult.ok(tbUser);
    }

    @Override
    public TaotaoResult logout(String token) {
        Long del = RedisUtil.del(USER_INFO + ":" + token);
        return TaotaoResult.ok(del);
    }
}
