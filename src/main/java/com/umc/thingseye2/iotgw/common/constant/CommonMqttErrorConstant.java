package com.umc.thingseye2.iotgw.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 프로토콜 처리중 발생 가능한 error 정의. 
 * @author : JiHwanKang
 * @since : 2019. 3. 5.
 */
public class CommonMqttErrorConstant {
	
	public static final String ERROR_DISPOSEOF_PROTOCOL = "10000";
	public static final String ERROR_DISPOSEOF_BYTE_PROTOCOL = "10001";
	public static final String ERROR_DISPOSEOF_JSON_PROTOCOL = "10002";
	public static final String ERROR_DISPOSEOF_IOT_PACKET = "10003";
	public static final String ERROR_DISPOSEOF_SET_REDIS = "10004";
	public static final String ERROR_DISPOSEOF_CONVERT_MESSAGE = "10005";
	public static final String ERROR_DISPOSEOF_CREATE_FACTORY = "10006";
	public static final String ERROR_DISPOSEOF_DB = "10007";
	public static final String ERROR_NOT_USE_IOTDEVICE = "20000";
	public static final String ERROR_NOT_MATCH_LENGTH = "20001";
	public static final String ERROR_NOT_FOUND_DATA = "20002";	
	public static final String ERROR_PARAM_NULL = "30000";
	public static final String ERROR_PARAM_LENGTH = "30001";
	public static final String ERROR_DISCONNECTED_IOTDEVICE = "40000";
	public static final String ERROR_SEND_DIAGNOSIS = "50000";
	public static final String ERROR_PROXY_API = "50001";
	public static final String ERROR_BIGDATA_LOG = "60000";

	public static final Map<String, String> ERROR_CODE_DEFINE = new HashMap<String, String>();
	static {
		ERROR_CODE_DEFINE.put("10000", "ERROR_DISPOSEOF_PROTOCOL");
		ERROR_CODE_DEFINE.put("10001", "ERROR_DISPOSEOF_BYTE_PROTOCOL");
		ERROR_CODE_DEFINE.put("10002", "ERROR_DISPOSEOF_JSON_PROTOCOL");
		ERROR_CODE_DEFINE.put("10003", "ERROR_DISPOSEOF_IOT_PACKET");
		ERROR_CODE_DEFINE.put("10004", "ERROR_DISPOSEOF_SET_REDIS");
		ERROR_CODE_DEFINE.put("10005", "ERROR_DISPOSEOF_CONVERT_MESSAGE");
		ERROR_CODE_DEFINE.put("10006", "ERROR_DISPOSEOF_CREATE_FACTORY");
		ERROR_CODE_DEFINE.put("10007", "ERROR_DISPOSEOF_DB");
		ERROR_CODE_DEFINE.put("20000", "ERROR_NOT_USE_IOTDEVICE");
		ERROR_CODE_DEFINE.put("20001", "ERROR_NOT_MATCH_LENGTH");
		ERROR_CODE_DEFINE.put("20002", "ERROR_NOT_FOUND_DATA");
		ERROR_CODE_DEFINE.put("30000", "ERROR_PARAM_NULL");
		ERROR_CODE_DEFINE.put("30001", "ERROR_PARAM_LENGTH");
		ERROR_CODE_DEFINE.put("40000", "ERROR_DISCONNECTED_IOTDEVICE");
		ERROR_CODE_DEFINE.put("50000", "ERROR_SEND_DIAGNOSIS");
		ERROR_CODE_DEFINE.put("50001", "ERROR_PROXY_API");
	}
	
	public static final String ERROR_NOT_FOUND_COMMAND = "401";
	public static final String ERROR_NOT_FOUND_IOTDEVICE = "402";
}
