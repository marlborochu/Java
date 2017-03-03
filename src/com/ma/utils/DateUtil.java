
package com.ma.utils;



import java.text.*;
import java.util.*;



/**
 * @author marlboro.chu@gmail.com
 **/

public class DateUtil{
	
	//private org.apache.log4j.Logger logger = Log4jUtil.getInstance().getLogger(com.bull.prj.Constant.getInstance().getApp_log_name());
	private DateUtil(){}
	private static DateUtil du ;
	private String format_type = "yyyy/MM/dd";
	private String default_datetime_format = "yyyyMMddkkmmss";
	private SimpleDateFormat sdf = new SimpleDateFormat(format_type);
	
	public synchronized static DateUtil getInstance(){
		if(du == null){
			du = new DateUtil();
		}	
		return du;
	}
	/**
	 * 
	 **/
	public long TimeInterval(Calendar startdate, Calendar enddate) {
		
		
		long interval_times = 0;
		try {
			interval_times = enddate.getTimeInMillis()
					- startdate.getTimeInMillis();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return interval_times;

	}
	/**
	 * 
	 **/
	public long TimeInterval(String startdate, String enddate) {
		
		long interval_times = 0;
		try {
			Date sd = sdf.parse(startdate);
			Date ed = sdf.parse(enddate);
			Calendar sc = (Calendar) Calendar.getInstance().clone();
			sc.setTime(sd);
			Calendar ec = (Calendar) Calendar.getInstance().clone();
			ec.setTime(ed);
			interval_times = TimeInterval(sc, ec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return interval_times;
	}
	
	public long TimeInterval(Date startdate, Date enddate) {
		
		long interval_times = 0;
		try {
			Calendar sc = (Calendar) Calendar.getInstance().clone();
			sc.setTime(startdate);
			Calendar ec = (Calendar) Calendar.getInstance().clone();
			ec.setTime(enddate);
			interval_times = TimeInterval(sc, ec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return interval_times;
	}
	
	
	public int DayInterval(Calendar startdate, Calendar enddate) {
		
		int interval_days = 0;
		try {
			Date sd = sdf.parse(formatDate(startdate));
			Date ed = sdf.parse(formatDate(enddate));
			Calendar sc = (Calendar) Calendar.getInstance().clone();
			sc.setTime(sd);
			Calendar ec = (Calendar) Calendar.getInstance().clone();
			ec.setTime(ed);

			interval_days = (int) (TimeInterval(sc, ec) / (1000L * 60L * 60L * 24L));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return interval_days;

	}
	

	public int DayInterval(String startdate, String enddate) {
		
		int interval_days = 0;
		try {
			Date sd = sdf.parse(startdate);
			Date ed = sdf.parse(enddate);
			Calendar sc = (Calendar) Calendar.getInstance().clone();
			sc.setTime(sd);
			Calendar ec = (Calendar) Calendar.getInstance().clone();
			ec.setTime(ed);
			interval_days = DayInterval(sc, ec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return interval_days;

	}
	
	public int DayInterval(Date begin_date, Date end_date) {
		Calendar tsc = Calendar.getInstance();
		tsc.setTimeInMillis(begin_date.getTime());
		Calendar dc = Calendar.getInstance();
		dc.setTimeInMillis(end_date.getTime());
		return DayInterval(tsc, dc);
	}
	
	public boolean compareDate(Date begin_date, Date end_date,Date check_date){
		
		
		if( DayInterval(begin_date,check_date) >=0 && 
			DayInterval(check_date,end_date) >= 0 ){
			return true;
		}else{
			return false;
		}
		
	}
	
	public String formatDate(Calendar d) {
		if (d != null) {
			Date dd = d.getTime();
			return formatDate(dd, format_type);
		} else {
			return null;
		}
	}

	public String formatDate(Date dd) {
		if (dd != null) {
			return formatDate(dd, format_type);
		} else {
			return null;
		}
	}
	
	public String formatDate(Date d, String format) {
		if (d != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(d);
		} else {
			return null;
		}
	}
	
	public Date parseDate(String dd) {
		
		if (dd != null) {
			try{
				SimpleDateFormat sdf = new SimpleDateFormat(format_type);
				return sdf.parse(dd);
			}catch(Exception e){
				e.printStackTrace();
				return null;	
			}
		} else {
			return null;
		}
	}
	
	
	public Date parseDate(String dd,String pattern) {
		
		if (dd != null) {
			try{
				SimpleDateFormat sdf = new SimpleDateFormat(pattern);
				return sdf.parse(dd);
			}catch(Exception e){
				e.printStackTrace();
				return null;	
			}
		} else {
			return null;
		}
	}
    public Date addMinutes(Date date, int minutes) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE , minutes);
		return c.getTime();
	}
	public Date addHours(Date date, int hours) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR, hours);
		return c.getTime();
	}
	public Date addDays(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		return c.getTime();
	}
	
	public Date addWeeks(Date date, int weeks) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.WEEK_OF_YEAR , weeks);
		return c.getTime();

	}
	
	public Date addMonths(Date date, int months) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, months);
		return c.getTime();

	}
	
