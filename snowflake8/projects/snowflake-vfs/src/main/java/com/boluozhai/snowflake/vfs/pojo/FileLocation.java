package com.boluozhai.snowflake.vfs.pojo;

import java.net.URI;

public class FileLocation {

	private String value;

	public FileLocation() {
	}

	public FileLocation(String v) {
		this.value = v;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static FileLocation convert(URI uri) {
		String scheme = uri.getScheme();
		if (scheme.equals("file")) {
			FileLocation loc = new FileLocation();
			loc.value = "file://" + uri.getPath();
			return loc;
		} else {
			throw new RuntimeException("need a scheme of [file:].");
		}
	}

	public static URI convert(FileLocation loc) {
		String val = loc.value;
		if (val.startsWith("file:")) {
			return URI.create(val);
		} else {
			throw new RuntimeException("need a scheme of [file:].");
		}
	}

}
