package org.f4a.utils;

public class StringUtils {

	public static boolean isNullOrEmpty(String style) {
		return style == null || style.length() == 0;
	}

	public static String getPlural(int c) {
		return c > 1 ? "s" : "";
	}
}
