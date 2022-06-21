package com.umc.thingseye2.iotgw.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.umc.thingseye2.iotgw.common.constant.CommonConstant;
import com.umc.thingseye2.iotgw.common.constant.CommonMqttErrorConstant;
import com.umc.thingseye2.iotgw.common.service.impl.VerifyDataServiceImpl;
import com.umc.thingseye2.iotgw.mqtt.dto.ProtocolDTO;
import com.umc.thingseye2.iotgw.mqtt.exception.UMCException;
import com.umc.thingseye2.iotgw.mqtt.packet.MqttHeaderPacket;

/**
 * data 검증을 위한 service.
 * @author : JiHwanKang
 * @since : 2019. 3. 14.
 */
@Service
public class VerifyDataService implements VerifyDataServiceImpl{

	@Override
	public Message<?> checkDataNull(Message<?> message) {				
		try {
			message=(message!=null?message:CommonConstant.message);
		} catch (Exception e) {
			new UMCException(CommonMqttErrorConstant.ERROR_PARAM_NULL, e);
		}
		return message;
	}
	
	@Override
	public String checkDataNull(String data) {	
		try {
			data=(data!=null?data:CommonConstant._BLANK);
		} catch (Exception e) {
			new UMCException(CommonMqttErrorConstant.ERROR_PARAM_NULL, e);
		}
		return data;
	}
	
	@Override
	public byte[] checkDataNull(byte[] data) {	
		try {
			data=(data!=null?data:CommonConstant.byteArray);
		} catch (Exception e) {
			new UMCException(CommonMqttErrorConstant.ERROR_PARAM_NULL, e);
		}
		return data;
	}
	
	@Override
	public Object checkDataNull(Object data) {	
		try {
			data=(data!=null?data:CommonConstant.object);
		} catch (Exception e) {
			new UMCException(CommonMqttErrorConstant.ERROR_PARAM_NULL, e);
		}
		return data;
	}
	
	@Override
	public List<ProtocolDTO> checkDataNull(List<ProtocolDTO> list) {
		try {
			list=(list!=null?list:CommonConstant.list);
		} catch (Exception e) {
			new UMCException(CommonMqttErrorConstant.ERROR_PARAM_NULL, e);
		}
		return list;
	}
	
	@Override
	public Map<String, Object> checkDataNull(Map<String, Object> map) {
		try {
			map=(map!=null?map:CommonConstant.map);
		} catch (Exception e) {
			new UMCException(CommonMqttErrorConstant.ERROR_PARAM_NULL, e);
		}
		return map;
	}
	
	@Override
	public MqttHeaderPacket checkDataNull(MqttHeaderPacket header) {
		try {
			header=(header!=null?header:CommonConstant.header);
		} catch (Exception e) {
			new UMCException(CommonMqttErrorConstant.ERROR_PARAM_NULL, e);
		}
		return header;
	}
	
	@Override
	public String[] checkOutOfLength(String[] type) {	
		try {
			type=(type.length==CommonConstant.typeArrayLength?type:CommonConstant.type);
		} catch (Exception e) {
			new UMCException(CommonMqttErrorConstant.ERROR_PARAM_LENGTH, e);
		}
		return type;
	}

}
