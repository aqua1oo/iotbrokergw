package com.umc.thingseye2.iotgw.mqtt.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.umc.thingseye2.iotgw.common.constant.CommonMqttConstant;
import com.umc.thingseye2.iotgw.common.constant.CommonMqttErrorConstant;
import com.umc.thingseye2.iotgw.common.service.impl.VerifyDataServiceImpl;
import com.umc.thingseye2.iotgw.mqtt.exception.UMCException;

import lombok.extern.slf4j.Slf4j;

/**
 * 빅데이터 활용을 위한 데이터 가공 service.
 * @version : $Revision:$
 * @author : JiHwanKang
 * @since : 2019. 3. 6.
 */
@Slf4j
public class MqttBigDataService {
	final static Logger logger = LoggerFactory.getLogger("bigDataLogger"); // logfile 생성을 위해 logger 사용.
	
	public static <T> String writeReportLog(short type, Map<String, Object> reportDataModel) throws UMCException { 		
		log.debug("MqttBigDataService-writeReportLog[start]"); 
		String bigDataLog = "";
		try {
			/**
			 * map에 담겨있는 'key=value|' 형태로 바꾸어 빅데이터용 log로 write.
			 */			
			bigDataLog = reportDataModel.toString().replaceAll("\\,", "|").replaceAll(" ", "").replaceAll("\\{", "").replaceAll("\\}", "");
			logger.info(bigDataLog);
			
		} catch (Exception e) { 
			throw new UMCException(CommonMqttErrorConstant.ERROR_BIGDATA_LOG, e);			
		}	
		log.debug("MqttBigDataService-writeReportLog[end]"); 
		return bigDataLog;
	}
}
