package com.boluozhai.snowflake.h2o.data.dao;

import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.h2o.data.pojo.element.AliasItem;
import com.boluozhai.snowflake.h2o.data.pojo.model.AliasDTM;

public class AliasDAO {

	private final DataClient client;

	public AliasDAO(DataClient dc) {
		this.client = dc;
	}

	public String findUser(String user) {
		for (int r = 3; r > 0; r--) {
			AliasDTM dtm = client.get(user, AliasDTM.class);
			if (dtm == null) {
				break;
			} else {
				AliasItem to = dtm.getTo();
				if (to == null) {
					return user;
				} else {
					user = to.getName();
				}
			}
		}
		return null;
	}

}
