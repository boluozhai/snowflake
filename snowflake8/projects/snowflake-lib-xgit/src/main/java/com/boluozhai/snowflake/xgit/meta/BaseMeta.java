package com.boluozhai.snowflake.xgit.meta;

import java.io.IOException;

public interface BaseMeta {

	Class<?> getType();

	String getKeyName();

	// text

	String getText();

	String loadText() throws IOException;

	void saveText(String text) throws IOException;

	// JSON

	<T> T getJSON(Class<T> type);

	<T> T loadJSON(Class<T> type) throws IOException;

	void saveJSON(Object pojo) throws IOException;

}
