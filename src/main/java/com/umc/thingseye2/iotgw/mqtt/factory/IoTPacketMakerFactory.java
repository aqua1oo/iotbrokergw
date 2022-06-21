package com.umc.thingseye2.iotgw.mqtt.factory;

import com.umc.thingseye2.iotgw.mqtt.exception.UMCException;
import com.umc.thingseye2.iotgw.mqtt.packet.IoTReportPacket;

/**
 * 가전 주기보고 처리 packet을 위한 추상 class. 
 * @author : JiHwanKang
 * @since : 2019. 3. 14.
 */
public abstract class IoTPacketMakerFactory {	
	public abstract IoTReportPacket makeIoTReportPacket(String type) throws UMCException;	
}
