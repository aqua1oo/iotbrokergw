package com.umc.thingseye2.iotgw.mqtt.exception;

/**
 * exception 기본 설정.
 * @author : JiHwanKang
 * @since : 2019. 3. 13.
 */
public class UMCException extends Exception {

	private static final long serialVersionUID = -1131824703908285531L;
	
	protected String resultCode = ""; // 결과코드
	boolean logWrite = true;
	/**
	 * 생성자.
	 * @param message
	 */
	public UMCException(String resultCode) {
		this.resultCode = resultCode;
	}
	
	/**
	 * 생성자.
	 * @param message
	 */
	public UMCException(String resultCode, boolean logWrite) {
		this.resultCode = resultCode;
		this.logWrite = logWrite;
	}
	
	/**
	 * 생성자.
	 * @param message
	 */
	public UMCException(String resultCode, String message) {
		super(message);
		this.resultCode = resultCode;
	}
	
	/**
	 * 생성자.
	 * @param message
	 */
	public UMCException(String resultCode, Throwable e) {
		super(e);
		this.resultCode = resultCode;
	}

	/**
	 * 생성자.
	 * @param cause
	 */
	public UMCException(Throwable cause) {
		super(cause);
	}

	/**
	 * @return the resultCode
	 */
	public String getResultCode() {
		return resultCode;
	}

	/**
	 * @param resultCode the resultCode to set
	 */
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public boolean isLogWrite() {
		return logWrite;
	}

	public void setLogWrite(boolean logWrite) {
		this.logWrite = logWrite;
	}
	
}
