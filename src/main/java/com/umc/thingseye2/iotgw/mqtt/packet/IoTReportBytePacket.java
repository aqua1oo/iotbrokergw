package com.umc.thingseye2.iotgw.mqtt.packet;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.umc.thingseye2.iotgw.common.constant.CommonBigDataConstant;
import com.umc.thingseye2.iotgw.common.constant.CommonMqttErrorConstant;
import com.umc.thingseye2.iotgw.common.constant.CommonMqttReportConstant;
import com.umc.thingseye2.iotgw.common.service.VerifyDataService;
import com.umc.thingseye2.iotgw.common.service.impl.VerifyDataServiceImpl;
import com.umc.thingseye2.iotgw.common.util.CommonByteUtil;
import com.umc.thingseye2.iotgw.common.util.CommonJacksonUtil;
import com.umc.thingseye2.iotgw.mqtt.dto.MqttDeviceDataModelDTO;
import com.umc.thingseye2.iotgw.mqtt.dto.ProtocolDTO;
import com.umc.thingseye2.iotgw.mqtt.exception.UMCException;
import com.umc.thingseye2.iotgw.mqtt.service.MqttBigDataService;
import com.umc.thingseye2.iotgw.mqtt.service.MqttSendService;
import com.umc.thingseye2.iotgw.mqtt.util.MqttIoTPaserUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 가전 byte data를 Json data로 parsing. 
 * @author : JiHwanKang
 * @since : 2019. 3. 13.
 */
@Slf4j
public class IoTReportBytePacket extends IoTReportPacket {

	@Autowired
	MqttSendService mqttSendService;
	
	@Autowired
	MqttBigDataService mqttBigDataService;
	
	@Override
	public String getReportData(byte[] payload, List<ProtocolDTO> protocolInfo) {
		log.debug("IoTReportBytePacket-getReportData[start]"); 
		MqttHeaderPacket headerPacket = new MqttHeaderPacket();
		VerifyDataService verifyDataService = new VerifyDataService(); // data 검증용.
		
		try {			
			/**
			 * 요청 내용 header, body로 분류. 
			 * header : headerPacket
			 * body : bodyData
			 */
			headerPacket = verifyDataService.checkDataNull(MqttHeaderPacket.makeMqttHeader(payload));			
			byte[] data = verifyDataService.checkDataNull(MqttIoTPaserUtil.makeMqttBody(payload));
		
			int size = protocolInfo.stream().mapToInt(ProtocolDTO::getFieldLength).sum(); // packet 길이와 protocol 규격 길이 비교.
			
			/**
			 * 빅데이터용 log를 위한 hdeviceSeq 추가.
			 */
			MqttDeviceDataModelDTO.REPORT_DATA_MODEL.put(CommonBigDataConstant.BIG_DATA_IOTDEVICEID, headerPacket.getDeviceId());
			if (data.length != size) { // 실제 들어온 packet과 protocol 정의 사이즈가 불일치 할 경우
				throw new UMCException(CommonMqttErrorConstant.ERROR_NOT_MATCH_LENGTH, false);							
			} else {
				int index = 0;
				ProtocolDTO deviceProtocol = null;
				for(int i = 0, j = protocolInfo.size(); i < j; i++) { // CPU 사용률 저하.		
					deviceProtocol = protocolInfo.get(i); // JVM memory check 활용을 위해 선언은 loop 밖.
					switch (deviceProtocol.getFieldUnit()) {
					case CommonMqttReportConstant.REPORT_DATA_TYPE_BYTE:
						/**
						 * protocol 길이가 1byte인 경우.  
						 */	
						MqttDeviceDataModelDTO.REPORT_DATA_MODEL.put(deviceProtocol.getFieldPhysicalName(), CommonByteUtil.getUnsignedByte(data[index]));
						index += deviceProtocol.getFieldLength();
						break;
					case CommonMqttReportConstant.REPORT_DATA_TYPE_SHORT:
						/**
						 * protocol 길이가 2byte인 경우.  
						 */						
						MqttDeviceDataModelDTO.REPORT_DATA_MODEL.put(deviceProtocol.getFieldPhysicalName(), CommonByteUtil.getUShort(data, index, CommonMqttReportConstant.REPORT_DATA_ALIGN_BE));							
						index += deviceProtocol.getFieldLength();
						break;
					case CommonMqttReportConstant.REPORT_DATA_TYPE_INT:	
						/**
						 * protocol 길이가 4byte인 경우.  
						 */	
						MqttDeviceDataModelDTO.REPORT_DATA_MODEL.put(deviceProtocol.getFieldPhysicalName(), CommonByteUtil.getInt(data, index, CommonMqttReportConstant.REPORT_DATA_ALIGN_BE));					
						index += deviceProtocol.getFieldLength();
						break;						
					}
				}
				
				/**
				 * 스마트진단 발생시 진단 결과 보고 : app으로 전달.
				 * TODO : 스마트 진단 가능 여부 체크 필요. factory 패턴으로 분류 or composite 패턴. 진행 예정.
				 */
				MqttErrorCodePacket errorCode = MqttErrorCodePacket.makeMqttError((int)MqttDeviceDataModelDTO.REPORT_DATA_MODEL.get(CommonBigDataConstant.BIG_DATA_ERROR), MqttErrorCodePacket.ERROR_DEVICE);								
				if(String.valueOf(MqttDeviceDataModelDTO.REPORT_DATA_MODEL.get(CommonBigDataConstant.BIG_DATA_MENUCODE)).equals(CommonMqttReportConstant.REPORT_CODE_DIAGNOSIS)){												
					boolean resultDiagnosis = mqttSendService.sendDiagnosisResult(headerPacket.getDeviceId(), headerPacket.getUserSeq(), errorCode);
					log.debug("send-diagnosis[result={}]", resultDiagnosis);	        
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
			}		
		} catch (Exception e) {
			new UMCException(CommonMqttErrorConstant.ERROR_DISPOSEOF_BYTE_PROTOCOL, e);
		}
		log.debug("IoTReportBytePacket-getReportData[end]"); 
		return reportData;
	}
}
