package com.boluozhai.snowflake.xgit.vfs;

import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.HashId;

public interface HashPathMapper {

	VFile getHashPath(VFile base, HashId hash);

	VFile getHashPath(VFile base, HashId hash, String suffix);

}
