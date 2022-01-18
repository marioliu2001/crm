package com.bjpowernode.crm.settings.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @Date 2022/1/16 17:12
 * @Version 1.0
 */
@Data
public class DictionaryValue implements Serializable {
    private String id;
    private String value;
    private String text;
    private String orderNo;
    private String typeCode;
}
