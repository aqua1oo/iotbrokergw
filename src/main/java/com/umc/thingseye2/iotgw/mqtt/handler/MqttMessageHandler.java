package com.umc.thingseye2.iotgw.mqtt.handler;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageHandler;

import com.umc.thingseye2.iotgw.common.constant.CommonConstant;
import com.umc.thingseye2.iotgw.common.constant.CommonMqttConstant;
import com.umc.thingseye2.iotgw.common.constant.CommonMqttErrorConstant;
import com.umc.thingseye2.iotgw.common.constant.CommonRedisConstant;
import com.umc.thingseye2.iotgw.common.service.impl.VerifyDataServiceImpl;
import com.umc.thingseye2.iotgw.mqtt.config.MqttServerConfig;
import com.umc.thingseye2.iotgw.mqtt.config.MqttServerConfig.MqttSendGateway;
import com.umc.thingseye2.iotgw.mqtt.dto.ProtocolDTO;
import com.umc.thingseye2.iotgw.mqtt.dto.ProtocolReqDTO;
import com.umc.thingseye2.iotgw.mqtt.exception.UMCException;
import com.umc.thingseye2.iotgw.mqtt.factory.AppCommandFactory;
import com.umc.thingseye2.iotgw.mqtt.factory.IoTReportFactory;
import com.umc.thingseye2.iotgw.mqtt.packet.AppCommandPacket;
import com.umc.thingseye2.iotgw.mqtt.packet.IoTReportPacket;
import com.umc.thingseye2.iotgw.mqtt.service.ProtocolService;

import lombok.extern.slf4j.Slf4j;

/**
 * subscribe message 처리를 위한 handler.  
 * @author : JiHwanKang
 * @since : 2018. 10. 28.
 */
@Slf4j
@Configuration
@IntegrationComponentScan
public class MqttMessageHandler {	
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
	VerifyDataServiceImpl verifyDataService;

