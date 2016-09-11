package com.boluozhai.snowflake.xgit.site.wrapper;

import com.boluozhai.snowflake.xgit.site.PartitionRepository;

public class PartitionRepoWrapper extends SiteRepoWrapper implements
		PartitionRepository {

	protected final PartitionRepository inner_partition;

	public PartitionRepoWrapper(PartitionRepository in) {
		super(in);
		this.inner_partition = in;
	}

}
