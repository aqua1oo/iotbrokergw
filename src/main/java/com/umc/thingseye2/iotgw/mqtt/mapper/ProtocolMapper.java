package com.umc.thingseye2.iotgw.mqtt.mapper;

import java.util.List;

import com.umc.thingseye2.iotgw.mqtt.dto.ProtocolDTO;
import com.umc.thingseye2.iotgw.mqtt.dto.ProtocolReqDTO;

/**
 * protocol list 조회를 위한 mapper. 
 * @author : JiHwanKang
 * @since : 2019. 3. 14.
 */
public interface ProtocolMapper {
	List<ProtocolDTO> getProtocolList(ProtocolReqDTO req);		
}