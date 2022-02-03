package com.bjpowernode.crm.workbench.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TranHistory {
	
	private String id;
	private String stage;
	private String money;
	private String expectedDate;
	private String createTime;
	private String createBy;
	private String tranId;

}
