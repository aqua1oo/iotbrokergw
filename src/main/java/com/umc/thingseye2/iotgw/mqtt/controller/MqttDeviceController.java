package com.umc.thingseye2.iotgw.mqtt.controller;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.umc.thingseye2.iotgw.common.constant.CommonMqttConstant;
import com.umc.thingseye2.iotgw.common.constant.CommonMqttErrorConstant;
import com.umc.thingseye2.iotgw.common.constant.CommonRedisConstant;
import com.umc.thingseye2.iotgw.common.service.impl.VerifyDataServiceImpl;
import com.umc.thingseye2.iotgw.common.util.CommonJacksonUtil;
import com.umc.thingseye2.iotgw.mqtt.config.MqttServerConfig;
import com.umc.thingseye2.iotgw.mqtt.config.MqttServerConfig.MqttSendGateway;
import com.umc.thingseye2.iotgw.mqtt.dto.ProtocolDTO;
import com.umc.thingseye2.iotgw.mqtt.dto.ProtocolReqDTO;
import com.umc.thingseye2.iotgw.mqtt.exception.UMCException;
import com.umc.thingseye2.iotgw.mqtt.factory.AppCommandFactory;
import com.umc.thingseye2.iotgw.mqtt.packet.AppCommandPacket;
import com.umc.thingseye2.iotgw.mqtt.service.ProtocolService;
import com.umc.thingseye2.iotgw.mqtt.util.MqttIoTPaserUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 외부 G/W를 통한 command 처리 요청.
 * @author : JiHwanKang
 * @since : 2019. 3. 14.
 */
@Slf4j
@RestController
public class MqttDeviceController {
	
	/**
	 * mqtt send를 위한 interface.
	 */
	@Autowired
	MqttSendGateway mqttSendGateway;

	/**
	 * 가전 protocol 규격 조회를 위한 service.  
	 */
	@Autowired
	ProtocolService protocolService;

	/**
	 * 환경 변수 값을 받기 위한 interface.
	 */
	@Autowired
	Environment environment;

	/**
	 * 가전 상태 정보를 저장하기 위한 redisTemplate.
	 */
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	/**
	 * parameter 검증을 위한 interface
	 */
	@Autowired
	VerifyDataServiceImpl verifyDataServiceImpl;
	
	
	@RequestMapping( method = { RequestMethod.POST }, value = "/command/req")
	public Object requestCommand(@RequestBody String command) throws UMCException {					
		log.debug("MqttDeviceController-requestCommand[start]"); 
		Object dataObj = null;
		AppCommandFactory appCommandFactory = null;		
		try {				
			String topicName = environment.getProperty("mqtt.iot.topic.name");
			
			/**
			 * req header와 body로 pasing.
			 */						
			String[] jsonMsg = MqttIoTPaserUtil.getJsonData(verifyDataServiceImpl.checkDataNull(command));							
			Map<String, Object> headerData = CommonJacksonUtil.objectFromJson(jsonMsg[0]);			      
			String type = verifyDataServiceImpl.checkDataNull((String)headerData.get(CommonMqttConstant.MQTT_TYPE_NAME));
			
			/** 
			 * 가전의 프로토콜 확인.
			 */
			ProtocolReqDTO req = new ProtocolReqDTO();			
			req.setUserApplianceId(String.valueOf(headerData.get(CommonMqttConstant.TOPIC_DEPTH7_IOTDEVICE))); // 프로토콜 조회를 위한 IoTDeviceId.
			req.setProtocolType(type); // 프로토콜 조회를 위한 프로토콜 type : byte / Json.			
			List<ProtocolDTO> protocolInfoList = verifyDataServiceImpl.checkDataNull(protocolService.getProtocol(req)); // protocol list 조회 후 null check.
			
			/**
			 * 가전 제어 요청 처리 : Json 형태의 payload.
			 */	
			appCommandFactory = new AppCommandFactory(); // app 제어 요청 factory.			
			AppCommandPacket appCommandPacket = appCommandFactory.makeAppCommandPacket(type); // type에 따라 factory에서 command packet 결정 : byte 처리용 / Json 처리용.			
			dataObj = verifyDataServiceImpl.checkDataNull(appCommandPacket.getCommandData(command.toString().getBytes("UTF-8"), protocolInfoList)); // app 제어 데이터 parsing -> Object : Json 또는 byte[] 반환.			
			
			/**
			 * 외부 G/W로부터 받은 제어요청을 가전으로 send.
			 */
			String topic = String.format("%s/%s/%s/%s/%s/%s", topicName, headerData.get(CommonMqttConstant.TOPIC_DEPTH3_MANUFACTURER), headerData.get(CommonMqttConstant.TOPIC_DEPTH4_MODEL), type, CommonMqttConstant.TOPIC_DEPTH6_REQ, headerData.get(CommonMqttConstant.TOPIC_DEPTH7_IOTDEVICE));						
			log.debug("topic ===> {}, dataObj ===> {}", topic, dataObj);
			sendMqttServer(topic, "", dataObj);
			
		}catch (Exception e) {
			throw new UMCException(CommonMqttErrorConstant.ERROR_DISPOSEOF_PROTOCOL, e);
		}
		log.debug("MqttDeviceController-requestCommand[end]");
		return dataObj;
	
	}

	/**
	 * mqtt server로 send.
	 * @param topic
	 * @param channelName
	 * @param redisKey
	 * @param payload :
	 */
	public void sendMqttServer(String topic, String channelName, Object payload) {			
		channelName = verifyDataServiceImpl.checkDataNull(MqttServerConfig.outChArr[new Random().nextInt(MqttServerConfig.outChArr.length)]); // channel null check.
		mqttSendGateway(payload, topic, channelName);		
	}
	
	/**
	 * 
	 * 가전 상태정보와 서버정보를 redis에 setting.
	 * 
	 * @param channelName
	 * @param redisKey
	 * @param jsonCommand
	 *            :
	 */
	public void multiSet(String channelName, String redisKey, Object payload) {		
		try {
			stringRedisTemplate.execute(new SessionCallback<List<Object>>() {
				@Override
				public List<Object> execute(RedisOperations operations) throws DataAccessException {
					operations.multi();
					operations.opsForValue().set(CommonRedisConstant.REDIS_DEVICE_STATUS + redisKey, payload, (long) (60 * 1.2), TimeUnit.SECONDS);
					operations.opsForValue().set(CommonRedisConstant.REDIS_DEVICE_LOC + redisKey, channelName, (long) (60 * 1.2), TimeUnit.SECONDS);
					return operations.exec();
				}
				
			});			
		} catch (Exception e) {
			new UMCException(CommonMqttErrorConstant.ERROR_DISPOSEOF_SET_REDIS, e);
		}		
	}

	public void mqttSendGateway(Object payload, String replyTopic, String channelName) {				
		mqttSendGateway.send(MessageBuilder.withPayload(payload).setHeader(MqttHeaders.TOPIC, replyTopic).setHeader(CommonMqttConstant.MQTT_CHANNEL_NAME_KEY, channelName).build());		
	}
}
