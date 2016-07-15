package com.boluozhai.snowflake.xgit.objects;

import java.security.MessageDigest;

public interface HashAlgorithmProvider {

	String algorithm();

	MessageDigest getMessageDigest();

}
