package com.bjpowernode.crm.settings.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @Date 2022/1/11 18:52
 * @Version 1.0
 */
//@Data注解可以帮助围我们申城实体类的get/set toString方法
@Data
public class User implements Serializable {
    private String id;
    private String loginAct;
    private String name;
    private String loginPwd;
    private String email;
    private String lockState;//锁定状态，0代表用户未锁定，可以登陆
    private String expireTime;//过期时间 19位字符串2022-1-10 22:07:08
    private String deptno;
    private String allowIps;//允许访问IP，逗号分割
    private String createTime;
    private String createBy;
    private String editTime;
    private String editBy;

}
