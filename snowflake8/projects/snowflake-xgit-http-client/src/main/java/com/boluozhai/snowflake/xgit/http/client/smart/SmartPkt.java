package com.boluozhai.snowflake.xgit.http.client.smart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmartPkt {

	public interface COMMAND {

		String v_cmd_prefix = "+v-cmd-";
		String entity = v_cmd_prefix + "entity";
		String stream_begin = v_cmd_prefix + "stream-begin";
		String stream_end = v_cmd_prefix + "stream-end";

		String want = "want";
		String have = "have";

	}

	public interface DEFINE {

		char param_sep = ' ';
		char opt_sep = ';';
		char kv_sep = '=';

	}

	public interface OPTION {

		String containEntity = "entity"; // boolean
		String recursion = "r"; // boolean , -R

		String type = "type"; // string, [ blob | tree|commit|tag|etc ]
		String accept = "accept"; // string , [ blob | tree|commit|tag|etc ]

		String plainSize = "plain-size"; // long
		String offset = "offset"; // long
		String length = "length"; // long
		String remain = "remain"; // long

	}

	private String command;
	private List<String> param;
	private Map<String, String> option;

	public SmartPkt() {
		this.param = new ArrayList<String>();
		this.option = new HashMap<String, String>();
	}

	public SmartPkt(SmartPkt init) {
		this.command = init.command;
		this.param = new ArrayList<String>(init.param);
		this.option = new HashMap<String, String>(init.option);
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<String> getParam() {
		return param;
	}

	public void setParam(List<String> param) {
		this.param = param;
	}

	public Map<String, String> getOption() {
		return option;
	}

	public void setOption(Map<String, String> option) {
		this.option = option;
	}

}