	public int getDayOfMonth(Date d){
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c.get(c.DAY_OF_MONTH);
	}
	
	public String getFormatDate(String pattern){
		return formatDate(new Date(), pattern);
	}
	
	public Date getLatestDateOfMonth(Date date) {
		
		Calendar sc = (Calendar) Calendar.getInstance().clone();
		sc.setTime(date);
		Date rtnDate = null;
		
		int month = sc.get(Calendar.MONTH);
		for (int i = 1; i < 5; i++) {
			sc.set(Calendar.DAY_OF_MONTH, 28 + i);
			if (sc.get(Calendar.MONTH) != month) {
				rtnDate = addDays(sc.getTime(),-1);
				break;
			}
		}
		return rtnDate;
		
	}
	
	public Date getFirstDateOfMonth(Date date){
		
		Calendar sc = (Calendar) Calendar.getInstance().clone();
		sc.setTime(date);
		int dates = sc.get(Calendar.DAY_OF_MONTH);
		sc.add(Calendar.DAY_OF_MONTH, 1-dates) ;
		return sc.getTime();
		
	}
    
    public boolean isLastDateOfMonth(Date date){
        Calendar sc = (Calendar) Calendar.getInstance().clone();
		sc.setTime(date);
        int lastDate = getDayOfMonth(getLatestDateOfMonth(date));
        //System.out.println(lastDate);
        return lastDate == getDayOfMonth(date);
    }
    
    public Date getTWDate(){
    	
    	Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(default_datetime_format);
		sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+8:00"));
		String curDateTime = sdf.format(curDate);
		curDate = DateUtil.getInstance().parseDate(curDateTime, default_datetime_format);
		return curDate;
		
    }
    public String getChineseWeekDay(Date d){
    	
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		String sd = null;
		int dayofweek = c.get(Calendar.DAY_OF_WEEK);
		switch(dayofweek){
			case Calendar.SUNDAY:
				sd = "星期日";
				break;
			case Calendar.MONDAY:
				sd = "星期一";
				break;
			case Calendar.TUESDAY:
				sd = "星期二";
				break;
			case Calendar.WEDNESDAY:
				sd = "星期三";
				break;
			case Calendar.THURSDAY:
				sd = "星期四";
				break;
			case Calendar.FRIDAY:
				sd = "星期五";
				break;
			case Calendar.SATURDAY:
				sd = "星期六";
				break;	
		}
		return sd;
		
    }
	public static void main(String args[]){
		
		
		Date bd = new Date();
		/*
		Date ed = getInstance().addDays(bd,20);
		Date cd = getInstance().addDays(bd,20);
		*/
		System.out.println(getInstance().formatDate(getInstance().getLatestDateOfMonth(bd)));	
		System.out.println(getInstance().formatDate(getInstance().getFirstDateOfMonth(bd)));	
	}
}
