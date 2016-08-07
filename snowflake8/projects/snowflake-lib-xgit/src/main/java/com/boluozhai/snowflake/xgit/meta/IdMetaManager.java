package com.boluozhai.snowflake.xgit.meta;

import com.boluozhai.snowflake.xgit.HashId;

public interface IdMetaManager {

	IdMeta getMeta(Class<?> type, HashId id);

}
