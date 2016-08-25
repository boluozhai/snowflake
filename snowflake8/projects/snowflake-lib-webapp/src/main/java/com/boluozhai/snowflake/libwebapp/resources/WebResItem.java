package com.boluozhai.snowflake.libwebapp.resources;

public class WebResItem {

	private String groupId;
	private String artifactId;
	private String version;

	private String pathInWar;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPathInWar() {
		return pathInWar;
	}

	public void setPathInWar(String pathInWar) {
		this.pathInWar = pathInWar;
	}

	/**********
	 * @param string
	 *            like 'groudId:artifactId:version:pathInWar'
	 * */

	public static WebResItem parse(String string) {

		String[] array = string.split(":");
		WebResItem item = new WebResItem();

		item.setGroupId(array[0]);
		item.setArtifactId(array[1]);
		item.setVersion(array[2]);
		item.setPathInWar(array[3]);

		return item;
	}

}
