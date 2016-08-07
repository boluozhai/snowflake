package com.boluozhai.snowflake.xgit.meta;

import java.net.URI;

public interface UriMetaManager {

	UriMeta getMeta(Class<?> type, URI uri);

}
