package com.imaginea.rest.documenter.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.imaginea.rest.documenter.DataCreation;
import com.imaginea.rest.model.ApiInfo;
import com.imaginea.rest.model.ClassInfo;
import com.imaginea.rest.model.ClassResponseEntity;

public class WebDocumenterDelegate extends DataCreation {

	private static final Logger LOGGER = Logger.getLogger(WebDocumenterDelegate.class);
	private Map<String, String> pathJsonMap = null;
	private Gson gson;

	public WebDocumenterDelegate(String basePath, String[] classPaths) {
		super(classPaths, basePath);
		gson = new Gson();
	}

	/**
	 * This Method will return the list of Classes having @Path annotation. from
	 * the file already created and stored.
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public ApiInfo getListClassMetaData() {
		ApiInfo apisInfo = new ApiInfo();
		List<ClassInfo> apis = new ArrayList<ClassInfo>();
		Set<String> pathKeySet = pathJsonMap.keySet();
		for (String path : pathKeySet) {
			LOGGER.debug("Inserting " + path + " for ClassInfo ");
			ClassInfo classDesc = new ClassInfo();
			classDesc.setPath(path);
			apis.add(classDesc);
		}
		LOGGER.debug("Total number of path list prepared " + apis.size());
		apisInfo.setApis(apis);
		return apisInfo;
	}

	public String getClassInfoFromMap(String className) {
		return pathJsonMap.get("/" + className);

	}

	@Override
	public void createData() throws ClassNotFoundException {
		List<ClassResponseEntity> classesInfo = dlService.extractClassesInfo();
		pathJsonMap = new HashMap<String, String>();
		for (ClassResponseEntity classInfo : classesInfo) {
			pathJsonMap.put(classInfo.getResourcePath(), gson.toJson(classInfo));
		}
		LOGGER.debug("Path Json Map Prepared Sucessfully, Total elements " + pathJsonMap.size());
	}

}
