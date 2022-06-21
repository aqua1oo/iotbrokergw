package com.umc.thingseye2.iotgw.mqtt.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.umc.thingseye2.iotgw.common.constant.CommonRedisConstant;

/**
 * app command pasing 관련 util.
 * @author : JiHwanKang
 * @since : 2018. 10. 28.
 */
public class MqttAppPaserUtil {
	
	final static Logger logger = LoggerFactory.getLogger(MqttAppPaserUtil.class);

	/**
	 * protocol header, body 생성 : Json.
	 * @param msg
	 * @return : appMsg
	 */
	public static String[] getJsonData(String msg) {		
		String[] appMsg = new String[2];		
		JsonParser parser = new JsonParser();		
	
		JsonElement headerObejct = parser.parse(msg).getAsJsonObject().get("header");
		appMsg[0] = headerObejct.toString();			
		JsonElement bodyObejct = parser.parse(msg).getAsJsonObject().get("body").getAsJsonObject().get("request");
		appMsg[1] = bodyObejct.toString();	
	
		return appMsg;
	}
	
	/**
	 * 가전 접속 상태 확인.
	 * @param redisKey
	 * @param stringRedisTemplate
	 * @return : check
	 */
	public static boolean checkConnectDevice(String redisKey, StringRedisTemplate stringRedisTemplate) {
		boolean check = false;
			
		List<String> keyList = new ArrayList<>();
		keyList.add(CommonRedisConstant.REDIS_DEVICE_STATUS+redisKey); //가전 상태 데이터
		List<String> dataList = stringRedisTemplate.opsForValue().multiGet(keyList);
		String statusInfo = dataList.get(0);//가전 상태 데이터
		
		if(statusInfo != null) {
			check = true;
		}	
		return check;
	}

}
