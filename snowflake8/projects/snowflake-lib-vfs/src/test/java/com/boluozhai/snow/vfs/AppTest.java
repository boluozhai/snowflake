package com.boluozhai.snow.vfs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AppTest {

	@Test
	public void test_env_var() {

		Map<String, String> env = System.getenv();
		List<String> keys = new ArrayList<String>(env.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			String value = env.get(key);
			System.out.format("%s = %s\n", key, value);
		}

	}

}