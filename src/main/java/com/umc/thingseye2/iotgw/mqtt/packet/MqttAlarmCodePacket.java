package com.umc.thingseye2.iotgw.mqtt.packet;

import java.util.ArrayList;
import java.util.List;

public class MqttAlarmCodePacket {

	public final static String ALARM_DEVICE= "DEVICE_ALARM_";
	
	String prefix;
	int code;
	List<String> codeList = new ArrayList<>();
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public MqttAlarmCodePacket(int code, String prefix) {
		this.prefix = prefix;
		this.code = code;
	}

	public List<String> getCodeList() {
		return codeList;
	}

	public void addErrorIndex(String codeNm){
		codeList.add(codeNm);
	}
	
	public String toBinaryString() {
		StringBuilder sb = new StringBuilder();
		for(int idx=0; idx<8; idx++){
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
	
	public String getCodeName(int index){
		return codeList.get(index);
	}
	
	public int getCodeCount(){
		return codeList.size();
	}
	
	public static MqttAlarmCodePacket parseData(int code, String prefix){
		MqttAlarmCodePacket cp = new MqttAlarmCodePacket(code, prefix);
		for(int idx=0; idx<8; idx++){
			boolean b = (code >> idx & 1) == 1 ? true:false;
			if(b){
				cp.addErrorIndex(cp.getPrefix()+idx);
			}
		}
		return cp;
	}
	
	public static void main(String[] args) {
		
		int error1 = Integer.parseInt("0000000000000001", 2);
		
		MqttAlarmCodePacket cp = MqttAlarmCodePacket.parseData(error1, MqttAlarmCodePacket.ALARM_DEVICE);
		
		System.out.println(cp.toBinaryString());
	}
	
	
}
