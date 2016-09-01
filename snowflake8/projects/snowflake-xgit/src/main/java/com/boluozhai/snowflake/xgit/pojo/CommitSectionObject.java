package com.boluozhai.snowflake.xgit.pojo;

public class CommitSectionObject extends CommitObject {

	public void setSection(String section) {

		if (section.contains("\\")) {
			section = section.replace('\\', '/');
		}

		String[] array = section.split("/");
		StringBuilder sb = new StringBuilder();
		for (String part : array) {
			part = part.trim();
			if (part.length() > 0) {
				if (sb.length() > 0) {
					sb.append('/');
				}
				sb.append(part);
			}
		}

		String key = CommitObject.KEY.section;
		this.setHeaderValue(key, sb.toString());
	}

	public String getSection() {
		String key = CommitObject.KEY.section;
		return this.getHeaderValue(key);
	}

}
