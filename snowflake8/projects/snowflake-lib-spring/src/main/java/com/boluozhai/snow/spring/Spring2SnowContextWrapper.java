package com.boluozhai.snow.spring;

import org.springframework.context.ApplicationContext;

import com.boluozhai.snow.context.SnowContext;
import com.boluozhai.snow.context.support.AbstractContext;
import com.boluozhai.snow.context.support.ContextInfo;
import com.boluozhai.snow.mvc.controller.ProcessContext;

public class Spring2SnowContextWrapper extends AbstractContext implements
		ProcessContext {

	public Spring2SnowContextWrapper(ApplicationContext ac, ContextInfo info) {

		super(info);

	}

	public static SnowContext wrap(ApplicationContext ac) {
		// TODO Auto-generated method stub
		return null;
	}

}
