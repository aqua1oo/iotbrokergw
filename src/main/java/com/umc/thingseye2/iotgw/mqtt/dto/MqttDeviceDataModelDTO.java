package com.umc.thingseye2.iotgw.mqtt.dto;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * protocol parsing을 위한 DTO.
 * @author : JiHwanKang
 * @since : 2019. 3. 14.
 */
public class MqttDeviceDataModelDTO {
	public static Map<String, Object> REPORT_DATA_MODEL = new LinkedHashMap<>(); // 주기보고 용.
	public static Map<String, Object> COMMAND_DATA_MODEL = new LinkedHashMap<>(); // 제어요청 용.
}
