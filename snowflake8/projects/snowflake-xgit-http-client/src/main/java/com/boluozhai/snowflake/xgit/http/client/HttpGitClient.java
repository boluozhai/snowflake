package com.boluozhai.snowflake.xgit.http.client;

import com.boluozhai.snowflake.xgit.XGitComponent;

public interface HttpGitClient extends XGitComponent {

	interface Service {

		String git_upload_pack = "git-upload-pack";
		String git_receive_pack = "git-receive-pack";

	}

	interface ContentType {

		String receive_pack_request = "application/x-git-receive-pack-request";
		String receive_pack_result = "application/x-git-receive-pack-result";
		String upload_pack_request = "application/x-git-upload-pack-request";
		String upload_pack_result = "application/x-git-upload-pack-result";
		String upload_pack_advertisement = "application/x-git-upload-pack-advertisement";

	}

}
