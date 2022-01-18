package com.bjpowernode.crm.workbench.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description TODO
 * @Date 2022/1/16 23:10
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class Activity implements Serializable {
    private String id; //唯一标识,通过UUIDUtil去随机生成32为字符串
    private String owner; //外键,用户表id
    private String name; //市场活动名称
    private String startDate; //开始时间
    private String endDate; //结束时间
    private String cost; //成本
    private String description; //描述
    private String createTime; //创建时间
    private String createBy; //创建人
    private String editTime; //修改时间
    private String editBy; //修改人
    private String isDelete; //逻辑删除,0代表未删除,1代表已删除
}
