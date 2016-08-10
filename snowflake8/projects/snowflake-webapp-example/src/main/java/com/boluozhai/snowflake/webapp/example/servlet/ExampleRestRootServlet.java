package com.boluozhai.snowflake.webapp.example.servlet;

import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.libwebapp.rest.RestController;
import com.boluozhai.snowflake.libwebapp.rest.RestServlet;

public class ExampleRestRootServlet extends RestServlet {

	private static final long serialVersionUID = -4343160209721510796L;

	@Override
	protected Map<String, RestController> create_handler_table() {

		Map<String, RestController> table = new HashMap<String, RestController>();

		// TODO table.put(key, value) ;

		return table;
	}

}
