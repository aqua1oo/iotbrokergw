package com.umc.thingseye2.iotgw.mqtt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.umc.thingseye2.iotgw.common.constant.CommonConstant;
import com.umc.thingseye2.iotgw.common.constant.CommonMqttErrorConstant;
import com.umc.thingseye2.iotgw.common.service.impl.VerifyDataServiceImpl;
import com.umc.thingseye2.iotgw.common.util.CommonJacksonUtil;
import com.umc.thingseye2.iotgw.mqtt.dto.MqttErrorCodeDTO;
import com.umc.thingseye2.iotgw.mqtt.exception.UMCException;
import com.umc.thingseye2.iotgw.mqtt.packet.MqttErrorCodePacket;

import lombok.extern.slf4j.Slf4j;

/**
 * 다른 G/W로 요청.
 * @author : JiHwanKang
 * @since : 2019. 3. 5.
 */
@Slf4j
@Service
public class MqttSendService {
	
	/**
	 * parameter 검증을 위한 interface
	 */
	@Autowired
	VerifyDataServiceImpl verifyDataServiceImpl;
	
	/**
	 * 스마트 진단 결과를 전달 : api G/W로.
	 * @param deviceId
	 * @param userId
	 * @param errorCode
	 * @return : result
	 * @throws UMCException 
	 */
    public static <T> boolean sendDiagnosisResult(long deviceId, long userId, MqttErrorCodePacket errorCode) throws UMCException {       	
    	log.debug("MqttSendService-sendDiagnosisResult[start]"); 
    	boolean result = false;  	    	
    	MqttErrorCodeDTO errorCodeDto = new MqttErrorCodeDTO();
    	try {
    		/**
    		 * 진단 결과(error) 생성 후 요청 : api G/W로.
    		 */    		
    		errorCodeDto.setUserApplianceId(deviceId);
    		errorCodeDto.setUserId(userId);
    		errorCodeDto.setAlarmStatus(CommonConstant._BLANK); 
    		errorCodeDto.setFaultStatus(errorCode.getCodeValue());
    		
    		HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);			
			HttpEntity<String> entity = new HttpEntity<String>(CommonJacksonUtil.objectToJson(errorCodeDto), headers);
					
			RestTemplate restTemplate = new RestTemplate();
			RestTemplateCustomizer customizers = (template) -> {
				// TODO : option 3초.
			};
			RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder(customizers);
			ResponseEntity<String> response = null;
			/**
			 * dev server.
			 * */			
			response = restTemplate.exchange("http://umcdev3.ubivelox.com/api/device/scan/push", HttpMethod.POST, entity, String.class);						
			if(response.getStatusCode().equals(HttpStatus.OK)) {
				result = true;
			}			
		} catch (Exception e) { 
			throw new UMCException(CommonMqttErrorConstant.ERROR_PROXY_API, e);
		}
    	log.debug("MqttSendService-sendDiagnosisResult[end]"); 
    	return result;
    }
}
