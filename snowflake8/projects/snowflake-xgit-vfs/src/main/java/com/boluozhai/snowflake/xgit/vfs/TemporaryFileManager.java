package com.boluozhai.snowflake.xgit.vfs;

import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.vfs.VFileNode;
import com.boluozhai.snowflake.xgit.XGitComponent;

public interface TemporaryFileManager extends VFileNode, XGitComponent {

	VFile newTemporaryFile();

	VFile newTemporaryFile(String prefix, String suffix);

}
