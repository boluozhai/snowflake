package com.boluozhai.snowflake.context.utils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.context.SnowflakeEnvironments;
import com.boluozhai.snowflake.context.SnowflakeParameters;
import com.boluozhai.snowflake.context.SnowflakeProperties;

public class ContextPrinter {

	private final PrintStream o;

	public ContextPrinter(PrintStream out) {
		this.o = out;
	}

	public void print(SnowflakeContext context) {
		o.format("[%s]\n", context);
		o.format("    URI         : %s\n", context.getURI());
		o.format("    parent      : %s\n", context.getParent());
		o.format("    birthday    : %d\n", context.getBirthday());
		o.format("    name        : %s\n", context.getName());
		o.format("    description : %s\n", context.getDescription());

		this.printAttributes(context);
		this.printEnvironments(context);
		this.printProperties(context);
		this.printParameters(context);

	}

	private void printAttributes(SnowflakeContext context) {

		Map<String, String> map = new HashMap<String, String>();
		String[] keys = context.getAttributeNames();
		for (String key : keys) {
			Object obj = context.getAttribute(key);
			String value = String.format("%s#%d", obj.getClass().getName(),
					obj.hashCode());
			map.put(key, value);
		}
		this.printKeyValueMap("  [attribute]", "    ", map);
	}

	private void printEnvironments(SnowflakeContext context) {
		Map<String, String> map = SnowflakeEnvironments.MapGetter.getMap(context);
		this.printKeyValueMap("  [environment]", "    ", map);
	}

	private void printProperties(SnowflakeContext context) {
		Map<String, String> map = SnowflakeProperties.MapGetter.getMap(context);
		this.printKeyValueMap("  [property]", "    ", map);
	}

	private void printParameters(SnowflakeContext context) {
		Map<String, String> map = SnowflakeParameters.MapGetter.getMap(context);
		this.printKeyValueMap("  [parameter]", "    ", map);
	}

	private void printKeyValueMap(String segment, String tab,
			Map<String, String> map) {

		o.println(segment);

		final String ref = "'";
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			String value = map.get(key);
			o.format("%s%20s = %s\n", tab, ref + key + ref, value);
		}

	}

	public static void print(SnowflakeContext context, PrintStream out) {
		ContextPrinter cp = new ContextPrinter(out);
		cp.print(context);
	}

}
