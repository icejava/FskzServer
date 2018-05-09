package com.pica.fskz.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {

	private static Properties prop = new Properties();

	static {
		try {
			InputStream inputStream = ConfigUtil.class.getClassLoader()
					.getResourceAsStream("fskz.properties");
			prop.load(inputStream);
		} catch (Exception e) {
			try {
				prop.clear();
				FileInputStream reader = new FileInputStream("fskz.properties");
				prop.load(reader);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static String getStringByName(String name) {
		return prop.getProperty(name);
	}

	public static Integer getIntegerByName(String name) {
		if (prop.getProperty(name) != null || !prop.getProperty(name).isEmpty()) {
			return Integer.valueOf(prop.getProperty(name));
		} else {
			return null;
		}
	}

	public static void main(String[] args) {
		System.out.println(getStringByName("aaa"));

	}
}
