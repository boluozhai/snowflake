package com.boluozhai.snowflake.xgit.http.client;

import java.io.IOException;
import java.net.URI;

import com.boluozhai.snowflake.httpclient.HttpConnection;

public interface GitHttpService {

	interface NAME {

		String git_upload_pack = "git-upload-pack";
		String git_receive_pack = "git-receive-pack";

	}

	GitHttpResource getOwner();

	URI getURI();

	String getServiceName();

	HttpConnection open() throws IOException;

}
