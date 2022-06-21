package com.umc.thingseye2.iotgw.mqtt.packet;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.umc.thingseye2.iotgw.common.constant.CommonMqttControllConstant;
import com.umc.thingseye2.iotgw.common.constant.CommonMqttErrorConstant;
import com.umc.thingseye2.iotgw.common.service.VerifyDataService;
import com.umc.thingseye2.iotgw.common.service.impl.VerifyDataServiceImpl;
import com.umc.thingseye2.iotgw.common.util.CommonJacksonUtil;
import com.umc.thingseye2.iotgw.mqtt.dto.ProtocolDTO;
import com.umc.thingseye2.iotgw.mqtt.exception.UMCException;
import com.umc.thingseye2.iotgw.mqtt.util.MqttAppPaserUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * APP Json data를 가전으로 보낼 byte data로 parsing.
 * @author : JiHwanKang
 * @since : 2019. 3. 14.
 */
@Slf4j
public class AppCommandBytePacket extends AppCommandPacket {
	
	@Override
	public Object getCommandData(byte[] payload, List<ProtocolDTO> protocolInfo) {				
		log.debug("AppCommandBytePacket-getCommandData[start]"); 
		MqttHeaderPacket headerPacket = new MqttHeaderPacket();	
		MqttControlPacket packet = new MqttControlPacket();	
		VerifyDataService verifyDataService = new VerifyDataService(); // data 검증용.
		try {
			
			/**
			 * 요청 내용 header, body로 분류. 
			 * header : appMsg[0]
			 * body : appMsg[1]
			 */
			String stringMsg = new String(payload, "UTF-8");
			String[] appMsg = MqttAppPaserUtil.getJsonData(stringMsg);			
			
			headerPacket = verifyDataService.checkDataNull(MqttHeaderPacket.parseData(appMsg[0]));
			Map<String, Object> data = verifyDataService.checkDataNull(CommonJacksonUtil.objectFromJson(appMsg[1]));
			
			int index = 0;
			ProtocolDTO deviceProtocol = null;
			for(int i = 0, j = protocolInfo.size(); i < j; i++) { //CPU 사용률 저하를 위해.		
				deviceProtocol = protocolInfo.get(i); //JVM memory check 활용을 위해 선언은 loop 밖.				
				if(data.containsKey(deviceProtocol.getFieldPhysicalName())&&CommonMqttControllConstant.ACTION_CODE.equals(deviceProtocol.getFieldPhysicalName())){
					/**
					 * app 명령중 protocol key에 정의되어 있으며, 그 key 값이 actionCode인 경우 app 명령 value 값을 actionCode에 set.
					 */
					packet.setActionCode(Integer.parseInt(deviceProtocol.getProtocolOrder()+data.get(deviceProtocol.getFieldPhysicalName()),16));
				}else if(data.containsKey(deviceProtocol.getFieldPhysicalName())&&!CommonMqttControllConstant.ACTION_CODE.equals(deviceProtocol.getFieldPhysicalName())) {
					/**
					 * app 명령중 protocol key에 정의되어 있으며, 그 key 값이 actionCode가 아닌 경우 app 명령 value 값을 menuCode에 set.
					 */
					packet.setMenuCode(Integer.parseInt(deviceProtocol.getProtocolOrder()+data.get(deviceProtocol.getFieldPhysicalName()),16));					
				}
				index += deviceProtocol.getFieldLength();
			}
			
			commandData = verifyDataService.checkDataNull(packet.makeCmdPacket(headerPacket.getDeviceId(), headerPacket.getVer(), headerPacket.getSeqNum(), String.valueOf(headerPacket.getUserSeq()), headerPacket.getHdType()));			
		} catch (Exception e) { 
			new UMCException(CommonMqttErrorConstant.ERROR_DISPOSEOF_BYTE_PROTOCOL, e);
		}
		log.debug("AppCommandBytePacket-getCommandData[end]");
		return commandData;
	}
}
