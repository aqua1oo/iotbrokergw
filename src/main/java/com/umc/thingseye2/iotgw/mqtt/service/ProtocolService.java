package com.umc.thingseye2.iotgw.mqtt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umc.thingseye2.iotgw.mqtt.dto.ProtocolDTO;
import com.umc.thingseye2.iotgw.mqtt.dto.ProtocolReqDTO;
import com.umc.thingseye2.iotgw.mqtt.mapper.ProtocolMapper;

/**
 * protocol list 조회 service.
 * @author : JiHwanKang
 * @since : 2019. 3. 14.
 */
@Service
public class ProtocolService {

	@Autowired 
	ProtocolMapper protocolMapper;
	
    public List<ProtocolDTO> getProtocol(ProtocolReqDTO req) throws Exception{       	
    	List<ProtocolDTO> protocolList = protocolMapper.getProtocolList(req);    	
    	return protocolList;
    }
}
