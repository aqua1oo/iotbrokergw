package com.umc.thingseye2.iotgw.mqtt.packet;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.umc.thingseye2.iotgw.common.constant.CommonBigDataConstant;
import com.umc.thingseye2.iotgw.common.constant.CommonMqttErrorConstant;
import com.umc.thingseye2.iotgw.common.constant.CommonMqttReportConstant;
import com.umc.thingseye2.iotgw.common.service.VerifyDataService;
import com.umc.thingseye2.iotgw.common.service.impl.VerifyDataServiceImpl;
import com.umc.thingseye2.iotgw.common.util.CommonJacksonUtil;
import com.umc.thingseye2.iotgw.mqtt.dto.MqttDeviceDataModelDTO;
import com.umc.thingseye2.iotgw.mqtt.dto.ProtocolDTO;
import com.umc.thingseye2.iotgw.mqtt.exception.UMCException;
import com.umc.thingseye2.iotgw.mqtt.service.MqttBigDataService;
import com.umc.thingseye2.iotgw.mqtt.service.MqttSendService;
import com.umc.thingseye2.iotgw.mqtt.util.MqttIoTPaserUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 가전 Json data를 Json data로 parsing. 
 * @author : JiHwanKang
 * @since : 2019. 3. 13.
 */
@Slf4j
public class IoTReportJsonPacket extends IoTReportPacket {
	
	@Autowired
	MqttSendService mqttSendService;
	
	@Autowired
	MqttBigDataService mqttBigDataService;
	
	@Override
	public String getReportData(byte[] payload, List<ProtocolDTO> protocolInfo) {
		log.debug("IoTReportJsonPacket-getReportData[start]"); 
		MqttHeaderPacket headerPacket = new MqttHeaderPacket();
		VerifyDataService verifyDataService = new VerifyDataService(); // data 검증용.
		try {
			
			/**
			 * 요청 내용 header, body로 분류. 
			 * header : jsonMsg[0]
			 * body : jsonMsg[1]
			 */
			String stringMsg = new String(payload, "UTF-8");				
			String[] jsonMsg = MqttIoTPaserUtil.getJsonData(stringMsg);				
			headerPacket = verifyDataService.checkDataNull(MqttHeaderPacket.parseData(jsonMsg[0]));												
	        Map<String, Object> data = verifyDataService.checkDataNull(CommonJacksonUtil.objectFromJson(jsonMsg[1]));
	        
			/**
			 * 빅데이터용 log를 위한 hdeviceSeq 추가.
			 */
			MqttDeviceDataModelDTO.REPORT_DATA_MODEL.put(CommonBigDataConstant.BIG_DATA_IOTDEVICEID, headerPacket.getDeviceId());
			int index = 0;
			ProtocolDTO deviceProtocol = null;
			for(int i = 0, j = protocolInfo.size(); i < j; i++) { // CPU 사용률 저하.	
				deviceProtocol = protocolInfo.get(i);
				/**
				 * payload의 key 값과 protocol 규격 key를 비교하여 protocol 규격에 선언된 key와 value만 map에 담음. 
				 */
				if(data.containsKey(deviceProtocol.getFieldPhysicalName())){
					MqttDeviceDataModelDTO.REPORT_DATA_MODEL.put(deviceProtocol.getFieldPhysicalName(), data.get(deviceProtocol.getFieldPhysicalName()));
				}
				index += deviceProtocol.getFieldLength();					
			}
			
			/**
			 * 스마트진단 발생시 진단 결과 보고 : app으로 전달.
			 * TODO : 스마트 진단 가능 여부 체크 필요. factory 패턴으로 분류 or composite 패턴. 진행 예정.
			 */
			MqttErrorCodePacket errorCode = MqttErrorCodePacket.makeMqttError((int)MqttDeviceDataModelDTO.REPORT_DATA_MODEL.get(CommonBigDataConstant.BIG_DATA_ERROR), MqttErrorCodePacket.ERROR_DEVICE);								
			if(String.valueOf(MqttDeviceDataModelDTO.REPORT_DATA_MODEL.get(CommonBigDataConstant.BIG_DATA_MENUCODE)).equals(CommonMqttReportConstant.REPORT_CODE_DIAGNOSIS)){												
				boolean resultDiagnosis = mqttSendService.sendDiagnosisResult(headerPacket.getDeviceId(), headerPacket.getUserSeq(), errorCode);						       
			}			
			
			/**
			 * 빅데이터용 가전 주기 보고 log.
			 */				
			String resultBigdataLog = mqttBigDataService.writeReportLog(headerPacket.getHdType(), verifyDataService.checkDataNull(MqttDeviceDataModelDTO.REPORT_DATA_MODEL));		
			
			/**
			 * app요청 내용을 주기 보고에 추가.
			 */				
			MqttDeviceDataModelDTO.REPORT_DATA_MODEL.put(CommonBigDataConstant.BIG_DATA_REMOVE_MAGICVALUE, headerPacket.getMagicValue());
			MqttDeviceDataModelDTO.REPORT_DATA_MODEL.put(CommonBigDataConstant.BIG_DATA_REMOVE_SEQNUM, headerPacket.getSeqNum());
			
			reportData = verifyDataService.checkDataNull(CommonJacksonUtil.objectToJson(MqttDeviceDataModelDTO.REPORT_DATA_MODEL));
		} catch (Exception e) {
			new UMCException(CommonMqttErrorConstant.ERROR_DISPOSEOF_JSON_PROTOCOL, e);
		}
		log.debug("IoTReportJsonPacket-getReportData[end]"); 
		return reportData;
	}
}
