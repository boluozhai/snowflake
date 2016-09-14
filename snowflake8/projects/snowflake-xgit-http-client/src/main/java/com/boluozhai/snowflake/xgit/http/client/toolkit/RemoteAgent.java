package com.boluozhai.snowflake.xgit.http.client.toolkit;

public interface RemoteAgent {

	String getURL();

	GitUploadPackRequest git_upload_pack();

	GitReceivePackRequest git_receive_pack();

}
