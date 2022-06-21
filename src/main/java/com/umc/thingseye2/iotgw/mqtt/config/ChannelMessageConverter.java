package com.umc.thingseye2.iotgw.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.AbstractIntegrationMessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MessageConversionException;

import com.umc.thingseye2.iotgw.common.constant.CommonMqttConstant;
import com.umc.thingseye2.iotgw.common.constant.CommonMqttErrorConstant;

/**
 * mqtt message 설정.
 * @author : JiHwanKang
 * @since : 2019. 3. 14.
 */
public class ChannelMessageConverter extends DefaultPahoMessageConverter {
	
	String channelName;
	public ChannelMessageConverter(String channelName){
		this.channelName = channelName;
	}
	
	@Override
	public Message<?> toMessage(String topic, MqttMessage mqttMessage) {
		try {			
			AbstractIntegrationMessageBuilder<Object> messageBuilder = getMessageBuilderFactory()
					.withPayload(mqttBytesToPayload(mqttMessage)) // byte 형태로 mqttMessage payload 설정.
					.setHeader(MqttHeaders.QOS, mqttMessage.getQos()) // qos : level 0 -> message 전송 만 수행 (성공 실패 유무 확인 X). level 1 -> 최소 한번의 성공한 message 전송 수행. level 2 -> message가 반드시 한번만 전송되었다는 것을 보장. 
					.setHeader(MqttHeaders.DUPLICATE, mqttMessage.isDuplicate()) // 복제를 통해 message를 생성할 것인지에 대한  true, false.
					.setHeader(MqttHeaders.RETAINED, mqttMessage.isRetained()) // message를 현재 publisher로 유지 할 것인지에 대한 true, false. 
					.setHeader(CommonMqttConstant.MQTT_CHANNEL_NAME_KEY, channelName); // mqtt channel 설정.
			if (topic != null) {
				messageBuilder.setHeader(MqttHeaders.TOPIC, topic);
			}
			return messageBuilder.build();
		}
		catch (Exception e) {
			throw new MessageConversionException(CommonMqttErrorConstant.ERROR_DISPOSEOF_CONVERT_MESSAGE, e);
		}
	}
}
