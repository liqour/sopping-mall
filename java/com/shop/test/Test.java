package com.shop.test;

import org.slf4j.LoggerFactory;

public class Test {

	static final org.slf4j.Logger logger = LoggerFactory.getLogger(Test.class);
	
	public static void main(String[] args) {
		logger.info("info");
		logger.error("error");
		logger.warn("warn");
		logger.debug("debug");
	}
	
}
