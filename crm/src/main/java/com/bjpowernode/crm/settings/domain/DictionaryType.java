package com.bjpowernode.crm.settings.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @Date 2022/1/15 17:00
 * @Version 1.0
 */
@Data
public class DictionaryType implements Serializable {
    private String code;
    private String name;
    private String description;
}
