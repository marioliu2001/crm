package com.bjpowernode.crm.workbench.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CustomerRemark {

	private String id;
	private String noteContent;
	private String createTime;
	private String createBy;
	private String editTime;
	private String editBy;
	private String editFlag;
	private String customerId;

}
