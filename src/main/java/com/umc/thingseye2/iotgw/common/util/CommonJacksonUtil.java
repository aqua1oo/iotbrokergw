package com.umc.thingseye2.iotgw.common.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * jackson 관련 util. 
 * @author : JiHwanKang
 * @since : 2019. 3. 6.
 */
public class CommonJacksonUtil {
	
	/**
	 * object를 Json으로 변환.
	 * @param obj
	 * @return : objJson
	 */
	public static String objectToJson(Object obj) {	
		ObjectMapper mapper = new ObjectMapper();
		String objJson = "";
		try {			
			objJson = mapper.writeValueAsString(obj);			
		} catch (Exception e) {

		}		
		return objJson;
	}
	
	/**
	 * Json을 object로 변환.
	 * @param obj
	 * @return : objJson
	 */
	public static Map<String, Object> objectFromJson(String obj) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> objJson = new HashMap<String, Object>();
		try {
			objJson = mapper.readValue(obj, Map.class);
		} catch (Exception e) {
		}		
		return objJson;
	}
}
