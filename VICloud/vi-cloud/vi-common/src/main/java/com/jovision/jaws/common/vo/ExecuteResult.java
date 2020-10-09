package com.jovision.jaws.common.vo;

import com.jovision.jaws.common.exception.BusinessErrorEnum;
import lombok.Data;

/**
 * 通用VO返回包装
 * @param <T>
 *
 * @author : ABug
 * @Date : 2019/5/20 10:01
 * @UpdateDate : 2019/5/20 10:01
 * @Version V1.0.0
 **/
@Data
public class ExecuteResult<T> {

	/**
	 * 状态码 正常:200  其它为异常
	 */
	private int code = 1000;
	/**
	 * 状态码解释信息
	 */
	private String msg = "OK";
	/**
	 * 返回内容
	 */
	private T data = null;

	public ExecuteResult() {
	}

	public ExecuteResult(BusinessErrorEnum businessErrorEnum) {
		this.code = businessErrorEnum.getCode();
		this.msg = businessErrorEnum.getMsg();
	}

	public ExecuteResult(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public void setError(int code , String msg){
		this.code = code;
		this.msg = msg;
	}

	public ExecuteResult(T t){
		this.data = t;
	}
}
