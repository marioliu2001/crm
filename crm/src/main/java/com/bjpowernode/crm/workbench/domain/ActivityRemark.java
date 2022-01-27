package com.bjpowernode.crm.workbench.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description TODO
 * @Date 2022/1/22 21:12
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class ActivityRemark implements Serializable {

    private String id;
    private String noteContent; //备注信息
    private String createTime;
    private String createBy;
    private String editTime;
    private String editBy;
    private String editFlag; //修改标记,0代表未修改,1代表已修改
    private String activityId; //外键
}
