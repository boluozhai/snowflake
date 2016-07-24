package com.boluozhai.snowflake.xgit;

import com.boluozhai.snowflake.xgit.support.ObjectIdFactory;

public interface HashId {

	class Factory {

		public static HashId create(String s) {
			return ObjectIdFactory.create(s);
		}

		public static HashId create(byte[] b) {
			return ObjectIdFactory.create(b);
		}

	}

	byte[] toByteArray();

}
