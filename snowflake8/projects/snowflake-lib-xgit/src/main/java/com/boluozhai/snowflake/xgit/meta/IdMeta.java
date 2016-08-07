package com.boluozhai.snowflake.xgit.meta;

import com.boluozhai.snowflake.xgit.HashId;

public interface IdMeta extends BaseMeta {

	HashId getId();

	IdMetaManager getManager();

}
