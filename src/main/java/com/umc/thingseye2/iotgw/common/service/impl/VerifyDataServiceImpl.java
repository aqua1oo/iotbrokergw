package com.umc.thingseye2.iotgw.common.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.umc.thingseye2.iotgw.mqtt.dto.ProtocolDTO;
import com.umc.thingseye2.iotgw.mqtt.exception.UMCException;
import com.umc.thingseye2.iotgw.mqtt.packet.MqttHeaderPacket;

/**
 * data 검증을 위한 interface.
 * @author : JiHwanKang
 * @since : 2019. 3. 11.
 */
public interface VerifyDataServiceImpl {
	
	/**
	 * mqtt Message null check.
	 * @param message
	 * @return : Message<?>.
	 * @throws UMCException 
	 */
	public Message<?> checkDataNull(Message<?> message);
	
	/**
	 * String null check.
	 * @param data
	 * @return : String.
	 */
	public String checkDataNull(String data);
	
	/**
	 * byte[] null check.
	 * @param data
	 * @return : byte[].
	 */
	public byte[] checkDataNull(byte[] data);
	
	/**
	 * Object null check.
	 * @param data
	 * @return : Object.
	 */
	public Object checkDataNull(Object data);
	
	/**
	 * protocol List null check.
	 * @param list
	 * @return : List<Object>.
	 */
	public List<ProtocolDTO> checkDataNull(List<ProtocolDTO> list);
	
	/**
	 * map null check.
	 * @param MqttHeaderPacket
	 * @return : MqttHeaderPacket.
	 */
	public Map<String, Object> checkDataNull(Map<String, Object> map);
	
	/**
	 * header null check.
	 * @param MqttHeaderPacket
	 * @return : MqttHeaderPacket.
	 */
	public MqttHeaderPacket checkDataNull(MqttHeaderPacket header);
	
	/**
	 * 배열의 out of length check.
	 * @param type
	 * @return : String[].
	 */
	public String[] checkOutOfLength(String[] type);
}
