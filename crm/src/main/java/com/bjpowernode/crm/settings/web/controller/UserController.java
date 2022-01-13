package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;

import com.bjpowernode.crm.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Date 2022/1/10 17:31
 * @Version 1.0
 */
//lombok提供的日志输出的注解
    //自带log属性 可以通过log属性输出日志信息
@Slf4j
@Controller
@RequestMapping("/settings/user")
public class UserController {

    @Autowired
    private UserService userService;
    /**
     * 跳转到登陆页面
     * @return
     */
    @RequestMapping("/toLogin.do")
    public String toLogin(){
        //拼接试图解析器的前缀+返回值+后缀名称，找到响应的jsp
        // 前缀 = /WEB-INF/jsp
        //返回值 = /login
        //后缀 = .jsp
        return "/login";
    }

    @RequestMapping("/login.do")
    @ResponseBody
    public Map<String,Object> login(@RequestParam(value = "loginAct",required = true)  String loginAct,
                                    @RequestParam(value = "loginPwd") String loginPwd,
                                    HttpSession session,
                                    HttpServletRequest request){

//        &&&&&&&&&&&&&&
//        lon.info()

        //将密码进行加密
        String md5Pwd = MD5Util.getMD5(loginPwd);

        //根据用户名和密码去查询数据库
        //User user = userService.findUserByLoginActAndLoginPwd(loginAct, md5Pwd);
        //
        //封装Map集合用于返回数据
//        Map<String,Object> resultMap = new HashMap<>();
//
//        if(user == null) {
//            //用户名或密码错误
//            resultMap.put("code", 1);
//            resultMap.put("msg","登陆失败,用户名或密码错误");
//            resultMap.put("data",null);
//            return resultMap;
//        }
//

        //获取ip地址
        String ip = request.getRemoteAddr();
        log.info("本机IP地址={}",ip);


        //校验用户名和密码，过期时间 ，锁定状态，IP是否受限
        Map<String,Object> resultMap = userService.findUserByLoginActAndLoginPwdCondition(loginAct, md5Pwd,ip);

        User user = (User) resultMap.get("data");

        if(user == null){
            //在service层封装返回的结果集合
            //1.用户名或密码错误
            //2.被锁定
            //3.用户过期
            //4.IP受限
            //5.登陆成功
            return resultMap;
        }

        //登陆成功将用户对象存入到Session中
        //存入到Session中 ，后续用于权限校验操作
        session.setAttribute("user",user);

//        resultMap.put("code",0);
//        resultMap.put("msg","登陆成功...");
//        resultMap.put("data",null);

        return resultMap;
  }
}
