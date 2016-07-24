package com.boluozhai.snowflake.xgit;

import com.boluozhai.snowflake.xgit.support.ObjectIdFactory;

public interface ObjectId extends HashId {

	class Factory {

		public static ObjectId create(String s) {
			return ObjectIdFactory.create(s);
		}

		public static ObjectId create(byte[] b) {
			return ObjectIdFactory.create(b);
		}

	}

}
