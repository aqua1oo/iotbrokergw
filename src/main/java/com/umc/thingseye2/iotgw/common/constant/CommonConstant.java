package com.umc.thingseye2.iotgw.common.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.Message;

import com.umc.thingseye2.iotgw.mqtt.dto.ProtocolDTO;
import com.umc.thingseye2.iotgw.mqtt.packet.MqttHeaderPacket;

/**
 * 일반 사항 관련 constant.
 * @author : JiHwanKang
 * @since : 2019. 3. 6.
 */
public class CommonConstant {
	public static final String _BLANK = "";
	public static final String RES_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * byte[] data 검증 후 return을 위한 변수 정의.
	 */
	public static byte[] byteArray = new byte[10];
	
	/**
	 * object data 검증 후 return을 위한 변수 정의.
	 */
	public static Object object = new Object();
	
	/**
	 * message data 검증 후 return을 위한 변수 정의.
	 */
	public static Message<?> message;
	
	/**
	 * protocol list data 검증 후 return을 위한 변수 정의.
	 */
	public static List<ProtocolDTO> list = new ArrayList<ProtocolDTO>();
	
	/**
	 * map data 검증 후 return을 위한 변수 정의.
	 */
	public static Map<String, Object> map = new HashMap<String, Object>();
	
	/**
	 * header data 검증 후 return을 위한 변수 정의.
	 */
	public static MqttHeaderPacket header = new MqttHeaderPacket();
	
	/**
	 * topic을 구분값('/')으로 나눈 배열의 length 검증을 위한 변수 정의.
	 */
	public static String[] type = new String[8];
	public static int typeArrayLength = 8;
}
