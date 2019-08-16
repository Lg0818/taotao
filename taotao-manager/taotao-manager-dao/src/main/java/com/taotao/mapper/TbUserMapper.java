package com.taotao.mapper;


import com.taotao.pojo.TbUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TbUserMapper {
    @Select("SELECT * FROM taotao.tbuser WHERE userName=#{userName}")
    TbUser checkUserName(String param);
    @Select("SELECT * FROM taotao.tbuser WHERE phone=#{phone}")
    TbUser checkPhone(String param);
    @Select("SELECT * FROM taotao.tbuser WHERE email=#{email}")
    TbUser checkEmail(String param);
    @Insert("INSERT INTO taotao.tbuser(userName, passWord, phone, email, created, updated) VALUE (#{userName},#{passWord},#{phone},#{email},#{created},#{updated})")
    void addTbUser(TbUser tbUser);
    @Select("SELECT * FROM taotao.tbuser WHERE userName = #{userName} AND passWord = #{passWord}")
    TbUser findUserByUserNameAndPassWord(@Param("userName") String userName, @Param("passWord") String passWord);
}