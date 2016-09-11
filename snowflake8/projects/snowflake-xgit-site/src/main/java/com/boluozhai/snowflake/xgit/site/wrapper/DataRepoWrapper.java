package com.boluozhai.snowflake.xgit.site.wrapper;

import com.boluozhai.snowflake.xgit.site.DataRepository;

public class DataRepoWrapper extends SiteRepoWrapper implements DataRepository {

	protected final DataRepository inner_data;

	public DataRepoWrapper(DataRepository in) {
		super(in);
		this.inner_data = in;
	}

}
