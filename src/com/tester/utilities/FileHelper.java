package com.tester.utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileHelper {

	public static String getFilePathToSave(String name) {
		return getFilePathToSave(FileHelper.class, name);
	}

	public static String getFilePathToSave(Object o, String name) {
		try {
			ClassLoader cl = o.getClass().getClassLoader();
			InputStream inputStream = cl.getResourceAsStream(name);
			StringBuilder sb = new StringBuilder();
			while (inputStream.available() > 0) {
				sb.append((char) inputStream.read());
			}
			inputStream.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "null";
	}

	public static File getFileFromResources(String name) {
		return new File(FileHelper.class.getResource('/' + name).getFile());
	}

	public static File getFileFromClassLoader(Object o, String name) {
		return new File(o.getClass().getClassLoader().getResource(name).getFile());
	}

}