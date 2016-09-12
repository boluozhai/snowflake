package com.boluozhai.snowflake.xgit.site;

import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.xgit.site.pojo.SitePartitionInfo;

public interface SitePartitionManager extends Component {

	SitePartitionInfo get(String id);

	SitePartitionInfo delete(SitePartitionInfo info);

	SitePartitionInfo update(SitePartitionInfo info);

	String[] ids();

}
