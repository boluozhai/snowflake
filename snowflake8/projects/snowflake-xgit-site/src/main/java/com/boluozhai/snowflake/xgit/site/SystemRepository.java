package com.boluozhai.snowflake.xgit.site;

public interface SystemRepository extends SiteRepository {

	SiteUserManager getUserManager();

	SiteRepositoryManager getRepositoryManager();

}
