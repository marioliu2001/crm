package com.bjpowernode.crm.workbench.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ContactsActivityRelation {

	private String id;
	private String contactsId;
	private String activityId;

}
