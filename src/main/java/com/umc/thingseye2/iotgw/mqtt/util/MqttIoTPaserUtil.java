package com.umc.thingseye2.iotgw.mqtt.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.umc.thingseye2.iotgw.mqtt.packet.MqttHeaderPacket;

/**
 * 가전 주기보고 parsing 관련 util.
 * @author : JiHwanKang
 * @since : 2018. 10. 28.
 */
public class MqttIoTPaserUtil {
	
	final static Logger logger = LoggerFactory.getLogger(MqttIoTPaserUtil.class);
	
	/**
	 * protocol size 비교.
	 * @param dataSize
	 * @param compareSize
	 * @return : check
	 */
	public static boolean checkProtocolSize(int dataSize, int compareSize) {		
		return dataSize == compareSize;
	}
	

	/**
	 * protocol body 생성 : byte.
	 * @param byteMsg
	 * @return : body
	 */
	public static byte[] makeMqttBody(byte[] byteMsg) {
		byte[] body = null;

		int bodyLen = byteMsg.length - MqttHeaderPacket.HEADER_LENGTH;
       	
       	if(bodyLen > 0){
       		body = new byte[bodyLen];
       		System.arraycopy(byteMsg, MqttHeaderPacket.HEADER_LENGTH, body, 0, bodyLen);
       	}
		
		return body;
	}

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

}
