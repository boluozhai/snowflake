package com.boluozhai.snowflake.xgit.http.client.smart;

import com.boluozhai.snowflake.xgit.ObjectId;

public class SmartPkt {

	public interface COMMAND {

		String entity = "virtual.command.entity";

		String want = "want";
		String have = "have";

	}

	private String command; // [want,have,etc]
	private ObjectId id;
	private ObjectId id2;
	private String ref;

	private boolean containEntity;
	private boolean recursion; // -R

	private String type; // [ blob | tree|commit|tag|etc ]
	private String accept; // [ blob | tree|commit|tag|etc ]

	private long plainSize;
	private long offset;
	private long length;
	private long remain;

	public SmartPkt() {
	}

	public SmartPkt(SmartPkt init) {
		this.accept = init.accept;
		this.command = init.command;
		this.containEntity = init.containEntity;
		this.id = init.id;
		this.length = init.length;
		this.offset = init.offset;
		this.recursion = init.recursion;
		this.remain = init.remain;
		this.type = init.type;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public long getRemain() {
		return remain;
	}

	public void setRemain(long remain) {
		this.remain = remain;
	}

	public boolean isContainEntity() {
		return containEntity;
	}

	public void setContainEntity(boolean containEntity) {
		this.containEntity = containEntity;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public boolean isRecursion() {
		return recursion;
	}

	public void setRecursion(boolean recursion) {
		this.recursion = recursion;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAccept() {
		return accept;
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

	public long getPlainSize() {
		return plainSize;
	}

	public void setPlainSize(long plainSize) {
		this.plainSize = plainSize;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public ObjectId getId2() {
		return id2;
	}

	public void setId2(ObjectId id2) {
		this.id2 = id2;
	}

}
