package com.wei.springboot.starter.exception;

/**
 * 异常基类 
 * @author Johnson.Jia
 */
public abstract class BaseException extends Exception {

	/**
	 * @author Johnson.Jia
	 */
	private static final long serialVersionUID = -5420974096907472856L;
	
	
	public BaseException() {
		super();
	}

	public BaseException(String err) {
		super(err);
	}

	public abstract String causedBy();


}
