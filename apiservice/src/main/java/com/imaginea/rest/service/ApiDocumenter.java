package com.imaginea.rest.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.imaginea.rest.constants.RestApiConstants;
import com.imaginea.rest.documenter.impl.WebDocumenterDelegate;
import com.imaginea.rest.util.PropertyReader;
import com.imaginea.rest.util.RestApiClassUtil;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/apidocs")
public class ApiDocumenter {

	private final Logger logger = Logger.getLogger(ApiDocumenter.class);
	private Gson gson;
	private WebDocumenterDelegate apiDelegate;

	String basePath;

	public ApiDocumenter() throws IOException {
		gson = new Gson();
		apiDelegate = new WebDocumenterDelegate(PropertyReader.getInstance()
						.getProperty(RestApiConstants.BASE_PATH_URL),PropertyReader.getInstance()
						.getProperty(RestApiConstants.CLASSPATHS).split(","));
	}

	/**
	 * This is the base function for Api docs, it will fetch the list of all
	 * resources
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("rawtypes")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getMetaInfo() throws ClassNotFoundException {
		apiDelegate.createData();
		return gson.toJson(apiDelegate.getListClassMetaData());
	}

	// com.sun.jersey.api.core.servlet.WebAppResourceConfig
	/**
	 * It will returns the class information in json information
	 * 
	 * @param className
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@GET
	@Path("/{class: .*}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getClassInfo(@PathParam("class") String className) throws IOException, ClassNotFoundException {
		logger.debug("Going to get JSON info for the ClassName  " + className);
		return apiDelegate.getClassInfoFromMap(className);
	}

}
