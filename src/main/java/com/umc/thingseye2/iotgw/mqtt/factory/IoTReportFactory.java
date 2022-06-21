package com.umc.thingseye2.iotgw.mqtt.factory;

import com.umc.thingseye2.iotgw.common.constant.CommonMqttConstant;
import com.umc.thingseye2.iotgw.common.constant.CommonMqttErrorConstant;
import com.umc.thingseye2.iotgw.mqtt.exception.UMCException;
import com.umc.thingseye2.iotgw.mqtt.packet.IoTReportBytePacket;
import com.umc.thingseye2.iotgw.mqtt.packet.IoTReportJsonPacket;
import com.umc.thingseye2.iotgw.mqtt.packet.IoTReportPacket;

import lombok.extern.slf4j.Slf4j;

/**
 * 가전 주기보고를 처리하기위한 packet 생성 factory.
 * @author : JiHwanKang
 * @since : 2019. 3. 14.
 */
@Slf4j
public class IoTReportFactory extends IoTPacketMakerFactory {
	
	@Override
	public IoTReportPacket makeIoTReportPacket(String type) throws UMCException {
		log.debug("IoTReportFactory-makeIoTReportPacket[start]"); 
		IoTReportPacket iotReportPacket = null;
		try {			
			/**
			 * protocol type에 따른 분기 처리 : byte, Json.
			 */
			switch (type) {
			case CommonMqttConstant.TOPIC_DEPTH5_BYTE:				
				/**
				 * byte 주기보고 처리.
				 */		
				iotReportPacket = new IoTReportBytePacket();				
				break;
			case CommonMqttConstant.TOPIC_DEPTH5_JSON:
				/**
				 * Json 주기보고 처리.
				 */
		        iotReportPacket = new IoTReportJsonPacket();		        
				break;
			}			
						
		} catch (Exception e) {			
			throw new UMCException(CommonMqttErrorConstant.ERROR_DISPOSEOF_IOT_PACKET, e);	
		}		
		log.debug("IoTReportFactory-makeIoTReportPacket[end]"); 
		return iotReportPacket;
	}
}
