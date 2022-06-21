package com.umc.thingseye2.iotgw.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * date 관련 처리를 위한 util.
 * @author : JiHwanKang
 * @since : 2019. 3. 14.
 */
public class CommonDateUtil {

	public static String YYYYMMDD = "yyyyMMdd";
	public static String YYYY = "yyyy";
	public static String MM = "MM";
	public static String DD = "dd";
	public static String YYYYMMDDHHMISS = "yyyyMMddhhmmss";
	
	/**
	 * format에 따라 날짜를 가져온다.
	 * @param format : 날짜 format.
	 * @return : String.
	 */
	public static String getCurrDateToStringFormat(String format) {
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String stringDate = sdf.format(date );
		
		return stringDate;
	}
	
	/**
	 * 이전달의 마지막 날짜를 가져온다.
	 * @param str_date : 날짜.
	 * @return : String.
	 * @throws ParseException 
	 */
	public String lastDayOfPreMonth(String str_date) throws ParseException {
		Date date = null;
		SimpleDateFormat transFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		date = transFormat1.parse(str_date);
		Calendar cal = Calendar.getInstance();
		cal.set(date.getYear(), date.getMonth()-1, 1);
		
		String tmpDate = 
				String.valueOf(cal.get(Calendar.YEAR) + 1900) + "-" + 
				String.valueOf(cal.get(Calendar.MONTH)+1) + "-" +
				String.valueOf(cal.getActualMaximum(Calendar.DATE));
		return tmpDate;
	}

	/**
	 * 현재달의 마지막 날짜를 가져온다.
	 * @param str_date : 날짜.
	 * @return : String.
	 * @throws ParseException
	 */
	public String lastDayOfCurrentMonth(String str_date) throws ParseException {
		Date date = null;
		SimpleDateFormat transFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		date = transFormat1.parse(str_date);
		Calendar cal = Calendar.getInstance();
		cal.set(date.getYear(), date.getMonth(), 1);
		
		String tmpDate = 
				String.valueOf(cal.get(Calendar.YEAR) + 1900) + "-" + 
				String.valueOf(cal.get(Calendar.MONTH)+1) + "-" +
				String.valueOf(cal.getActualMaximum(Calendar.DATE));
		return tmpDate;		
	}

	/**
	 * 다음달의 마지막 날짜를 가져온다.
	 * @param str_date : 날짜.
	 * @return : String.
	 * @throws ParseException 
	 */
	public String lastDayOfNextMonth(String str_date) throws ParseException {
		Date date = null;
		SimpleDateFormat transFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		date = transFormat1.parse(str_date);
		Calendar cal = Calendar.getInstance();
		cal.set(date.getYear(), date.getMonth()+1, 1);

		String tmpDate = 
				String.valueOf(cal.get(Calendar.YEAR) + 1900) + "-" + 
				String.valueOf(cal.get(Calendar.MONTH)+1) + "-" +
				String.valueOf(cal.getActualMaximum(Calendar.DATE));
		return tmpDate;
	}
	
	/**
	 * 현재 날짜를 년/월/일/시/분 24시 기준으로 표현.
	 * @return
	 */
	public static String getYYYYMMDDHH24MI() {
		Date date = Calendar.getInstance().getTime();
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		return format.format(date);
	}
	
	/**
	 * 현재 날짜를 년/월/일/시/분/초 24시 기준으로 표현.
	 * @return :
	 */
	public static String getYYYYMMDDHH24MISS() {
		Date date = Calendar.getInstance().getTime();
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(date);
	}
	
