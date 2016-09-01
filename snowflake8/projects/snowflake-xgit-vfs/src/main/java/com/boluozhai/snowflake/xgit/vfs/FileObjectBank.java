package com.boluozhai.snowflake.xgit.vfs;

import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponent;

public interface FileObjectBank extends ObjectBank, FileXGitComponent {

	FileObject object(ObjectId id);

}
