package com.imaginea.documenter.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertyReader {
	
	private static PropertyReader instance;
	Properties appProps;
	final String FILE_NAME = "ApiDoumenterConfig.properties";

	private PropertyReader() throws IOException {
		appProps = new Properties();
		init();
	}

	public static PropertyReader getInstance() throws IOException {
		if (instance == null) {
			synchronized (PropertyReader.class) {
				if (instance == null) 
					instance = new PropertyReader();
			}
		}
		return instance;
	}

	public void init() throws IOException {

		InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(FILE_NAME);
		if (resourceAsStream != null) {
			try {
				appProps.load(resourceAsStream);
			}
			finally {
				try {
					resourceAsStream.close();
				}
				catch (Exception ignore) {
				}
			}
		}
	}

	public String getProperty(String propName) {
		return appProps.getProperty(propName);
	}

}
