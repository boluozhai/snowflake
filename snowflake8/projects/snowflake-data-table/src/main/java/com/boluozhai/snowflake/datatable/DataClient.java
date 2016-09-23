package com.boluozhai.snowflake.datatable;

import java.io.Closeable;

public interface DataClient extends Closeable {

	/**********
	 * @param name
	 *            is a email-address, or, a user-name@this
	 * */

	DataLine line(String email);

	DataLine line(String host, String user);

	String[] list(Class<?> type);

}
