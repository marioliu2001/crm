package com.bjpowernode.crm.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description TODO 封装公共的分页返回值结果集
 * @Date 2022/1/17 16:21
 * @Version 1.0
 */
@Data
@Accessors(chain = true)//将当前实体类的get，set，toString...方法默认实现，
// 可以通过链式编程的方式进行封装实体类
public class RPage<T> implements Serializable {

    private int code;
    private String msg;
    private int pageNo;//当前页
    private int pageSize;//每页条数
    private int maxRowPerPage;//每页最多显示的记录数，20
    private int visiblePages;//显示几个卡片
    private long totalPages;//总页数
    private long totalCounts;//总记录数

    private T data;//返回页面的数据，分页的数据
}
