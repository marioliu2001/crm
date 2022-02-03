package com.bjpowernode.crm.workbench.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClueActivityRelation {
	
	private String id;
	private String clueId;
	private String activityId;

}
