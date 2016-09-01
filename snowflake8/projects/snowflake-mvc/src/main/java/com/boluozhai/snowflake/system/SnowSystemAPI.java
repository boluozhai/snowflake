package com.boluozhai.snowflake.system;

import java.io.InputStream;
import java.io.PrintStream;

public interface SnowSystemAPI {

	PrintStream out();

	PrintStream err();

	InputStream in();

}
