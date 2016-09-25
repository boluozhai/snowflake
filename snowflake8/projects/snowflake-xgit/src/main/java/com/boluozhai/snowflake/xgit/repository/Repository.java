package com.boluozhai.snowflake.xgit.repository;

import java.net.URI;

import com.boluozhai.snowflake.xgit.XGitComponent;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.workspace.Workspace;

public interface Repository extends XGitComponent {

	XGitContext context();

	URI location();

	Workspace getWorking();

}
