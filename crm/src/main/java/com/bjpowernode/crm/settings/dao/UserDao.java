package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserDao {
    //mybatis提供的注解@Select @Insert @Update @Delete
//    @Select("select * from tbl_user where loginAct = #{loginAct} and loginPwd = #{loginPwd}")
//    User findUserByLoginActAndLoginPwd(@Param("loginAct")String loginAct,
//                                       @Param("loginPwd")String loginPwd);


    //@Select("select * from tbl_user where loginAct = #{param1} and loginPwd = #{param2}")
    User findUserByLoginActAndLoginPwd(String loginAct,
                                       String loginPwd);
}
