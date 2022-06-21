package com.umc.thingseye2.iotgw.common.constant;

/**
 * 제어 관련 constant.
 * @author : JiHwanKang
 * @since : 2018. 10. 30.
 */
public class CommonMqttControllConstant {

	
	public static final String ACTION_CODE = "actionCode";		
	public static final String ACTION_CODE_PREFIX = "0x";
	public static final String ACTION_CODE_START = "ControlStart"; 
	public static final String ACTION_CODE_CANCEL = "2";
	public static final String ACTION_CODE_SAVE = "3";
	public static final String ACTION_CODE_DELETE = "4";
	public static final String ACTION_CODE_STATUS = "STATUS";
	
	/**
	 * 참고용 menucode. 
	 * TODO : 2019.03.20 삭제 예정.
	 */
	/*public static final int ACTION_CODE_START_BYTE = 0x1001; 
	public static final int ACTION_CODE_CANCEL_BYTE = 0x1002;
	public static final int ACTION_CODE_SAVE_BYTE = 0x1003;
	public static final int ACTION_CODE_DELETE_BYTE = 0x1004;
	public static final int ACTION_CODE_STATUS_BYTE = 1;
	
	public static final int[] ACTION_CODE_ARRAY = {ACTION_CODE_START_BYTE,ACTION_CODE_CANCEL_BYTE,ACTION_CODE_SAVE_BYTE,ACTION_CODE_DELETE_BYTE,ACTION_CODE_STATUS_BYTE};*/
		
 	
	/**
	 * 공통 menucode 
	 */	
	public static final String MENU_CODE = "menuCode";	
	
	/**
	 * 참고용 menucode. 
	 * TODO : 2019.03.20 삭제 예정.
	 */
	/*public static final String MENU_CODE_DIAGNOSIS = "diagnosis";
	public static final String MENU_CODE_DIAGNOSIS_ON = "on";	
	public static final int MENU_CODE_DIAGNOSIS_ON_BYTE = 0XFF01;
	public static final String AIR_CONTROLL_POWER = "power";
	public static final String AIR_CONTROLL_POWER_ON = "on";
	public static final String AIR_CONTROLL_POWER_OFF = "off";
	public static final int AIR_CONTROLL_POWER_ON_BYTE = 0x1002;
	public static final int AIR_CONTROLL_POWER_OFF_BYTE = 0x1001;
		
	public static final String AIR_CONTROLL_AUTO = "auto";
	public static final String AIR_CONTROLL_AUTO_ON = "on";
	public static final String AIR_CONTROLL_AUTO_OFF = "off";
	public static final int AIR_CONTROLL_AUTO_ON_BYTE = 0x8002;
	public static final int AIR_CONTROLL_AUTO_OFF_BYTE = 0x8001;
	
	public static final String AIR_CONTROLL_AIRFLOW = "wind";
	public static final String AIR_CONTROLL_AIRFLOW_1 = "1";
	public static final String AIR_CONTROLL_AIRFLOW_2 = "2";
	public static final String AIR_CONTROLL_AIRFLOW_3 = "3";
	public static final String AIR_CONTROLL_AIRFLOW_4 = "4";
	public static final String AIR_CONTROLL_AIRFLOW_TURBO = "turbo";		
	public static final int AIR_CONTROLL_AIRFLOW_1_BYTE = 0x4001;
	public static final int AIR_CONTROLL_AIRFLOW_2_BYTE = 0x4002;
	public static final int AIR_CONTROLL_AIRFLOW_3_BYTE = 0x4003;
	public static final int AIR_CONTROLL_AIRFLOW_4_BYTE = 0x4004;
	public static final int AIR_CONTROLL_AIRFLOW_TURBO_BYTE = 0x40fe;
	
	
	public static final String AIR_CONTROLL_QUIET = "quiet";
	public static final String AIR_CONTROLL_QUIET_ON = "on";
	public static final String AIR_CONTROLL_QUIET_OFF = "off";
	public static final int AIR_CONTROLL_QUIET_ON_BYTE = 0xa002;
	public static final int AIR_CONTROLL_QUIET_OFF_BYTE = 0xa001;
	
	public static final String AIR_CONTROLL_RESERVE = "resvOff";	
	public static final String AIR_CONTROLL_RESERVE_OFF = "0";
	public static final String AIR_CONTROLL_RESERVE_1 = "1";
	public static final String AIR_CONTROLL_RESERVE_2 = "2";
	public static final String AIR_CONTROLL_RESERVE_3 = "3";
	public static final String AIR_CONTROLL_RESERVE_4 = "4";
	public static final String AIR_CONTROLL_RESERVE_5 = "5";
	public static final String AIR_CONTROLL_RESERVE_6 = "6";
	public static final String AIR_CONTROLL_RESERVE_7 = "7";
	public static final String AIR_CONTROLL_RESERVE_8 = "8";
	public static final String AIR_CONTROLL_RESERVE_9 = "9";
	public static final int AIR_CONTROLL_RESERVE_OFF_BYTE = 0x5001;
	public static final int AIR_CONTROLL_RESERVE_1_BYTE = 0x5002;
	public static final int AIR_CONTROLL_RESERVE_2_BYTE = 0x5003;
	public static final int AIR_CONTROLL_RESERVE_3_BYTE = 0x5004;
	public static final int AIR_CONTROLL_RESERVE_4_BYTE = 0x5005;
	public static final int AIR_CONTROLL_RESERVE_5_BYTE = 0x5006;
	public static final int AIR_CONTROLL_RESERVE_6_BYTE = 0x5007;
	public static final int AIR_CONTROLL_RESERVE_7_BYTE = 0x5008;
	public static final int AIR_CONTROLL_RESERVE_8_BYTE = 0x5009;
	public static final int AIR_CONTROLL_RESERVE_9_BYTE = 0x500a;
	
	public static final String AIR_CONTROLL_LOCK = "lock";
	public static final String AIR_CONTROLL_LOCK_ON = "on";
	public static final String AIR_CONTROLL_LOCK_OFF = "off";
	public static final int AIR_CONTROLL_LOCK_ON_BYTE = 0x6002;
	public static final int AIR_CONTROLL_LOCK_OFF_BYTE = 0x6001;
	
	public static final String AIR_CONTROLL_LIGHT = "light";
	public static final String AIR_CONTROLL_LIGHT_ON = "on";
	public static final String AIR_CONTROLL_LIGHT_OFF = "off";
	public static final int AIR_CONTROLL_LIGHT_ON_BYTE = 0xf004;
	public static final int AIR_CONTROLL_LIGHT_OFF_BYTE = 0xf001;*/

}
