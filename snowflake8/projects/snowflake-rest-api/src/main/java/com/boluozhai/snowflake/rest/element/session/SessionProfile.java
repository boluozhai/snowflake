package com.boluozhai.snowflake.rest.element.session;

import com.boluozhai.snowflake.rest.element.account.AccountProfile;

public class SessionProfile extends AccountProfile {

	private long loginTimestamp;
	private boolean login;

	public long getLoginTimestamp() {
		return loginTimestamp;
	}

	public void setLoginTimestamp(long loginTimestamp) {
		this.loginTimestamp = loginTimestamp;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

}
