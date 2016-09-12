package com.boluozhai.snowflake.xgit.site;

import com.boluozhai.snowflake.mvc.model.Component;
import com.boluozhai.snowflake.xgit.site.pojo.SiteUserInfo;

public interface SiteUserManager extends Component {

	SiteUserInfo get(String id);

	SiteUserInfo insert(SiteUserInfo info);

	SiteUserInfo delete(SiteUserInfo info);

	SiteUserInfo update(SiteUserInfo info);

	String[] ids();

}
