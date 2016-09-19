package com.boluozhai.snowflake.datatable;

import com.boluozhai.snowflake.xgit.ObjectId;

public interface DAO {

	String getName(Object object);

	ObjectId getId(Object object);

	Object makePrototype(Object object);

}
