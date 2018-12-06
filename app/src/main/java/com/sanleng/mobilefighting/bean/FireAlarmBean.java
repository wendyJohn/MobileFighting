package com.sanleng.mobilefighting.bean;

import java.io.Serializable;

/**
 * 火警信息
 *
 * @author qiaoshi
 *
 */
public class FireAlarmBean implements Serializable {
	private static final long serialVersionUID = 12561545625L;

	private String alarmTime;
	private String alarmEquipment;
	private String alarmPosition;
	private String alarmUnit;
	private String taskId;
	private double latitude;
	private double longitude;
	private String type;

	public String getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}

	public String getAlarmEquipment() {
		return alarmEquipment;
	}

	public void setAlarmEquipment(String alarmEquipment) {
		this.alarmEquipment = alarmEquipment;
	}

	public String getAlarmPosition() {
		return alarmPosition;
	}

	public void setAlarmPosition(String alarmPosition) {
		this.alarmPosition = alarmPosition;
	}

	public String getAlarmUnit() {
		return alarmUnit;
	}

	public void setAlarmUnit(String alarmUnit) {
		this.alarmUnit = alarmUnit;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
