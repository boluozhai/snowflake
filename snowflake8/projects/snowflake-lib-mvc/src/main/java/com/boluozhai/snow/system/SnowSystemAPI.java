package com.boluozhai.snow.system;

import java.io.InputStream;
import java.io.PrintStream;

public interface SnowSystemAPI {

	PrintStream out();

	PrintStream err();

	InputStream in();

}
