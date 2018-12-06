package com.sanleng.mobilefighting.bean;

/**
 * 整改隐患信息属性
 *
 * @author qiaoshi
 *
 */
public class RectificationBean {
	private String labelnumber;
	private String devicename;
	private String deviceposition;
	private String term;
	private String reorganizer;
	private String describe;
	private String taskId;

	public String getLabelnumber() {
		return labelnumber;
	}

	public void setLabelnumber(String labelnumber) {
		this.labelnumber = labelnumber;
	}

	public String getDevicename() {
		return devicename;
	}

	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}

	public String getDeviceposition() {
		return deviceposition;
	}

	public void setDeviceposition(String deviceposition) {
		this.deviceposition = deviceposition;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getReorganizer() {
		return reorganizer;
	}

	public void setReorganizer(String reorganizer) {
		this.reorganizer = reorganizer;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}
