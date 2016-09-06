package com.boluozhai.snowflake.rest.api.h2o;

import com.boluozhai.snowflake.rest.api.RestDoc;
import com.boluozhai.snowflake.rest.element.file.NodeList;

public class RepoFileModel extends RestDoc {

	private NodeList vfile;

	public NodeList getVfile() {
		return vfile;
	}

	public void setVfile(NodeList vfile) {
		this.vfile = vfile;
	}

}
