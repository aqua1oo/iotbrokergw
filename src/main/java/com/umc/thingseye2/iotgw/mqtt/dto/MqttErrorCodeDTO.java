package com.umc.thingseye2.iotgw.mqtt.dto;

import lombok.Data;

/**
 * 스마트 진단 결과를 전송하기 위한 DTO. 
 * @author : JiHwanKang
 * @since : 2019. 3. 14.
 */
@Data
public class MqttErrorCodeDTO {
	private long userId;
	private long userApplianceId;
	private String faultStatus;
	private String alarmStatus;
}
