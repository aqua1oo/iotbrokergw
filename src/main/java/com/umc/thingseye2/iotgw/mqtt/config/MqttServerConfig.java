package com.umc.thingseye2.iotgw.mqtt.config;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.umc.thingseye2.iotgw.common.constant.CommonConstant;
import com.umc.thingseye2.iotgw.common.constant.CommonMqttConstant;
import com.umc.thingseye2.iotgw.common.service.impl.VerifyDataServiceImpl;

/**
 * 
 * 
 * @version : $Revision:$
 * @author : JiHwanKang
 * @since : 2018. 10. 30.
 */
@Configuration
@IntegrationComponentScan
public class MqttServerConfig {
	final static Logger logger = LoggerFactory.getLogger(MqttServerConfig.class);
	final static String _UUID = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);

	@Autowired
	Environment environment;
	
	@Autowired
	VerifyDataServiceImpl verifyDataServiceImpl;

	/**
	 * channel 구분을 위한 naming key.
	 * channel 구분을 하지 않을 시, mqtt 상의 모든 channel에 발행하여 문제 발생.
	 */
	public final static String[] outChArr = {CommonMqttConstant.MQTT_CHANNEL_NAME};

	/**
	 * Message 전달을 위한 channel을 bean으로 등록.
	 */
	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel(); //round robin 전략에 의한 message 전달용.
	}

	@Bean
	public MessageChannel mqttOutboundChannel() {
		return new PublishSubscribeChannel();
	}

	/**
	 * mqtt client option 설정. 
	 * - cleanSession : true로 설정시 client가 연결시 이전 구독 제거. 
	 * - serverURI : mqtt server urls. 
	 * - userName : mqtt 설정 user. 
	 * - password : mqtt 설정 user의 password. 
	 */
	public MqttPahoClientFactory mqttClientFactory() {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(true); // true 설정하여 이전 session 지우고 연결하도록 처리. 20190306. JiHwanKang.
		options.setServerURIs(new String[] { environment.getProperty("mqtt.tcp.urls") });
		options.setUserName(environment.getProperty("mqtt.auth.username"));
		options.setPassword(environment.getProperty("mqtt.auth.password").toCharArray());

		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setConnectionOptions(options);
		factory.setPersistence(new MemoryPersistence()); // client or device에 직접 관여없이 memory를 이용하여 지속.
		return factory;
	}

	/**
	 * MQTT message 설정.
	 * - converter : 입력한 name에 따라 channel 변경. 
	 * - payloadAsBytes : true, false 값에 따라 byte를 String으로 변환 여부 설정.
	 * - adapter : clientId, 설정된 mqttClientFactory 정보를 통해 mqtt 연결 adapter 생성.
	 * - completionTimeout : 작업 완료 시간 설정.
	 * - qos : level 0 -> message 전송 만 수행 (성공 실패 유무 확인 X). level 1 -> 최소 한번의 성공한 message 전송 수행. level 2 -> message가 반드시 한번만 전송되었다는 것을 보장. 
	 */
	@Bean
	public MessageProducer setMqttMessage() {
		String topicName = environment.getProperty("mqtt.topic.name");
		/**
		 * topic 변경(프로토콜 타입 추가)으로 인한 주석처리 : ex) /thingseye/iot/daeyoung/ha-830/00/ack/1234567948. 프로토콜 타입 : 00(byte), 01(json).
		 */
		String topic = String.format("%s/%s/%s/%s/%s/%s/%s", topicName, "+", "+", "+", "+", "+", "+");
		String clientId = MqttAsyncClient.generateClientId();
		DefaultPahoMessageConverter converter = new ChannelMessageConverter(CommonMqttConstant.MQTT_CHANNEL_NAME);
        converter.setPayloadAsBytes(true); // true 일 경우 String으로 convert 하지 않음. 
		
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId, mqttClientFactory(), topic);
		adapter.setCompletionTimeout(30000); // 작업 완료 시간을 default로 설정.
		adapter.setConverter(converter);
		adapter.setQos(0);
		adapter.setOutputChannel(mqttInputChannel());
		adapter.setRecoveryInterval(1000); // mqtt 연결이 끊겼을 시, 1초 후 재연결 시도.
		return adapter;
	}

	/**
	 * message handling을 위한 handler 설정.
	 * @return :
	 */
	@Bean
	@ServiceActivator(inputChannel = CommonMqttConstant.MQTT_OUTBOUND_CHANNEL_NAME+CommonMqttConstant.MQTT_SPLIT_CHANNEL_NAME+CommonMqttConstant.MQTT_CHANNEL_NAME)
	public MessageHandler setMqttOutboundHandler() {
		/**
		 * 설정한 factory를 통해 message handler setting.
		 */
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(CommonMqttConstant.MQTT_OUTBOUND_PUBLISH_CLIENT_NAME+CommonMqttConstant.MQTT_SPLIT_CHANNEL_NAME+CommonMqttConstant.MQTT_CHANNEL_NAME+CommonMqttConstant.MQTT_SPLIT_CHANNEL_NAME+_UUID, mqttClientFactory());
		messageHandler.setAsync(true);
		return messageHandler;
	}


	/**
	 * mqtt message send를 위한 interface.
	 */
	@MessagingGateway(defaultRequestChannel = CommonMqttConstant.MQTT_OUTBOUND_CHANNEL_NAME)
	public interface MqttSendGateway {
		void send(Message<?> data);
	}

	/**
	 * message를 특정 channel로 분배.
	 */
	@Router(inputChannel = "mqttOutboundChannel")
	public String routerMqttChannel(Message<?> message) {		
		String channelName = CommonConstant._BLANK;
		message = verifyDataServiceImpl.checkDataNull(message);
		channelName = verifyDataServiceImpl.checkDataNull((String)message.getHeaders().get(CommonMqttConstant.MQTT_CHANNEL_NAME_KEY));
		return CommonMqttConstant.MQTT_OUTBOUND_CHANNEL_NAME+CommonMqttConstant.MQTT_SPLIT_CHANNEL_NAME+channelName;
	}

}
