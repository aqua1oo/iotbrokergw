package com.umc.thingseye2.iotgw.mqtt.packet;

import com.umc.thingseye2.iotgw.common.util.CommonByteUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 제어결과 packet.
 * @author : JiHwanKang
 * @since : 2018. 11. 7.
 */
@Slf4j
@Data
public class MqttControlPacket {	
	int actionCode;	//제어종류 시작, 취소, 저장, 삭제, 상태
	int menuCode;	//메뉴코드 
	short payloadLen;	//payload 길이
	byte[] payload;	//payload
	
	@Override
	public String toString() {
		return "ControlResPacket [actionCode=" + actionCode + ", menuCode=" + menuCode + ", payloadLen=" + payloadLen
				+ ", payload=" + payload + "]";
	}
	public String toLog(){
		StringBuilder sb = new StringBuilder();
		sb.append("제어 결과[")
		.append("액션코드=0x").append(CommonByteUtil.ushortToByteArrayString(actionCode, CommonByteUtil.BIG_ENDIAN))
		.append(", 메뉴코드=0x").append(CommonByteUtil.ushortToByteArrayString(menuCode, CommonByteUtil.BIG_ENDIAN));
		if(payloadLen > 0){
			sb.append(", payload=").append("0x"+CommonByteUtil.byteArrayToHex(payload)).append("]");
		}
		
		return sb.toString();
	}
	public byte[] getBytes(){
		int len = 2 + 2;
		if(payload != null)
			payloadLen = (short)payload.length;
			
		if(payloadLen>0)
			len += 2 + payloadLen;

		byte[] packet = new byte[len];
		
		CommonByteUtil.putUShort(packet, 0, actionCode, CommonByteUtil.BIG_ENDIAN);
		CommonByteUtil.putUShort(packet, 2, menuCode, CommonByteUtil.BIG_ENDIAN);
		if(payloadLen>0){
			CommonByteUtil.putShort(packet, 4, payloadLen, CommonByteUtil.LITTLE_ENDIAN);
			System.arraycopy(payload, 0, packet, 6, payloadLen);
		}
		return packet;
	}
	
	public byte[] makeCmdPacket(long deviceId, byte ver, short seqNum, String userId, short applianceType){
		log.debug("packet.ver ===> {}, packet.seqNum ===> {}, packet.userId ===> {}, packet.applianceType ===> {}", ver, seqNum, userId, applianceType);
		MqttHeaderPacket headerPacket = MqttHeaderPacket.defaultHeaderPacket();
		headerPacket.setMagicValue(MqttHeaderPacket.MAGIC_VALUE);				
		headerPacket.setVer(ver);
        headerPacket.setSeqNum(seqNum);
		headerPacket.setCheckSum((byte)0x01);
		headerPacket.setHdType(applianceType);
		headerPacket.setMainType(MqttHeaderPacket.MAIN_TYPE_CONROL);
		headerPacket.setSubType((short)0);
		headerPacket.setDeviceId(deviceId);
		headerPacket.setUserSeq(Long.parseLong(userId));
		headerPacket.setResCd((byte)0x01);
		headerPacket.setCmdSeqNum((byte)0x00);
		
		//body 생성
		
		byte[] body = getBytes();
		
		int checksum = getActionCode() + getMenuCode();
		byte[] triler = new byte[4];
		CommonByteUtil.putInt(triler, 0, checksum, CommonByteUtil.LITTLE_ENDIAN);		
		headerPacket.setTotalLength(MqttHeaderPacket.HEADER_LENGTH - 8 + body.length + 4);
		byte[] header = headerPacket.getBytes();		
		byte[] packet = new byte[header.length + body.length + 4];		
		
		System.arraycopy(header, 0, packet, 0, header.length);		
		System.arraycopy(body, 0, packet, header.length, body.length);		
		System.arraycopy(triler, 0, packet, header.length + body.length, 4);		
		
		log.debug("packet.length ===> {}", packet.length);
		return packet;
	}
	
	public byte[] makeCmdPacket(String deviceId, String userId, short applianceType){
		MqttHeaderPacket headerPacket = MqttHeaderPacket.defaultHeaderPacket();
		headerPacket.setMagicValue(MqttHeaderPacket.MAGIC_VALUE);		
		//전문버전 받아올 것
		headerPacket.setVer((byte)0x00);
		//전문추적 받아올 것
		headerPacket.setSeqNum((short)3);
		headerPacket.setCheckSum((byte)0x01);
		//가전타입 받아올 것
		headerPacket.setHdType(applianceType);
		headerPacket.setMainType(MqttHeaderPacket.MAIN_TYPE_CONROL);
		headerPacket.setSubType((short)0);
		headerPacket.setDeviceId(Long.parseLong(deviceId));
		headerPacket.setUserSeq(Long.parseLong(userId));
		headerPacket.setResCd((byte)0x01);
		headerPacket.setCmdSeqNum((byte)0x00);
		
		//body 생성
		
		byte[] body = getBytes();
		
		int checksum = getActionCode() + getMenuCode();
		byte[] triler = new byte[4];
		CommonByteUtil.putInt(triler, 0, checksum, CommonByteUtil.LITTLE_ENDIAN);		
		headerPacket.setTotalLength(MqttHeaderPacket.HEADER_LENGTH - 8 + body.length + 4);
		byte[] header = headerPacket.getBytes();		
		byte[] packet = new byte[header.length + body.length + 4];		
		
		System.arraycopy(header, 0, packet, 0, header.length);		
		System.arraycopy(body, 0, packet, header.length, body.length);		
		System.arraycopy(triler, 0, packet, header.length + body.length, 4);		
		
		log.debug("packet.length ===> {}", packet.length);
		return packet;
	}
	
	public static MqttControlPacket parseData(byte[] data){
		
		MqttControlPacket packet = new MqttControlPacket();
		
		packet.setActionCode(CommonByteUtil.getUShort(data, 0, CommonByteUtil.BIG_ENDIAN));
		packet.setMenuCode(CommonByteUtil.getUShort(data, 2, CommonByteUtil.BIG_ENDIAN));
		if(data.length > 4){
			packet.setPayloadLen(CommonByteUtil.getShort(data, 4, CommonByteUtil.LITTLE_ENDIAN));
			if(packet.getPayloadLen() > 0){
				byte[] temp = new byte[packet.getPayloadLen()];
				System.arraycopy(data, 6, temp, 0, packet.getPayloadLen());
				packet.setPayload(temp);
			}
		}
		return packet;
	}
}
