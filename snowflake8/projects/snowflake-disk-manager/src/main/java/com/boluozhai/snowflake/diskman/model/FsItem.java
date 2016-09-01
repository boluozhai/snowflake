package com.boluozhai.snowflake.diskman.model;

import java.util.Map;

public class FsItem {

	public interface property {

		String automounthint = "automounthint";
		String blocksize = "blocksize";
		String detectedat = "detectedat";

		String device = "device";
		String device_file = "device-file";
		String device_file_by_id = "device-file.by-id";
		String device_file_by_path = "device-file.by-path";
		String device_file_presentation = "device-file.presentation";

		String drive = "drive";
		String drive_ATASMART = "drive.ATASMART";
		String drive_WWN = "drive.WWN";
		String drive_adapter = "drive.adapter";
		String drive_canspindown = "drive.canspindown";
		String drive_detachable = "drive.detachable";
		String drive_ejectable = "drive.ejectable";
		String drive_ifspeed = "drive.ifspeed";
		String drive_interface = "drive.interface";
		String drive_media = "drive.media";
		String drive_model = "drive.model";
		String drive_ports = "drive.ports";
		String drive_revision = "drive.revision";
		String drive_rotationalmedia = "drive.rotationalmedia";
		String drive_serial = "drive.serial";
		String drive_similardevices = "drive.similardevices";
		String drive_vendor = "drive.vendor";
		String drive_write_cache = "drive.write-cache";

		String hasmedia = "hasmedia";
		String hasmedia_detectionbypolling = "hasmedia.detectionbypolling";
		String hasmedia_detectioninhibitable = "hasmedia.detectioninhibitable";
		String hasmedia_detectioninhibited = "hasmedia.detectioninhibited";
		String hasmedia_detectschange = "hasmedia.detectschange";

		String ismounted = "ismounted";
		String isreadonly = "isreadonly";
		String jobunderway = "jobunderway";
		String label = "label";
		String mountedbyuid = "mountedbyuid";
		String mountpaths = "mountpaths";
		String native_path = "native-path";

		String partition_alignmentoffset = "partition.alignmentoffset";
		String partition_flags = "partition.flags";
		String partition_label = "partition.label";
		String partition_number = "partition.number";
		String partition_offset = "partition.offset";
		String partition_partof = "partition.partof";
		String partition_scheme = "partition.scheme";
		String partition_size = "partition.size";
		String partition_type = "partition.type";
		String partition_uuid = "partition.uuid";

		String partitiontable = "partitiontable";
		String partitiontable_count = "partitiontable.count";
		String partitiontable_scheme = "partitiontable.scheme";
		String presentationhide = "presentationhide";
		String presentationicon = "presentationicon";
		String presentationname = "presentationname";
		String presentationnopolicy = "presentationnopolicy";

		String removable = "removable";
		String size = "size";
		String systeminternal = "systeminternal";
		String type = "type";
		String usage = "usage";
		String uuid = "uuid";
		String version = "version";

	}

	private String primaryKey;
	private String[] alias;
	private Map<String, String> properties;

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String[] getAlias() {
		return alias;
	}

	public void setAlias(String[] alias) {
		this.alias = alias;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public String getString(String key) {
		return this.properties.get(key);
	}

	public int getInteger(String key) {
		String s = this.properties.get(key);
		return Integer.parseInt(s);
	}

}
