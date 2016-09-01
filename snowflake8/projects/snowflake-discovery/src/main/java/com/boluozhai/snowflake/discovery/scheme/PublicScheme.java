package com.boluozhai.snowflake.discovery.scheme;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

public class PublicScheme extends PublicSchemePojo {

	public final static String reg_scheme = "snowflake/1.0";

	interface k {

		// the keys of fields

		String scheme = "scheme";

		String timestamp = "timestamp";
		String timeout = "timeout";
		String broadcast = "broadcast";
		String response = "response";

		String from_name = "from_name";
		String from_host = "from_host";
		String from_port = "from_port";

		String to_name = "to_name";
		String to_host = "to_host";
		String to_port = "to_port";

	}

	public static String toString(PublicScheme obj) {

		obj.scheme = reg_scheme;

		Properties pro = new Properties();

		pro.setProperty(k.scheme, obj.scheme);
		pro.setProperty(k.timestamp, String.valueOf(obj.timestamp));
		pro.setProperty(k.timeout, String.valueOf(obj.timeout));
		pro.setProperty(k.broadcast, String.valueOf(obj.broadcase));
		pro.setProperty(k.response, String.valueOf(obj.response));
		pro.setProperty(k.from_name, obj.from_name);
		pro.setProperty(k.from_host, obj.from_host);
		pro.setProperty(k.from_port, String.valueOf(obj.from_port));
		pro.setProperty(k.to_name, obj.to_name);
		pro.setProperty(k.to_host, obj.to_host);
		pro.setProperty(k.to_port, String.valueOf(obj.to_port));

		StringWriter writer = new StringWriter();
		try {
			pro.store(writer, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer.toString();

	}

	/***
	 * @return if the scheme not match, return null
	 * */

	public static PublicScheme parse(String str) {

		Reader reader = new StringReader(str);
		Properties pro = new Properties();
		try {
			pro.load(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}

		PublicScheme obj = new PublicScheme();

		obj.scheme = pro.getProperty(k.scheme);

		obj.timeout = ValueParser.pInt(pro.getProperty(k.timeout));
		obj.timestamp = ValueParser.pLong(pro.getProperty(k.timestamp));
		obj.broadcase = ValueParser.pBoolean(pro.getProperty(k.broadcast));
		obj.response = ValueParser.pBoolean(pro.getProperty(k.response));

		obj.from_host = pro.getProperty(k.from_host);
		obj.from_port = ValueParser.pInt(pro.getProperty(k.from_port));
		obj.from_name = pro.getProperty(k.from_name);

		obj.to_host = pro.getProperty(k.to_host);
		obj.to_port = ValueParser.pInt(pro.getProperty(k.to_port));
		obj.to_name = pro.getProperty(k.to_name);

		if (reg_scheme.equals(obj.scheme)) {
			return obj;
		} else {
			return null;
		}

	}

	private static class ValueParser {

		public static int pInt(String s) {
			if (s == null) {
				return 0;
			} else {
				return Integer.parseInt(s);
			}
		}

		public static boolean pBoolean(String s) {
			if (s == null) {
				return false;
			} else {
				return Boolean.parseBoolean(s);
			}
		}

		public static long pLong(String s) {
			if (s == null) {
				return 0;
			} else {
				return Long.parseLong(s);
			}
		}
	}

}
