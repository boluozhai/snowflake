package com.boluozhai.snowflake.test;

import com.boluozhai.snowflake.test.support.DefaultTester;

public interface Tester {

	Testing open(Object target);

	void close(Testing testing);

	public class Factory {
		public static Tester newInstance() {
			return new DefaultTester();
		}
	}

}
