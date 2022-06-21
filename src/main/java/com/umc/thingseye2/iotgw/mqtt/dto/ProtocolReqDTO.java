package com.umc.thingseye2.iotgw.mqtt.dto;

import lombok.Data;

/**
 * protocol 조회를 위한 parameter DTO.
 * @version : $Revision:$
 * @author : JiHwanKang
 * @since : 2018. 10. 28.
 */
@Data
public class ProtocolReqDTO {	
	private String userApplianceId;	
	private String protocolVersion;
	private String protocolType;
}
