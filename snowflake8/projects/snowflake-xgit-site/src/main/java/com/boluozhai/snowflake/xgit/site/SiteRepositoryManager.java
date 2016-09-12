package com.boluozhai.snowflake.xgit.site;

import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.xgit.site.pojo.SiteRepoInfo;

public interface SiteRepositoryManager extends Component {

	SiteRepoInfo get(String id);

	SiteRepoInfo insert(SiteRepoInfo info);

	SiteRepoInfo delete(SiteRepoInfo info);

	SiteRepoInfo update(SiteRepoInfo info);

	String[] ids();

}
