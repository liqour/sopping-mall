package com.shop.common;

public enum TimeUnit {

	NANOSECONDS(java.util.concurrent.TimeUnit.NANOSECONDS, "毫微秒"), // 十亿分之一秒（就是微秒/1000）
	MICROSECONDS(java.util.concurrent.TimeUnit.MICROSECONDS, "微秒"), // 一百万分之一秒（就是毫秒/1000）
	MILLISECONDS(java.util.concurrent.TimeUnit.MILLISECONDS, "毫秒 "), // 千分之一秒
	SECONDS(java.util.concurrent.TimeUnit.SECONDS, "秒"), 
	MINUTES(java.util.concurrent.TimeUnit.MINUTES, "分"), 
	HOURS(java.util.concurrent.TimeUnit.HOURS, "时"), 
	DAYS(java.util.concurrent.TimeUnit.DAYS, "天");

	private java.util.concurrent.TimeUnit timeUnit;
	private String description;

	private TimeUnit(java.util.concurrent.TimeUnit timeUnit, String description){
		this.timeUnit = timeUnit;
		this.description = description;
	}
	
	public static String getDescription(java.util.concurrent.TimeUnit timeUnit){
		for(TimeUnit unit:TimeUnit.values()){
			if(unit.getTimeUnit().equals(timeUnit)){
				return unit.getDescription();
			}
		}
		return "";
	}
	
	public java.util.concurrent.TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(java.util.concurrent.TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
