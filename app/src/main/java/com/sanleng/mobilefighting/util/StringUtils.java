package com.sanleng.mobilefighting.util;

import java.util.List;

public class StringUtils {
	/**
	 * 判断字符串是否为空
	 *
	 * @param param
	 * @return
	 */
	public static final boolean isEmpty(String param) {
		return null == param || 0 == param.length() || "".equals(param.trim());
	}

	public static String stringListToString(List<String> stringList) {
		if (stringList == null || stringList.size() <= 0) {
			return null;
		}
		String result = "";
		for (String string : stringList) {
			result += string + ",";
		}
		return result.substring(0, result.lastIndexOf(","));
	}
}
