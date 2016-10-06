package com.boluozhai.snowflake.installer.min.context;

import javax.servlet.ServletContext;

final class InnerApplicationContextFactory {

	private ApplicationContext _fast_ac;
	private ApplicationContext _safe_ac;

	public ApplicationContext getAC(ServletContext sc) {
		ApplicationContext ac = this._fast_ac;
		if (ac == null) {
			ac = this.create_ac(sc);
			this._fast_ac = ac;
		}
		return ac;
	}

	private synchronized ApplicationContext create_ac(ServletContext sc) {
		ApplicationContext ac = this._safe_ac;
		if (ac == null) {
			InnerApplicationContext new_ac = new InnerApplicationContext();
			new_ac.init();
			ac = new_ac;
			this._safe_ac = ac;
		}
		return ac;
	}

}
