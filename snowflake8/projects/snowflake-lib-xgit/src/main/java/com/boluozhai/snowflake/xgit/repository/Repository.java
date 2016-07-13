package com.boluozhai.snowflake.xgit.repository;

import java.net.URI;

import com.boluozhai.snow.mvc.model.Component;

public interface Repository extends Component {

	RepositoryContext context();

	// RepositoryDriver driver();

	URI location();

}
