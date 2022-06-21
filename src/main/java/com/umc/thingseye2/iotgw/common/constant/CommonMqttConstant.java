package com.umc.thingseye2.iotgw.common.constant;

/**
 * MQTT 공통 관련 constant.
 * @author : JiHwanKang
 * @since : 2018. 10. 30.
 */
public class CommonMqttConstant {

	/**
	 * mqtt 공통 명칭 정의.
	 */
	public static final String MQTT_SPLIT_CHANNEL_NAME = "-";
	public static final String MQTT_CHANNEL_NAME = "thingseye";
	public static final String MQTT_CHANNEL_NAME_KEY = "channelName";
	public static final String MQTT_OUTBOUND_CHANNEL_NAME = "mqttOutboundChannel";
	public static final String MQTT_OUTBOUND_PUBLISH_CLIENT_NAME = "springPubClient";
	public static final String MQTT_TYPE_NAME = "type";
	
	public static final String TOPIC_DEPTH1 = "thingseye";
	public static final String TOPIC_DEPTH2_IOT = "iot";
	public static final String TOPIC_DEPTH2_APP = "app";
	public static final String TOPIC_DEPTH3_MANUFACTURER = "manufacturer";
	public static final String TOPIC_DEPTH4_MODEL = "modelNm";
	public static final String TOPIC_DEPTH5_BYTE = "00";
	public static final String TOPIC_DEPTH5_JSON = "01";
	public static final String TOPIC_DEPTH6_REQ = "req";
	public static final String TOPIC_DEPTH6_ACK = "ack";
	public static final String TOPIC_DEPTH7_IOTDEVICE = "ioTDeviceId";
	
	public static final String TOPIC_CHANGE_IOT = "/iot/";
	public static final String TOPIC_CHANGE_APP = "/app/";
	
	public static final short DEVICE_TYPE_AIR = 257;

}
