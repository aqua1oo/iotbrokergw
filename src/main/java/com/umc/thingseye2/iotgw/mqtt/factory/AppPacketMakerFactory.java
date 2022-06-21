package com.umc.thingseye2.iotgw.mqtt.factory;

import com.umc.thingseye2.iotgw.mqtt.packet.AppCommandPacket;

/**
 * app command 처리 packet을 위한 추상 class.
 * @author : JiHwanKang
 * @since : 2019. 3. 14.
 */
public abstract class AppPacketMakerFactory {	
	public abstract AppCommandPacket makeAppCommandPacket(String type);
}
