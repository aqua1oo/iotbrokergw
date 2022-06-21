package com.umc.thingseye2.iotgw.mqtt.packet;

import java.util.List;

import com.umc.thingseye2.iotgw.mqtt.dto.ProtocolDTO;

/**
 * app 제어 parsing을 위한 추상 class.
 * @author : JiHwanKang
 * @since : 2019. 3. 14.
 */
public abstract class AppCommandPacket {
	Object commandData;		

	public Object getCommandData(byte[] payload, List<ProtocolDTO> protocolInfo) {
		return commandData;
	}
}
