package com.boluozhai.snowflake.xgit.http.client;

import java.net.URI;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.httpclient.HttpClient;

public interface GitHttpClient {

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

	class Factory {

		public static GitHttpClient getInstance(SnowflakeContext context) {
			Class<GitHttpClientFactory> type = GitHttpClientFactory.class;
			String name = type.getName();
			GitHttpClientFactory factory = context.getBean(name, type);
			return factory.newClient(context);
		}

	}

	GitHttpRepo connect(URI uri);

	HttpClient getHttpClient();

}
