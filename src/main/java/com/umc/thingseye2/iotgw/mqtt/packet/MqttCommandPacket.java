package com.umc.thingseye2.iotgw.mqtt.packet;

import java.util.Map;

import lombok.Data;

@Data
public class MqttCommandPacket {
	MqttHeaderPacket header;
	Map<String, Object> body;
}
