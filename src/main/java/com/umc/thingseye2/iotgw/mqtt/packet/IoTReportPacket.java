package com.umc.thingseye2.iotgw.mqtt.packet;

import java.util.List;

import com.umc.thingseye2.iotgw.mqtt.dto.ProtocolDTO;

/**
 * 가전 주기보고 parsing을 위한 추상 class. 
 * @author : JiHwanKang
 * @since : 2019. 3. 14.
 */
public abstract class IoTReportPacket {
	String reportData;
	
	public String getReportData(byte[] payload, List<ProtocolDTO> protocolInfo) {
		return reportData;
	}
}
