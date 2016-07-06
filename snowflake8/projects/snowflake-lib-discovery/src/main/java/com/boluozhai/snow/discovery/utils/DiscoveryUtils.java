package com.boluozhai.snow.discovery.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boluozhai.snow.discovery.DiscoveryPacket;

public class DiscoveryUtils {

	public static Map<String, String> packet2map(DiscoveryPacket pack) {

		Map<String, String> map = new HashMap<String, String>();

		return map;
	}

	public static DiscoveryPacket map2packet(Map<String, String> map) {

		DiscoveryPacket pack = new DiscoveryPacket();
		return pack;
	}

	public static String map2string(Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			String value = map.get(key);
			sb.append(key);
			sb.append("=");
			sb.append(value);
			sb.append("\n");
		}
		return sb.toString();
	}

}
