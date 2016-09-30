package com.boluozhai.snowflake.xgit.http.client.smart;

import java.util.List;

import com.boluozhai.snowflake.xgit.ObjectId;

public class SmartPktWrapper {

	private final SmartPkt pkt;

	public SmartPktWrapper(SmartPkt pkt) {
		this.pkt = pkt;
	}

	public SmartPkt getPkt() {
		return pkt;
	}

	// command

	public String getCommand() {
		return pkt.getCommand();
	}

	public void setCommand(String cmd) {
		pkt.setCommand(cmd);
	}

	// param

	public void setParam(int index, String value) {
		this.pkt.getParam().set(index, value);
	}

	public String getParam(int index) {
		return this.getParam(index, null, false);
	}

	public String getRequiredParam(int index) {
		return this.getParam(index, null, true);
	}

	public String getParam(int index, String value_default, boolean required) {
		final List<String> list = pkt.getParam();
		String value = null;
		if (index < list.size()) {
			value = list.get(index);
		}
		if (value == null) {
			if (required) {
				String msg = "need command[%d]";
				msg = String.format(msg, index);
				throw new RuntimeException(msg);
			} else {
				value = value_default;
			}
		}
		return value;
	}

	// option

	public void setOption(String name, String value) {
		this.pkt.getOption().put(name, value);
	}

	public String getOption(String name) {
		return this.getOption(name, null, false);
	}

	public String getRequiredOption(String name) {
		return this.getOption(name, null, true);
	}

	public String getOption(String name, String value_default, boolean required) {
		String value = pkt.getOption().get(name);
		if (value == null) {
			if (required) {
				String msg = "need option: " + name;
				throw new RuntimeException(msg);
			} else {
				value = value_default;
			}
		}
		return value;
	}

	// in types

	public ObjectId getIdParam(int index) {
		String str = this.getRequiredParam(index);
		return ObjectId.Factory.create(str);
	}

	public int getIntOption(String name) {
		String str = this.getRequiredOption(name);
		return Integer.parseInt(str);
	}

	public long getLongOption(String name) {
		String str = this.getRequiredOption(name);
		return Long.parseLong(str);
	}

	public boolean getBooleanOption(String name) {
		String str = this.getRequiredOption(name);
		return Boolean.parseBoolean(str);
	}

	public void setParam(int index, ObjectId id) {
		String str = id.toString();
		this.setParam(index, str);
	}

	public void setOption(String name, int value) {
		String str = String.valueOf(value);
		this.setOption(name, str);
	}

	public void setOption(String name, long value) {
		String str = String.valueOf(value);
		this.setOption(name, str);
	}

	public void setOption(String name, boolean value) {
		String str = String.valueOf(value);
		this.setOption(name, str);
	}

	// in fields

}
