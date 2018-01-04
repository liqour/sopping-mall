package com.shop.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @Description: 时间工具类 
 * @ClassName: DateTool 
 * @author liquor
 * @date 2017年12月28日 下午2:35:20 
 * @Introduce: Calendar ERA 纪元 0表示公元前 1表示公元
 * 						YEAR 年
 * 						MONTH 月
 * 						WEEK_OF_YEAR 当前日期在本年中对应第几个星期
 * 						WEEK_OF_MONTH 当前日期在本月中对应第几个星期
 * 						DATE 日
 * 						DAY_OF_MONTH 日
 * 						DAY_OF_YEAR 当前日期在本年中对应第几天
 * 						DAY_OF_WEEK 星期几
 * 						DAY_OF_WEEK_IN_MONTH 当前月中的第几个星期
 * 						AM_PM 0表示上午1表示下午
 * 						HOUR 指示一天中的第几小时12 小时制
 * 						HOUR_OF_DAY 指示一天中的第几小时24 小时制
 * 						MINUTE 一小时中的第几分钟
 * 						SECOND 一分钟中的第几秒
 * 						MILLISECOND 一秒中的第几毫秒
 * 						ZONE_OFFSET 毫秒为单位指示距 GMT 的大致偏移量
 * 						DST_OFFSET 毫秒为单位指示夏令时的偏移量
 */
public class DateTool {

	/**
	 * @Description: 缺省的日期显示格式 yyyy-MM-dd
	 */
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * @Description: 缺省的日期显示格式 yyyyMMdd
	 */
	public static final String DEFAULT_YYYYMMDD_FORMAT = "yyyyMMdd";

	/**
	 * @Description: 缺省的日期显示格式 yyyy-MM
	 */
	public static final String DEFAULT_YYMM_FORMAT = "yyyy-MM";

	/**
	 * @Description: 缺省的日期时间显示格式 yyyy-MM-dd HH:mm:ss
	 */
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * @Description: 私有构造方法，禁止对该类进行实例化
	 */
	private DateTool() {
	}
	
	/**
	 * @Description: 得到系统当前日期时间 
	 * @Notice: 这里可能会遇到时区问题,启动参数加上-Duser.timezone=GMT+8
	 * @Title: getNow 
	 * @return Date 系统当前时间
	 * @author liquor
	 */
	public static Date getNow() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * @Description: 得到格式化的当前日期 YYYY-MM-DD
	 * @Title: getDate 
	 * @return String  当前时间
	 * @author liquor
	 */
	public static String getDate() {
		return getDateTime(DEFAULT_DATE_FORMAT);
	}

	/**
	 * @Description: 得到用缺省方式格式化的当前日期及时间 YYYY-MM-DD HH:MM:SS
	 * @Title: getDateTime 
	 * @return String  当前时间
	 * @author liquor
	 */
	public static String getDateTime() {
		return getDateTime(DEFAULT_DATETIME_FORMAT);
	}

	/**
	 * @Description: 得到系统当前日期及时间，并用指定的方式格式化
	 * @Title: getDateTime 
	 * @param pattern 显示格式
	 * @return String    当前日期及时间
	 * @author liquor
	 */
	public static String getDateTime(String pattern) {
		Date datetime = Calendar.getInstance().getTime();
		return getDateTime(datetime, pattern);
	}

