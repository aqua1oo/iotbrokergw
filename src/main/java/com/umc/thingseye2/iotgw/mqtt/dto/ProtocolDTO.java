package com.umc.thingseye2.iotgw.mqtt.dto;

import lombok.Data;

/**
 * protocol 조회 후 data DTO.
 * @version : $Revision:$
 * @author : JiHwanKang
 * @since : 2018. 10. 28.
 */
@Data
public class ProtocolDTO {		
	private String fieldPhysicalName;		
	private String protocolOrder;	
	private int fieldLength;
	private String fieldUnit;
}