	public static String getYYYYMMDDHH24MI_CAL_MIN(int min) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, min);
		
		Date date = calendar.getTime();
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		return format.format(date);
	}
	
	/**
	 * 현재 날짜를 년/월/일/시 24시 기준으로 표현.
	 * @return
	 */
	public static String getYYYYMMDDHH24() {
		Date date = Calendar.getInstance().getTime();
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyyMMddHH");
		return format.format(date);
	}
	/**
	 * 현재보다 hour시간 뒤 날짜를 년/월/일/시 24시 기준으로 표현.
	 * @param hour : 현재보다 hour시간 뒤 표현시.
	 * @return
	 */
	public static String getYYYYMMDDHH24_CAL_HOUR(int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, hour);
		
		Date date = calendar.getTime();
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyyMMddHH");
		return format.format(date);
	}
	
	
	/**
	 * 현재보다 day일 뒤 년/월/일 형태로 표현 .
	 * @param day : 입력한 day일 뒤 년/월/일.
	 * @return
	 */
	public static String getYYYYMMDD_CAL_DAY(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, day); 
		
		Date date = calendar.getTime();
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyyMMdd");
		return format.format(date);
	}
	
	public static String getYYYY() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 0); 
		
		Date date = calendar.getTime();
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy");
		return format.format(date);
	}
	
	public static String getMM() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 0); 
		
		Date date = calendar.getTime();
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("MM");
		return format.format(date);
	}
	
	public static String getDD() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 0); 
		
		Date date = calendar.getTime();
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("dd");
		return format.format(date);
	}
	
	
	public static Date getDate(String dateyyyymmddhhmmss){
		Date date = null;
		SimpleDateFormat transFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			date = transFormat1.parse(dateyyyymmddhhmmss);
		} catch (ParseException e) {
		}
		return date;
	}
	
	/**
	 * 두 날짜 간격 (dupMonth)를 구하여 true, false를 리턴.
	 * 두 날짜 간격이 dupMonth 보다 클때 : true.
	 * 두 날짜 간격이 dupMonth 보다 작을때 : false.
	 * @param startDate
	 * @param endDate
	 * @param dupMonth
	 * @return
	 */
	public static boolean diffMonth(String startDate , String endDate, int dupMonth){
	
		Date st = getDate(startDate);
		Date end = getDate(endDate);
		
	    Calendar calSt = new GregorianCalendar(Locale.KOREA);
	    calSt.setTime(st);
	    calSt.add(Calendar.MONTH, dupMonth); // 한달을 더한다.
	    calSt.add(Calendar.SECOND, -1); // -1초 더한다.
	    
	    Calendar calEnd = new GregorianCalendar(Locale.KOREA);
	    calEnd.setTime(end);
	    
	     
	    if(calEnd.after(calSt)){
	    	return true; // 이전
	    }else{
	    	return false; // 이후 
	    }
	}
	
	/**
	 * 달력 날짜 연산 Month 추가함. 
	 * @param startDate
	 * @param dupMonth
	 * @return
	 */
	public static Calendar addMonth(String startDate , int dupMonth, int addSecond){
		
		Date st = getDate(startDate);
		
	    Calendar addSt = new GregorianCalendar(Locale.KOREA);
	    addSt.setTime(st);
	    addSt.add(Calendar.MONTH, dupMonth); // 한달을 더한다.
	    addSt.add(Calendar.SECOND, addSecond); // -1초 더한다.

	   return addSt;
	}
		
	/**
	 * Calendar를 날짜 스트링으로 변환 .
	 * @param inputCalendar
	 * @return :
	 */
	public static String calendarToDateFmt(Calendar inputCalendar){
		
		SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMddHHmmss");
		String formatted = dateFmt.format(inputCalendar.getTime());
		
		return formatted;
	}
	
	/**
	 * 입력한 두 날짜 간의 차이 일수로 표현.
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long diffOfDate(String startDate, String endDate) {
		Date startDt = null;
		Date endDt = null;
		long diff = 0;
		long diffDays = 0;
		
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
		try {
			startDt = transFormat.parse(startDate);
			endDt = transFormat.parse(endDate);
			
			diff = endDt.getTime() - startDt.getTime();
			
			diffDays = diff / (24 * 60 * 60 * 1000); // 하루 단위 
			
			
		} catch (ParseException e) {
			
		}
		return diffDays;
	}
	
	/**
	 * 두 날짜 간의 차이 월로 표현 .
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int diffOfMonth(String startDate, String endDate) {
		
		
		int strtYear = Integer.parseInt(startDate.substring(0,4));
		int strtMonth = Integer.parseInt(startDate.substring(4,6));

		int endYear = Integer.parseInt(endDate.substring(0,4));
		int endMonth = Integer.parseInt(endDate.substring(4,6));
		
		int month = (endYear - strtYear)* 12 + (endMonth - strtMonth);

		return month;
	}

	
	public static void main(String[] args) {
		System.out.println("1 ::"+getYYYYMMDD_CAL_DAY(2));
		System.out.println("2 ::"+getYYYYMMDDHH24());
		System.out.println("3 ::"+getYYYYMMDDHH24_CAL_HOUR(-1));
		System.out.println("4 ::"+getYYYYMMDDHH24MI_CAL_MIN(-30));
		System.out.println("5 ::"+getYYYYMMDD_CAL_DAY(-31));
		
		System.out.println("5 ::"+diffMonth("2018010516130000", "2019030516130000", 12));
		System.out.println("5 ::"+diffMonth("2018020516130000", "2019030516130000", 12));
		System.out.println("5 ::"+diffMonth("2018030516130000", "2019030516130000", 12));
		System.out.println("5 ::"+diffMonth("2018030516130001", "2019030516130000", 12));
		System.out.println("5 ::"+diffMonth("2018030516130002", "2019030516130000", 12));
		System.out.println("5 ::"+diffMonth("2018030516130003", "2019030516130000", 12));

		
		String now = CommonDateUtil.getCurrDateToStringFormat(CommonDateUtil.YYYYMMDDHHMISS);
		System.out.println("6 ::"+CommonDateUtil.diffOfMonth(now, "20180420"));
	}

}
