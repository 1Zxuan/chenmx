package com.louzx.yueqowu.utils;


import com.alibaba.fastjson.JSONObject;
import com.louzx.yueqowu.constants.Constant;

public class AjaxResult {

	private int code;
	private String msg;
	private int count;
	private Object data;
	
	private AjaxResult(int code, String msg){
		this.code = code;
		this.msg = msg;
	}
	
	private AjaxResult(int code, String msg, Object data){
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	private AjaxResult(int code, String msg, int count, Object data){
		this.code = code;
		this.msg = msg;
		this.count = count;
		this.data = data;
	}
	
	public static AjaxResult success(String msg){
		return new AjaxResult(0, null == msg ? Constant.retMsg.success : msg);
	}
	
	public static AjaxResult success(String msg, Object data){
		return new AjaxResult(0, null == msg ? Constant.retMsg.success : msg, data);
	}
	
	public static AjaxResult success(String msg, int count, Object data){
		return new AjaxResult(0, null == msg ? Constant.retMsg.success : msg, count, data);
	}

	public static AjaxResult success(Object data){
		return new AjaxResult(0,Constant.retMsg.success,data);
	}

	public static AjaxResult success(int count, Object data){
		return new AjaxResult(0,Constant.retMsg.success,count,data);
	}
	
	public static AjaxResult error(String msg){
		return new AjaxResult(-1, null == msg ? Constant.retMsg.error : msg);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	@Override
	public String toString(){
		return JSONObject.toJSONString(this);
	}

}
