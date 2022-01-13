package com.bjpowernode.crm.settings.service.Impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.mysql.jdbc.StringUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Date 2022/1/11 18:50
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findUserByLoginActAndLoginPwd(String loginAct, String loginPwd) {

        //登陆操作
        User user = userDao.findUserByLoginActAndLoginPwd(loginAct, loginPwd);
        //校验用户

        return user;
    }

    @Override
    public Map<String, Object> findUserByLoginActAndLoginPwdCondition(String loginAct, String md5Pwd,String ip) {
        User user = userDao.findUserByLoginActAndLoginPwd(loginAct, md5Pwd);
        Map<String,Object> resultMap = new HashMap<>();
        if (user == null){
            //用户名或密码错误
           resultMap.put("code",1);
           resultMap.put("msg","用户名或密码错误");
           resultMap.put("data",null);
           return resultMap;
        }
        //过期时间
        String expireTime = user.getExpireTime();

        //获取当前时间
        String now = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());

        //用户过期校验
        //compareTo比较日期，如果返回的是1表示未过期（过期时间大于现在时间）
        //如果返回的是1表示未过期（过期时间大于现在时间）
        //如果返回的是0表示未过期（过期时间等于现在时间）
        //如果返回的是-1表示已经过期（现在时间大于过期时间）
        //如果过期时间为空，代表已过期
        if (!StringUtils.isNullOrEmpty(expireTime)){
            if(expireTime.compareTo(now)<=0){
                //代表已过期
                resultMap.put("code",1);
                resultMap.put("msg", "当前用户已过期");
                resultMap.put("data", null);
                return resultMap;
            }
        }

        //校验用户是否被锁定
        String lockState = user.getLockState();
        if (!StringUtils.isNullOrEmpty(lockState)){
            if ("1".equals(lockState)){
                resultMap.put("code",1);
                resultMap.put("msg", "用户已锁定");
                resultMap.put("data", null);
                return resultMap;
            }
        }

        //校验用户ip地址是否受限
        String allowIps = user.getAllowIps();
        if(!StringUtils.isNullOrEmpty(allowIps)){
            if (!allowIps.contains(ip)){
                resultMap.put("code",1);
                resultMap.put("msg", "ip地址受限");
                resultMap.put("data", null);
                return resultMap;
            }
        }

        //登陆成功
        resultMap.put("code",0);
        resultMap.put("msg", "登陆成功");
        resultMap.put("data", null);
        return resultMap;
    }

}
