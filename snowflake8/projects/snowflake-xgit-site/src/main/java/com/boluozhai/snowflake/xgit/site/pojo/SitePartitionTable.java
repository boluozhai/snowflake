package com.boluozhai.snowflake.xgit.site.pojo;

import java.util.Map;

public class SitePartitionTable extends SiteDoc {

	private Map<String, SitePartitionInfo> partitions;

	public Map<String, SitePartitionInfo> getPartitions() {
		return partitions;
	}

	public void setPartitions(Map<String, SitePartitionInfo> partitions) {
		this.partitions = partitions;
	}

}