	/**
	 * private protocol(mqtt) 처리.
	 */
	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler() {		
		return (message) -> {			
		        log.debug("MqttMessageHandler-MessageHandler[start]"); 
			    
				message = verifyDataService.checkDataNull(message); // message null check.
				byte[] byteMsg = verifyDataService.checkDataNull((byte[]) message.getPayload());	
				
				/**
				 * topic : /{topic대표key}/{publisher:iot/app}/{manufacturer}/{model}/{protocolType1:byte/Json}/{protocolType2:req/ack}/{userDeviceId}.
				 * ex)     /thingseye/iot/huming/ha-870/00/123456789.
				 */
				String topic = verifyDataService.checkDataNull(((String) message.getHeaders().get(MqttHeaders.TOPIC)).toLowerCase()); // topic null check.
				
				/**
				 * type을 통해 프로토콜 분기 처리.
				 * type[1] : publisher:iot/app, type[2] : protocolType:byte/Json
				 */
				String[] type = verifyDataService.checkOutOfLength(topic.split("/")); // topic type length check.
				String channelName =  verifyDataService.checkDataNull((String) message.getHeaders().get(CommonMqttConstant.MQTT_CHANNEL_NAME_KEY)); // channel name null check.				
				String redisKey = type[4].toLowerCase() + type[7]; // redis key : modelNm + IoTDeviceId

				try {
					/** 
					 * 가전의 프로토콜 확인.
					 */
					ProtocolReqDTO req = new ProtocolReqDTO();
					req.setUserApplianceId(type[7]); // 프로토콜 조회를 위한 IoTDeviceId.
					req.setProtocolType(type[5]); // 프로토콜 조회를 위한 프로토콜 type : byte / Json.
					List<ProtocolDTO> protocolInfoList = verifyDataService.checkDataNull(protocolService.getProtocol(req)); // protocol list 조회 후 null check.							
					Object dataObj = null;
					
					switch (type[2]) { // iot(가전) / app(앱).
					case CommonMqttConstant.TOPIC_DEPTH2_IOT:
						IoTReportFactory ioTReportFactory = null;
						switch (type[6]) {						
						case CommonMqttConstant.TOPIC_DEPTH6_REQ:
							break;
						case CommonMqttConstant.TOPIC_DEPTH6_ACK:

							/**
							 * protocol 등록 여부 확인.
							 */
							if(protocolInfoList.size() > 0) {
								/**
								 * 가전 주기보고 처리 : byte, Json 형태의 payload.
								 */
								ioTReportFactory = new IoTReportFactory(); // iot 주기보고 처리 factory.
								IoTReportPacket iotReportPacket = ioTReportFactory.makeIoTReportPacket(type[5]); // type에 따라 factory에서 report packet 결정 : byte 처리용 / Json 처리용.
								dataObj = iotReportPacket.getReportData(byteMsg, protocolInfoList); // iot 가전 데이터 parsing -> Json 데이터 get.
								log.debug("dataObj ===> {} ", dataObj);
								/**
								 * 가전으로부터 받은 주기보고를 app으로 send.
								 */
								topic = topic.replaceAll(CommonMqttConstant.TOPIC_CHANGE_IOT, CommonMqttConstant.TOPIC_CHANGE_APP);
								multiSet(channelName, redisKey, (dataObj != CommonConstant._BLANK?dataObj:CommonConstant._BLANK)); // 가전 상태 저장을 위해 redis에 set.
								sendMqttServer(topic, channelName, redisKey, dataObj);
							}else {
								new UMCException(CommonMqttErrorConstant.ERROR_NOT_FOUND_DATA,false);
							}
							break;
						}
						break;
					case CommonMqttConstant.TOPIC_DEPTH2_APP:
						AppCommandFactory appCommandFactory = null;
						switch (type[6]) {
						case CommonMqttConstant.TOPIC_DEPTH6_REQ:												
 
							/**
							 * 가전 제어 요청 처리 : Json 형태의 payload.
							 */	
							appCommandFactory = new AppCommandFactory(); // app 제어 요청 factory.
							AppCommandPacket appCommandPacket = appCommandFactory.makeAppCommandPacket(type[5]); // type에 따라 factory에서 command packet 결정 : byte 처리용 / Json 처리용.						
							dataObj = appCommandPacket.getCommandData(byteMsg, protocolInfoList); // app 제어 데이터 parsing -> Object : Json 또는 byte[] 반환.
							log.debug("dataObj ===> {} ", dataObj);
							/**
							 * app으로부터 받은 제어를 가전으로 send.
							 */
							topic = topic.replaceAll(CommonMqttConstant.TOPIC_CHANGE_APP, CommonMqttConstant.TOPIC_CHANGE_IOT);
							sendMqttServer(topic, channelName, redisKey, dataObj);								
							break;													
						case CommonMqttConstant.TOPIC_DEPTH6_ACK:
							break;
						}
					}

				} catch (DataAccessException e) {					
					new UMCException(CommonMqttErrorConstant.ERROR_DISPOSEOF_DB, e);
				} catch (Exception e) {					
					new UMCException(CommonMqttErrorConstant.ERROR_DISPOSEOF_PROTOCOL, e);
				}
				log.debug("MqttMessageHandler-MessageHandler[end]"); 
		};			
	}


	/**
	 * mqtt server로 send.
	 * @param topic
	 * @param channelName
	 * @param redisKey
	 * @param payload :
	 */
	public void sendMqttServer(String topic, String channelName, String redisKey, Object payload) {				
		/**
		 * 가전 상태 및 접속 channel 정보 등록.
		 */
		channelName = stringRedisTemplate.opsForValue().get(CommonRedisConstant.REDIS_APP_LOC + redisKey);
		channelName = (channelName!=null?channelName:MqttServerConfig.outChArr[new Random().nextInt(MqttServerConfig.outChArr.length)]); // channel null check.

		// App 접속 정보는 복수가 있을 수 있기때문에 | 구분자로 스플릿하여, 그 수만큼 메시지 전달
		if (channelName.indexOf("|") > 0) {
			String[] temp = channelName.split("[|]");
			for (String ch : temp) {
				mqttSendGateway(payload, topic, ch);
			}
		} else {
			mqttSendGateway(payload, topic, channelName);
		}		
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
	    	/**
	    	 * redisTemplate을 통하여 data를 redis에 저장.
	    	 */
	    	stringRedisTemplate.execute(new SessionCallback<List<Object>>() {
	    		@Override
	    		public List<Object> execute(RedisOperations operations) throws DataAccessException {
	    			operations.multi();
	    			operations.opsForValue().set(CommonRedisConstant.REDIS_DEVICE_STATUS + redisKey, payload, (long) (60 * 1.2), TimeUnit.SECONDS); // IoTDevice의 현재 상태 값 저장.
	    			operations.opsForValue().set(CommonRedisConstant.REDIS_DEVICE_LOC + redisKey, channelName, (long) (60 * 1.2), TimeUnit.SECONDS); // IoTDevice의 현재 channel 저장.
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
