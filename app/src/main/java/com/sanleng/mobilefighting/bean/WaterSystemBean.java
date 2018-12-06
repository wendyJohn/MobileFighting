package com.sanleng.mobilefighting.bean;

/**
 * 水系统信息
 *
 * @author qiaoshi
 *
 */
public class WaterSystemBean {
	private String title;
	private String w_address;
	private String safetyvalue;
	private String safetyrange;
	private String historicaldata;
	private String taskId;

	private String state;
	private String type;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getW_address() {
		return w_address;
	}

	public void setW_address(String w_address) {
		this.w_address = w_address;
	}

	public String getSafetyvalue() {
		return safetyvalue;
	}

	public void setSafetyvalue(String safetyvalue) {
		this.safetyvalue = safetyvalue;
	}

	public String getSafetyrange() {
		return safetyrange;
	}

	public void setSafetyrange(String safetyrange) {
		this.safetyrange = safetyrange;
	}

	public String getHistoricaldata() {
		return historicaldata;
	}

	public void setHistoricaldata(String historicaldata) {
		this.historicaldata = historicaldata;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}
