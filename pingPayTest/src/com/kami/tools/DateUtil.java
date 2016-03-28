package com.kami.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
	
	public static final long OneDayMillies = 24*60*60*1000L;
	
	/**
	 * 得到系统日期
	 * @return
	 */
	public static Date getDate() { 
		TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8");
		TimeZone.setDefault(tz);
        return new Date();
	}
	
	/**
	 * 得到系统Calendar日期
	 * @return
	 */
	public static Calendar getCalendar() {
		TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8");
		TimeZone.setDefault(tz);
		Calendar cal = Calendar.getInstance();
		return cal;
	}
	/**
	 * 得到当天零时，0：0：0 0的时间戳
	 * @Description: TODO
	 * @param @return   
	 * @return long  
	 * @throws
	 * @author Kami_SDY
	 * @date 2015-12-18
	 */
	public static long getMillisecond_DAY(){
		long millisecond = 0;
		TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8");
		TimeZone.setDefault(tz);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		millisecond = cal.getTimeInMillis();
		return millisecond;
	}
	
	/**
	 * 根据系统时间，获取本月1号的时间戳
	 * @return
	 */
	public static long getMillisecond_MONTH() {
		long millisecond = 0;
		TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8");
		TimeZone.setDefault(tz);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		millisecond = cal.getTimeInMillis();
		return millisecond;
	}
	
	/**
	 * 根据系统时间，获取上个月1号的时间戳
	 * @return
	 */
	public static long getMillisecond_FRONTMONTH() {
		long millisecond = 0;
		TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8");
		TimeZone.setDefault(tz);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, Calendar.MONTH-2);
		millisecond = cal.getTimeInMillis();
		return millisecond;
	}
	
	/**
	 * 获取当前系统时间，日期转毫秒
	 * @param date
	 * @return
	 */
	public static long getCurMilli() { 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newDate = sdf.format(DateUtil.getDate());
		long millisecond = 0;
		try {
			millisecond = sdf.parse(newDate).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return millisecond;
	}
	
	/**
	 * 根据参数日期，日期转毫秒
	 * @param date
	 * @return
	 */
	public static long getMillisecond(Date date) { 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newDate = "";
		if (!"".equals(date)) {
			newDate = sdf.format(date);
		} else {
			newDate = sdf.format(DateUtil.getDate());
		}
		long millisecond = 0;
		try {
			millisecond = sdf.parse(newDate).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return millisecond;
	}
	
	/**
	 * 日期转毫秒（加24小时,yyyy-MM-dd HH:mm:ss）
	 * @param date
	 * @return
	 */
	public static long getMillisecond_24h(Date date) { 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newDate = "";
		if (!"".equals(date)) {
			newDate = sdf.format(date);
		} else {
			newDate = sdf.format(DateUtil.getDate());
		}
		long millisecond = 24*3600*1000;
		try {
			millisecond += sdf.parse(newDate).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return millisecond;
	}
	
	/**
	 * 日期转毫秒（加N年）
	 * @param date
	 * @return
	 */
	public static long getMillisecond_year(String date, Integer year){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newDate = "";
		if ("".equals(date)) {
			newDate = sdf.format(DateUtil.getDate());
		} else {
			newDate = getDateTime(Long.parseLong(date));
		}
        Date dt = null;
		try {
			dt = sdf.parse(newDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.YEAR, year);
        Date dt1 = rightNow.getTime();
        return dt1.getTime();
	}
	
	/**
	 * 日期转毫秒（加N天）
	 * @param date 毫秒字符串
	 * @return
	 */
	public static long getMillisecond_day(String date, Integer day){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newDate = "";
		if ("".equals(date)) {
			newDate = sdf.format(DateUtil.getDate());
		} else {
			newDate = getDateTime(Long.parseLong(date));
		}
        Date dt = null;
		try {
			dt = sdf.parse(newDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.DATE, day);
        Date dt1 = rightNow.getTime();
        return dt1.getTime();
	}
	
	/**
	 * 日期转毫秒（加N月）
	 * @param date
	 * @return
	 */
	public static long getMillisecond_month(String date, Integer day){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newDate = "";
		if ("".equals(date)) {
			newDate = sdf.format(DateUtil.getDate());
		} else {
			newDate = getDateTime(Long.parseLong(date));
		}
        Date dt = null;
		try {
			dt = sdf.parse(newDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.MONTH, day);
        Date dt1 = rightNow.getTime();
        return dt1.getTime();
	}
	
	/**
	 * 字符串日期转毫秒
	 * @param date，年月日时分秒
	 * @return
	 */
	public static long getMillisecond_str(String date) { 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if ("".equals(date)) {
			date = sdf.format(DateUtil.getDate());
		} 
		long millisecond = 0;
		try {
			millisecond = sdf.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return millisecond;
	}
	
	/**
	 * 字符串日期转毫秒
	 * @param date，年月日
	 * @return
	 */
	public static long getMillisecond_strYmd(String date) { 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if ("".equals(date)) {
			date = sdf.format(DateUtil.getDate());
		} 
		long millisecond = 0;
		try {
			millisecond = sdf.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return millisecond;
	}
	/**
	 * 字符串日期转毫秒
	 * @param date，年月日
	 * @return date为null或格式不对时，返回-1
	 */
	public static long getMillisecond2_strYmd(String date) { 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long millisecond = -1;
		try {
			millisecond = sdf.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return millisecond;
	}
	/**
	 * 字符串日期转毫秒转毫秒（加24小时）
	 * @param date
	 * @return
	 */
	public static long getMillisecond_str_24h(String date) { 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if ("".equals(date)) {
			date = sdf.format(DateUtil.getDate());
		} 
		long millisecond = 24*3600*1000;
		try {
			millisecond += sdf.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return millisecond;
	}
	
	/**
	 * 毫秒转日期，年月日
	 * @param millisecond
	 * @return
	 */
	public static String getDate(long millisecond) { 
		if (millisecond == 0) {
			millisecond = System.currentTimeMillis();
		}
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millisecond);
		return dateformat.format(calendar.getTime());
	}
	
	/**
	 * 毫秒转日期时间，年月日时分秒
	 * @param millisecond
	 * @return
	 */
	public static String getDateTime(long millisecond) { 
		if (millisecond == 0) {
			millisecond = System.currentTimeMillis();
		}
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millisecond);
		return dateformat.format(calendar.getTime());
	}
	
	/**
	 * 两日期相差毫秒
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getMinusMillisecond(Date startDate, Date endDate) { 
		long startMillisecond = getMillisecond(startDate);
		long endMillisecond = getMillisecond(endDate);
		long minusMillisecond = endMillisecond-startMillisecond;
		if (minusMillisecond < 0) {
			minusMillisecond = 0;
		}
		return minusMillisecond;
	}
	
	/**
	 * 两日期相差天数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getMinusDay(Date startDate, Date endDate) { 
		long startMillisecond = getMillisecond(startDate);
		long endMillisecond = getMillisecond(endDate);
		long minusMillisecond = endMillisecond-startMillisecond;
		long day = 0;
		if (minusMillisecond < 0) {
			day = 0;
		} else {
			day = minusMillisecond/(24*3600*1000); 
		}
		return day;
	}
	
	/**
	 * 前N天毫秒
	 * @param day
	 * @return
	 */
	public static long getRetreatDay_millisecond(int day) { 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long nowMillisecond = 0; 
		try {
			nowMillisecond = sdf.parse(sdf.format(DateUtil.getDate())).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		nowMillisecond += 24*3600*1000;
		long retreatMillisecond = 24*3600*1000*day;
		return nowMillisecond - retreatMillisecond;
	}
	
	/**
	 * 日期转秒
	 * @param date
	 * @return
	 */
	public static long getDecond(Date date) { 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newDate = "";
		if (!"".equals(date)) {
			newDate = sdf.format(date);
		} else {
			newDate = sdf.format(DateUtil.getDate());
		}
		long second = 0; 
		try {
			second = sdf.parse(newDate).getTime()/1000;
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return second;
	}

	/**
	 * 耗时，对人友好格式化
	 * 
	 * @author lzq1689
	 * @param consumedMillies 消耗的毫秒数
	 * @return
	 */
	public static String timeConsumeFormat(long consumedMillies){
		long days = consumedMillies/(24*60*60*1000);
		long hours = consumedMillies % (24*60*60*1000) / (60*60*1000);
		long minutes = consumedMillies % (60*60*1000) / (60*1000);
		long seconds = consumedMillies % (60*1000) / (1000);
		long millies = consumedMillies % 1000;
		StringBuffer sb = new StringBuffer();
		sb.append(days == 0 ? "" : days + "天")
			.append(hours == 0 ? "" : hours + "小时")
			.append(minutes == 0 ? "" : minutes + "分钟")
			.append(seconds == 0 ? "" : seconds + "秒")
			.append(millies + "毫秒");
		return sb.toString();
	}
	public static void main(String[] args) throws ParseException {
		System.out.println(getDateTime(getMillisecond_MONTH()));
		System.out.println(getDateTime(getMillisecond_FRONTMONTH()));
		System.out.println(DateUtil.getCurMilli());
		System.out.println(getMillisecond_str("2015-12-18 00:00:00")); 
		System.out.println(getMillisecond_DAY());
		System.out.println(getMillisecond_day("1382011708000",-1)); 
		System.out.println(getDateTime(Long.parseLong("1382011708000")));
		System.out.println(getDateTime(Long.parseLong("1438145041000")));
		System.out.println(getMillisecond_month("1422506641000", 6));
		System.out.println(getCurMilli());
		System.out.println(getDate());
	}
	
}
