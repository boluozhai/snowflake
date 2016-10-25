package com.boluozhai.snowflake.xgit.site;

import com.boluozhai.snowflake.mvc.model.Component;

public interface MimeTypeRegistrar extends Component {

	String getTypeNameByFileName(String fileName);

	String getTypeNameByExtName(String extName);

	MimeTypeInfo get(String typeName);

	void register(MimeTypeInfo info);

	MimeTypeRegistrar cache();

}
