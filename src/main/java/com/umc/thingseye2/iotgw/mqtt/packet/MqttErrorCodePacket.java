package com.umc.thingseye2.iotgw.mqtt.packet;

/**
 * error 발생 시 생성할 error packet.
 * @author : JiHwanKang
 * @since : 2019. 3. 14.
 */
public class MqttErrorCodePacket {

	public final static String ERROR_DEVICE= "DEVICE_ERROR_";
	
	public final static String ERROR_DEVICE_BASIC = "00000000000000000000000000000000";
	public final static String ERROR_DEVICE_CHECK = "1";
	String prefix;
	int code;
	String codeValue;
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public MqttErrorCodePacket(int code, String prefix) {
		this.prefix = prefix;
		this.code = code;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void addErrorIndex(String codeNm){
		codeValue = codeNm;
	}
	
	public String toBinaryString() {
		StringBuilder sb = new StringBuilder();
		for(int idx=0; idx<32; idx++){
			sb.append(code >> idx & 1);
		}
		return sb.toString();
	}
	
	public String[] toBinaryStringArray() {
		char[] charArray = toBinaryString().toCharArray();
		String[] strArray = new String[charArray.length]; 
		for(int i=0; i<charArray.length; i++){
			strArray[i] = Character.toString(charArray[i]);
		}
		
		return strArray;
	}
	
	public int getCodeCount(){
		return codeValue.length();
	}
	
	public static MqttErrorCodePacket makeMqttError(int code, String prefix){
		MqttErrorCodePacket cp = new MqttErrorCodePacket(code, prefix);
		String value = "";
		for(int idx=0; idx<32; idx++){
			boolean b = (code >> idx & 1) == 1 ? true:false;
			if(b){
				value += "1";
				//cp.addErrorIndex(cp.getPrefix()+idx);				
			}else {
				value += "0";
			}
		}		
		cp.addErrorIndex(value);
		return cp;
	}
	
	public static void main(String[] args) {		
		int error1 = Integer.parseInt("0000000000000001", 2);		
		MqttErrorCodePacket cp = MqttErrorCodePacket.makeMqttError(error1, MqttErrorCodePacket.ERROR_DEVICE);				
	}
	
	
}
