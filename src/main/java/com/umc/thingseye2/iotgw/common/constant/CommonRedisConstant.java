package com.umc.thingseye2.iotgw.common.constant;

/**
 * 
 * redis 관련 constant.
 * @author : JiHwanKang
 * @since : 2018. 10. 31.
 */
public class CommonRedisConstant {
	
	public static final String REDIS_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public final static String REDIS_DEVICE_STATUS = "DEVICE:STATUS:";	//가전 상태
	public final static String REDIS_DEVICE_LOC = "DEVICE:LOC:"; 	    //가전 위치
	public final static String REDIS_DEVICE_REPORT = "DEVICE:REPORT:"; 	//주기보고
	public final static String REDIS_APP_LOC = "APP:LOC:"; 	            //단말 위치

}
