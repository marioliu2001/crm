package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;

import com.bjpowernode.crm.utils.MD5Util;
import com.mysql.jdbc.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.*;
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
     * 跳转到登陆页面 + 自动登陆
     *
     * @return
     */
    @RequestMapping("/toLogin.do")
    public String toLogin(HttpServletRequest request) throws LoginException {

        String loginAct = "";
        String loginPwd = "";

        //获取所有Cookies
        Cookie[] cookies = request.getCookies();
        if (!ObjectUtils.isEmpty(cookies)){
            //遍历所有Cookies定位到LoginAct和loginPwd
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals("loginAct")){
                    loginAct = cookie.getValue();
                    continue;
                }
                if (cookie.getName().equals("loginPwd")){
                    loginPwd = cookie.getValue();
                }

            }
        }

        //拿到ip地址
        String ip = request.getRemoteAddr();

        //设置user
        Map<String, Object> resultMap = userService.findUserByLoginActAndLoginPwdCondition(loginAct, loginPwd, ip);

        User user = (User) resultMap.get("data");

        if (!ObjectUtils.isEmpty(user)){
            //登陆成功，将session中的user更新
            request.getSession().setAttribute("user",user);

            //重定向到首页面
            return "redirect:/workbench/toIndex.do";
        }
        //拼接试图解析器的前缀+返回值+后缀名称，找到响应的jsp
        // 前缀 = /WEB-INF/jsp
        //返回值 = /login
        //后缀 = .jsp
        return "/login";
    }

    /**
     * 登陆 + 十天免登录
     * @return
     */
    @RequestMapping("/login.do")
    @ResponseBody
    public Map<String,Object> login(@RequestParam(value = "loginAct",required = true)  String loginAct,
                                    @RequestParam(value = "loginPwd") String loginPwd,
                                    HttpSession session,
                                    HttpServletRequest request,
                                    String flag,
                                    HttpServletResponse response) throws LoginException {

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

        if (!StringUtils.isNullOrEmpty(flag)){
            //将信息存入到cookie中
            Cookie loginActCookie = new Cookie("loginAct",loginAct);
            Cookie loginPwdCookie = new Cookie("loginPwd",md5Pwd);

            //设置Cookie的有效时间
            loginActCookie.setMaxAge(60*60*24*10);
            loginPwdCookie.setMaxAge(60*60*24*10);

            //设置Cookie的路径
            loginActCookie.setPath("/");
            loginPwdCookie.setPath("/");

            //将cookie通过响应对象，塞给浏览器
            response.addCookie(loginActCookie);
            response.addCookie(loginPwdCookie);
        }

        return resultMap;
  }
}
