package com.umc.thingseye2.iotgw.mqtt.factory;

import com.umc.thingseye2.iotgw.common.constant.CommonMqttConstant;
import com.umc.thingseye2.iotgw.common.constant.CommonMqttErrorConstant;
import com.umc.thingseye2.iotgw.mqtt.exception.UMCException;
import com.umc.thingseye2.iotgw.mqtt.packet.AppCommandBytePacket;
import com.umc.thingseye2.iotgw.mqtt.packet.AppCommandJsonPacket;
import com.umc.thingseye2.iotgw.mqtt.packet.AppCommandPacket;

import lombok.extern.slf4j.Slf4j;

/**
 * app command를 처리하기위한 packet 생성 factory.
 * @author : JiHwanKang
 * @since : 2019. 3. 14.
 */
@Slf4j
public class AppCommandFactory extends AppPacketMakerFactory {
	
	@Override
	public AppCommandPacket makeAppCommandPacket(String type) {
		
		AppCommandPacket appCommandPacket = null;									
		try {			
			/**
			 * protocol type에 따른 분기 처리 : byte, Json.
			 */
			switch (type) { // byte / Json. factory 패턴 사용 할 것.
			case CommonMqttConstant.TOPIC_DEPTH5_BYTE:			
				/**
				 * 주기보고 처리 : byte layload.
				 */								
				appCommandPacket = new AppCommandBytePacket();				
				break;
			case CommonMqttConstant.TOPIC_DEPTH5_JSON:
				/**
				 * 주기보고 처리 : Json layload.
				 */		        
				appCommandPacket = new AppCommandJsonPacket();		        
				break;
			}			
						
		} catch (Exception e) {			
			new UMCException(CommonMqttErrorConstant.ERROR_DISPOSEOF_CREATE_FACTORY, e);
		}			
		return appCommandPacket;
	}
}
