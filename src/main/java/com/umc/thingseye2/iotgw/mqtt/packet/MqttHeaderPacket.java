package com.umc.thingseye2.iotgw.mqtt.packet;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.thingseye2.iotgw.common.util.CommonByteUtil;

import lombok.Data;

/**
 * 가전 header packet.
 * @author : JiHwanKang
 * @since : 2018. 11. 1.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MqttHeaderPacket {	
	
	final static Logger logger = LoggerFactory.getLogger(MqttHeaderPacket.class);
	
	/** 헤더의 총 크기 */
	public static final int HEADER_LENGTH = 45;

	/** magic value */
	public static final int MAGIC_VALUE = 0x83274812;

	/** 전문 버전 */
	public static final byte VERSION = 1;
	
	/** 로그 상태 */
	public final static String PREFIX_STATS = "STATS";
	
	// 메인 타입
	/** 가전 등록 완료 */
	public static final short MAIN_TYPE_COMPLETE_REGISTRATION = 0x0001;
	/** 가전  정보 조회 */
	public static final short MAIN_TYPE_QUERY_INFORMATION = 0x0002;
	/** 가전 제어 */
	public static final short MAIN_TYPE_CONROL = 0x0003;	
	/** 가전 진단 조회 */
	public static final short MAIN_TYPE_DIAGNOSIS = 0x0004;
	
	//서브 타입
	public static final short SUB_TYPE_NONE = 0;
	/** 공통 상태 조회 */
	public static final short SUB_TYPE_STATUS = 0x1001;
	
	//응답 코드
	public static final byte RES_CD_OK = 0x01;
	public static final byte RES_CD_ERR_UNKNOWN = 0x02;
	public static final byte RES_CD_ERR_NOT_ALLOWED_METHOD = 0x03;
	public static final byte RES_CD_ERR_CHECKSUM = 0x04;
	public static final byte RES_CD_ERR_INDOOR_DUPL_CONN = 0x05;
	public static final byte RES_CD_ERR_AUTH = 0x06;
	public static final byte RES_CD_ERR_DUPLICATION = -1;
	int magicValue;		//magicValue
	int totalLength;	//전문 길이
	byte ver;	        //전문 버전
	short seqNum;	    //sequence number
	byte checkSum;	    //checksum 여부
	short hdType;	    //가전 타입
	short mainType;	    //메인타입
	short subType;	    //서브타입
	long deviceId;	    //가전아이디
	long userSeq;	    //사용자 아이디
	byte resCd;	        //응답코드
	byte cmdSeqNum;
	short firmSeqNum;   //펌웨어 릴리즈 번호(릴리즈 될때마다 순차적으로 증가함)
	short documentVer;  //가전연동 규격서 버전
	
	byte[] reserved = new byte[5];
	
	public byte[] getBytes(){
		byte[] packet = new byte[HEADER_LENGTH];
		
		CommonByteUtil.putInt(packet, 0, magicValue, CommonByteUtil.BIG_ENDIAN);
		CommonByteUtil.putInt(packet, 4, totalLength, CommonByteUtil.LITTLE_ENDIAN);
		packet[8] = ver;
		CommonByteUtil.putShort(packet, 9, seqNum, CommonByteUtil.LITTLE_ENDIAN);
		packet[11] = checkSum;
		CommonByteUtil.putShort(packet, 12, hdType, CommonByteUtil.BIG_ENDIAN);
		CommonByteUtil.putShort(packet, 14, mainType, CommonByteUtil.BIG_ENDIAN);
		CommonByteUtil.putShort(packet, 16, subType, CommonByteUtil.BIG_ENDIAN);
		CommonByteUtil.putLong(packet, 18, deviceId, CommonByteUtil.LITTLE_ENDIAN);
		CommonByteUtil.putLong(packet, 26, userSeq, CommonByteUtil.LITTLE_ENDIAN);
		packet[34] = resCd;
		packet[35] = cmdSeqNum;
		CommonByteUtil.putShort(packet, 36, firmSeqNum, CommonByteUtil.LITTLE_ENDIAN);
		CommonByteUtil.putShort(packet, 38, documentVer, CommonByteUtil.LITTLE_ENDIAN);
		return packet;
	}
	
	public static MqttHeaderPacket makeMqttHeader(byte[] data) {	//TODO : 메서드 명 수정. parseData -> parseHeaderData. 변수 naming DB에서 data 받아 올 것. 	
		MqttHeaderPacket chp = new MqttHeaderPacket();		
		chp.setMagicValue(CommonByteUtil.getInt(data, 0, CommonByteUtil.BIG_ENDIAN));		
		chp.setTotalLength(CommonByteUtil.getInt(data, 4, CommonByteUtil.LITTLE_ENDIAN));		
		chp.setVer(data[8]);		
		chp.setSeqNum(CommonByteUtil.getShort(data, 9, CommonByteUtil.LITTLE_ENDIAN));
		chp.setCheckSum(data[11]);		
		chp.setHdType(CommonByteUtil.getShort(data, 12, CommonByteUtil.BIG_ENDIAN));		
		chp.setMainType(CommonByteUtil.getShort(data, 14, CommonByteUtil.BIG_ENDIAN));		
		chp.setSubType(CommonByteUtil.getShort(data, 16, CommonByteUtil.BIG_ENDIAN));		
		chp.setDeviceId(CommonByteUtil.getLong(data, 18, CommonByteUtil.LITTLE_ENDIAN));		
		chp.setUserSeq(CommonByteUtil.getLong(data, 26, CommonByteUtil.LITTLE_ENDIAN));
		chp.setResCd(data[34]);		
		chp.setCmdSeqNum(data[35]);
		chp.setFirmSeqNum(CommonByteUtil.getShort(data, 36, CommonByteUtil.LITTLE_ENDIAN));
		chp.setDocumentVer(CommonByteUtil.getShort(data, 38, CommonByteUtil.LITTLE_ENDIAN));		
		return chp;
	}
	
	public static MqttHeaderPacket parseData(String data) {		
		ObjectMapper objectMapper = new ObjectMapper();	
		MqttHeaderPacket chp = new MqttHeaderPacket();
		try {
			chp = objectMapper.readValue(data, MqttHeaderPacket.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return chp;
	}
	
	public static MqttHeaderPacket defaultHeaderPacket() {
		MqttHeaderPacket chp = new MqttHeaderPacket();
		chp.setMagicValue(MqttHeaderPacket.MAGIC_VALUE);
		chp.setTotalLength(MqttHeaderPacket.HEADER_LENGTH - 8);
		chp.setVer(MqttHeaderPacket.VERSION);
		chp.setSeqNum((byte)-1);		
		return chp;
	}
	
	public String toDebugLog(){
		StringBuilder builder = new StringBuilder();
		builder.append("[magicValue=0x");
		builder.append(Integer.toHexString(magicValue));
		builder.append(", totalLength=");
		builder.append(totalLength);
		builder.append(", ver=0x");
		builder.append(CommonByteUtil.byteArrayToString(ver));
		builder.append(", seqNum=");
		builder.append(seqNum);
		builder.append(", checkSum=0x");
		builder.append(CommonByteUtil.byteArrayToString(checkSum));
		builder.append(", hdType=0x");
		builder.append(CommonByteUtil.shortToByteArrayString(hdType, CommonByteUtil.BIG_ENDIAN));
		builder.append(", mainType=0x");
		builder.append(CommonByteUtil.shortToByteArrayString(mainType, CommonByteUtil.BIG_ENDIAN));
		builder.append(", subType=0x");
		builder.append(CommonByteUtil.shortToByteArrayString(subType, CommonByteUtil.BIG_ENDIAN));
		builder.append(", deviceId=");
		builder.append(deviceId);
		builder.append(", userSeq=");
		builder.append(userSeq);
		builder.append(", resCd=0x");
		builder.append(CommonByteUtil.byteArrayToString(resCd));
		builder.append(", reserved=0x");
		builder.append(CommonByteUtil.byteArrayToString(reserved));
		builder.append(", firmSeqNum=");
		builder.append(firmSeqNum);
		builder.append(", documentVer=");
		builder.append(documentVer);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("magicValue=");
		builder.append(magicValue);
		builder.append(", totalLength=");
		builder.append(totalLength);
		builder.append(", ver=");
		builder.append(ver);
		builder.append(", seqNum=");
		builder.append(seqNum);
		builder.append(", checkSum=");
		builder.append(checkSum);
		builder.append(", hdType=");
		builder.append(hdType);
		builder.append(", mainType=");
		builder.append(mainType);
		builder.append(", subType=");
		builder.append(subType);
		builder.append(", deviceId=");
		builder.append(deviceId);
		builder.append(", userSeq=");
		builder.append(userSeq);
		builder.append(", resCd=");
		builder.append(resCd);
		builder.append(", reserved=");
		builder.append(Arrays.toString(reserved));		
		return builder.toString();
	}
}