	/**
	 * @Description: 将指定日期按照指定方式格式化
	 * @Title: getDateTime 
	 * @param date 需要进行格式化的日期
	 * @param pattern 显示格式
	 * @return String 日期时间字符串
	 * @author liquor
	 */
	public static String getDateTime(Date date, String pattern) {
		if (null == pattern || "".equals(pattern)) {
			pattern = DEFAULT_DATETIME_FORMAT;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}

	/**
	 * @Description: 得到当前年份
	 * @Title: getCurrentYear 
	 * @return INT 当前年份
	 * @author liquor
	 */
	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * @Description: 得到当前月份
	 * @Title: getCurrentMonth 
	 * @return INT 当前月份
	 * @author liquor
	 */
	public static int getCurrentMonth() {
		// 用get得到的月份数比实际的小1，需要加上
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	/**
	 * @Description: 得到当前日
	 * @Title: getCurrentDay 
	 * @return INT 当前日
	 * @author liquor
	 */
	public static int getCurrentDay() {
		return Calendar.getInstance().get(Calendar.DATE);
	}

	/**
	 * @Description: 获取当天0点0分
	 * @Title: getCurrentToDay 
	 * @return Date 当天0点0分
	 * @author liquor
	 */
	public static Date getCurrentToDay() {
		return parse(getDate(), DEFAULT_DATE_FORMAT);
	}

	/**
	 * @Description: 获取当天23点59分59秒
	 * @Title: getCurrentLastDate 
	 * @return Date 当天23点59分
	 * @author liquor
	 */
	public static Date getCurrentLastDate(){
		return add(addDays(getCurrentToDay(), 1), -1, Calendar.SECOND);
	}
	
	/**
	 * @Description: 获取指定日期23点59分59秒
	 * @Title: getDayLastDate 
	 * @param date 需要获取的日期
	 * @return Date 日期的23点59分59秒
	 * @author liquor
	 */
	public static Date getDayLastDate(Date date) {
		String dateStr = getDateTime(date, DEFAULT_DATE_FORMAT);
		return parse(dateStr + " 23:59:59", DEFAULT_DATETIME_FORMAT);
	}
	
	/**
	 * @Description:  取得当前日期以后若干分钟的日期。如果要得到以前的日期，参数用负数。
	 * @Title: addDays 
	 * @param hours 增加的分钟数
	 * @return Date  增加以后的日期
	 * @author liquor
	 */
	public static Date addMinute(int minute){
		return add(getNow(), minute, Calendar.MINUTE);
	}
	
	/**
	 * @Description:  取得指定日期以后若干分钟的日期。如果要得到以前的日期，参数用负数。
	 * @Title: addDays 
	 * @param hours 增加的分钟数
	 * @return Date  增加以后的日期
	 * @author liquor
	 */
	public static Date addMinute(Date date, int minute){
		return add(date, minute, Calendar.MINUTE);
	}
	
	/**
	 * @Description:  取得当前日期以后若干小时的日期。如果要得到以前的日期，参数用负数。
	 * @Title: addDays 
	 * @param hours 增加的小时数
	 * @return Date  增加以后的日期
	 * @author liquor
	 */
	public static Date addHour(int hours){
		return add(getNow(), hours, Calendar.HOUR);
	}
	
	/**
	 * @Description:  取得指定日期以后若干小时的日期。如果要得到以前的日期，参数用负数。
	 * @Title: addDays 
	 * @param hours 增加的小时数
	 * @return Date  增加以后的日期
	 * @author liquor
	 */
	public static Date addHour(Date date, int hours){
		return add(date, hours, Calendar.HOUR);
	}
	
	/**
	 * @Description:  取得当前日期以后若干天的日期。如果要得到以前的日期，参数用负数。 例如要得到上星期同一天的日期，参数则为-7
	 * @Title: addDays 
	 * @param days 增加的日期数
	 * @return Date  增加以后的日期
	 * @author liquor
	 */
	public static Date addDays(int days) {
		return add(getNow(), days, Calendar.DATE);
	}

	/**
	 * @Description: 取得指定日期以后若干天的日期。如果要得到以前的日期，参数用负数。
	 * @Title: addDays 
	 * @param date 基准日期
	 * @param days 增加的日期数
	 * @return Date 增加以后的日期
	 * @author liquor
	 */
	public static Date addDays(Date date, int days) {
		return add(date, days, Calendar.DATE);
	}

	/**
	 * @Description: 取得当前日期以后某月的日期。如果要得到以前月份的日期，参数用负数。
	 * @Title: addMonths 
	 * @param months 增加的月份数
	 * @return Date  增加以后的日期
	 * @author liquor
	 */
	public static Date addMonths(int months) {
		return add(getNow(), months, Calendar.MONTH);
	}

	/**
	 * @Description: 取得指定日期以后某月的日期。如果要得到以前月份的日期，参数用负数
	 * 				  注意，可能不是同一日子，例如2003-1-31加上一个月是2003-2-28
	 * @Title: addMonths 
	 * @param date 基准日期
	 * @param months 增加的月份数
	 * @return Date 增加以后的日期
	 * @author liquor
	 */
	public static Date addMonths(Date date, int months) {
		return add(date, months, Calendar.MONTH);
	}

	/**
	 * @Description: 为指定日期增加相应的天数、月数或者年
	 * @Title: add 
	 * @param date 基准日期
	 * @param amount 增加的数量
	 * @param field 增加的单位，年，月或者日Calendar.YEAR
	 * @return Date 增加以后的日期
	 * @author liquor
	 */
	public static Date add(Date date, int amount, int field) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * @Description: 计算两个日期相差分钟数。 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
	 * @Notice:如果不足一分钟则返回0,这里要注意0.99的情况
	 * @Title: diffMinutes
	 * @param one 第一个日期数，作为基准
	 * @param two 第二个日期数，作为比较
	 * @return long  两个日期相差分钟数
	 * @author liquor
	 */
	public static long diffMinutes(Date one, Date two){
		return (one.getTime() - two.getTime()) / (60 * 1000);
	}
	
	/**
	 * @Description: 计算两个日期相差小时数。 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
	 * @Notice:如果不足一小时则返回0,这里要注意0.99的情况
	 * @Title: diffHours
	 * @param one 第一个日期数，作为基准
	 * @param two 第二个日期数，作为比较
	 * @return long  两个日期相差小时数
	 * @author liquor
	 */
	public static long diffHours(Date one, Date two){
		return (one.getTime() - two.getTime()) / (60 * 60 * 1000);
	}
	
	/**
	 * @Description: 计算两个日期相差天数(完整的24小时算一天)。 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
	 * 				 如果不足一天则返回0,这里要注意0.99的情况
	 * @Title: diffDays 
	 * @param one 第一个日期数，作为基准
	 * @param two 第二个日期数，作为比较
	 * @return long  两个日期相差天数
	 * @author liquor
	 */
	public static long diffDays(Date one, Date two) {
		return (one.getTime() - two.getTime()) / (24 * 60 * 60 * 1000);
	}

	/**
	 * @Description: 计算两个日期跨天数(大日期在后)。 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
	 * 				 跨天数,今天23点与明天0点也是相差一天
	 * @Title: diffDay 
	 * @param one 第一个日期数，作为基准
	 * @param two 第二个日期数，作为比较
	 * @return long   两个日期相差天数
	 * @author liquor
	 */
	public static long diffDay(Date one, Date two) {
		Calendar cal1 = Calendar.getInstance();
        cal1.setTime(one);
        
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(two);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
        
        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2){// 不是同一年
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++){
                if((i%4==0 && i%100!=0) || i%400==0){//闰年
                    timeDistance += 366;
                }
                else{// 不是闰年
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2-day1) ;
        }
        else{// 不同年
            return day2-day1;
        }
	}

	/**
	 * @Description: 获取指定日期是星期几
	 * @Title: diffDay 
	 * @param one 第一个日期数，作为基准
	 * @return long   日期是星期几
	 * @author liquor
	 */
	public static long getWeek(Date one) {
		Calendar calendar = Calendar.getInstance();
		// 得到第一个日期的年分和月份数
		calendar.setTime(one);
		int oneday = calendar.get(Calendar.DAY_OF_WEEK);
		return oneday - 1;
	}
	
	/**
	 * @Description: 计算两个日期相差月份数 如果前一个日期小于后一个日期，则返回负数
	 * @Title: diffMonths 
	 * @param one 第一个日期数，作为基准
	 * @param two 第二个日期数，作为比较
	 * @return INT 两个日期相差月份数
	 * @author liquor
	 */
	public static int diffMonths(Date one, Date two) {
		Calendar calendar = Calendar.getInstance();
		// 得到第一个日期的年分和月份数
		calendar.setTime(one);
		int yearOne = calendar.get(Calendar.YEAR);
		int monthOne = calendar.get(Calendar.MONDAY);
		// 得到第二个日期的年份和月份
		calendar.setTime(two);
		int yearTwo = calendar.get(Calendar.YEAR);
		int monthTwo = calendar.get(Calendar.MONDAY);

		return (yearOne - yearTwo) * 12 + (monthOne - monthTwo);
	}

	/**
	 * @Description: 将一个字符串用给定的格式转换为日期类型。注意：如果返回null，则表示解析失败
	 * @Title: parse 
	 * @param datestr 需要解析的日期字符串
	 * @param pattern 日期字符串的格式，默认为“YYYY-MM-DD”的形式
	 * @return Date 解析后的日期
	 * @author liquor
	 */
	public static Date parse(String datestr, String pattern) {
		Date date = null;
		if (null == pattern || "".equals(pattern)) {
			pattern = DEFAULT_DATE_FORMAT;
		}
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			date = dateFormat.parse(datestr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * @Description: 返回本月的最后一天
	 * @Title: getMonthLastDay 
	 * @return Date 本月最后一天的日期
	 * @author liquor
	 */
	public static Date getMonthLastDay() {
		return getMonthLastDay(getNow());
	}

	/**
	 * @Description: 返回给定日期中的月份中的最后一天
	 * @Title: getMonthLastDay 
	 * @param date 基准日期
	 * @return Date 该月最后一天的日期
	 * @author liquor
	 */
	public static Date getMonthLastDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// 将日期设置为下一月第一天
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);
		// 减去1天，得到的即本月的最后一天
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}

	/**
	 * @Description: 比较日期大小
	 * @Title: compareDate 
	 * @param date1 日期1
	 * @param date2 日期2
	 * @return INT 如果日期1大于日期2则返回1,等于则返回0,小于返回-1 
	 * @throws 
	 * @author liquor
	 */
	public static int compareDate(Date date1, Date date2) {
		if (null == date1 && date2 == null) {
			return 0;
		}
		try {
			Date dt1 = DateTool.parse(DateTool.getDateTime(date1, DateTool.DEFAULT_DATETIME_FORMAT),
					DateTool.DEFAULT_DATETIME_FORMAT);
			Date dt2 = DateTool.parse(DateTool.getDateTime(date2, DateTool.DEFAULT_DATETIME_FORMAT),
					DateTool.DEFAULT_DATETIME_FORMAT);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {

		}
		return 0;
	}

	/**
	 * @Description: 判断日期是否在某个时间段内
	 * @Title: between 
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param date 时间
	 * @return boolean 时间是否在时间段内
	 * @throws 
	 * @author liquor
	 */
	public static boolean isBetween(Date startDate, Date endDate, Date date){
		if(date.after(startDate) && date.before(endDate)){
			return true;
		}
		return false;
	}
	
}