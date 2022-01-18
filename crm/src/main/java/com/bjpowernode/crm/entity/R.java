package com.bjpowernode.crm.entity;

import org.apache.poi.ss.formula.functions.T;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO 公共返回值结果集
 * @Date 2022/1/17 14:08
 * @Version 1.0
 */
public class R extends HashMap {

    public static R ok(){
        R r = new R();
        r.put("code",0);
        r.put("msg","操作成功...");
        return r;
    }

    public static R ok(Integer code){
        R r = new R();
        r.put("code",code);
        r.put("msg","操作成功...");
        return r;
    }

    public static R ok(String msg){
        R r = new R();
        r.put("code",0);
        r.put("msg",msg);
        return r;
    }

    public static R ok(Integer code,String msg){
        R r = new R();
        r.put("code",code);
        r.put("msg",msg);
        return r;
    }

    public static <T> R ok(Integer code, String msg, T data){
        R r = new R();
        r.put("code",code);
        r.put("msg",msg);
        r.put("data",data);
        return r;
    }

    public static R ok(Map map){
        R r = new R();
        r.put("code",0);
        r.put("msg","操作成功...");
        r.putAll(map);
        return r;
    }

    public static R err(){
        R r = new R();
        r.put("code",1);
        r.put("msg","操作失败...");
        return r;
    }

    public static R err(Integer code){
        R r = new R();
        r.put("code",code);
        r.put("msg","操作失败...");
        return r;
    }

    public static R err(String msg){
        R r = new R();
        r.put("code",1);
        r.put("msg",msg);
        return r;
    }

    public static R err(Integer code, String msg){
        R r = new R();
        r.put("code",code);
        r.put("msg",msg);
        return r;
    }

}
