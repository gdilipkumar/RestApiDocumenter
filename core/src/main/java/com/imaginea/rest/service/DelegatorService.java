package com.imaginea.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.imaginea.rest.constants.RestApiConstants;
import com.imaginea.rest.delegate.ClassDocumenter;
import com.imaginea.rest.model.ClassResponseEntity;
import com.imaginea.rest.util.RestApiClassUtil;

public class DelegatorService {
	private static Logger LOGGER = Logger.getLogger(DelegatorService.class);
	private ClassDocumenter classDoc;
	private String[] classPaths;

	public DelegatorService(String basePath, String[] classPaths) {
		this.classDoc = new ClassDocumenter(basePath);
		this.classPaths = classPaths;
	}

	public List<ClassResponseEntity> extractClassesInfo() throws ClassNotFoundException {
		Set<Class> classList = RestApiClassUtil.getPathAnnotatedClasses(classPaths);
		LOGGER.debug("Preparing Map of path and respective JSON, Keyset Size  " + classList.size());
		List<ClassResponseEntity> classesInfo = new ArrayList<ClassResponseEntity>();
		for (Class className : classList) {
			if (!(className == (Class.forName(RestApiConstants.REST_API_MAIN_CLASS_NAME)))) {
				ClassResponseEntity classInfo = classDoc.extractClassInfo(className);
			}
		}
		return classesInfo;
	}
}