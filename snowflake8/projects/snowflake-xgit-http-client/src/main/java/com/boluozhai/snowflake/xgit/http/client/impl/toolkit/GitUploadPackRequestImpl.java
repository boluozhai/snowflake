package com.boluozhai.snowflake.xgit.http.client.impl.toolkit;

import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;
import com.boluozhai.snowflake.xgit.http.client.GitHttpService;
import com.boluozhai.snowflake.xgit.http.client.toolkit.GitUploadPackAdvertisement;
import com.boluozhai.snowflake.xgit.http.client.toolkit.GitUploadPackRequest;
import com.boluozhai.snowflake.xgit.http.client.toolkit.GitUploadPackResult;

final class GitUploadPackRequestImpl implements GitUploadPackRequest {

	private final GitHttpService _serv;

	public GitUploadPackRequestImpl(GitHttpRepo repo) {
		this._serv = repo.getService("info/refs", "git-upload-pack");
	}

	@Override
	public GitUploadPackAdvertisement requestAdvertisement() {
		return new GitUploadPackAdvertisementImpl(_serv);
	}

	@Override
	public GitUploadPackResult requestResult() {
		return new GitUploadPackResultImpl(_serv);
	}

}
