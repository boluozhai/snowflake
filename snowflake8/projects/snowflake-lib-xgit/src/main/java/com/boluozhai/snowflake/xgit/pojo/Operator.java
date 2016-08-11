package com.boluozhai.snowflake.xgit.pojo;

public class Operator {

	public static Operator parse(String s) {

		final int i1 = s.lastIndexOf('<');
		final int i2 = s.lastIndexOf('>');

		if (i1 < i2) {

			String name = s.substring(0, i1).trim();
			String mail = s.substring(i1 + 1, i2).trim();
			String time = s.substring(i2 + 1).trim();

			int it = time.indexOf(' ');
			String time1 = time.substring(0, it);
			String time2 = time.substring(it + 1);

			Operator op = new Operator();
			op.setMail(mail);
			op.setName(name);
			op.setTime(Long.parseLong(time1) * 1000);
			op.setZone(Integer.parseInt(time2));

			return op;
		} else {
			return null;
		}

	}

	private String name;
	private String mail;
	private long time;
	private int zone; // +800 = east+8h

	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append(name);
		sb.append(" <");
		sb.append(mail);
		sb.append("> ");
		sb.append(time / 1000);

		sb.append(' ');
		String z = String.valueOf(zone);
		if (zone > 0) {
			sb.append('+');
		}
		for (int i = z.length(); i < 4; i++) {
			sb.append('0');
		}
		sb.append(z);

		return sb.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getZone() {
		return zone;
	}

	public void setZone(int zone) {
		this.zone = zone;
	}

}
