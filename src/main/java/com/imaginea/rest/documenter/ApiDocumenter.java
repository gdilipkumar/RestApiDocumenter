package com.imaginea.rest.documenter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import com.imaginea.rest.delegate.ClassDocumenter;
import com.imaginea.rest.model.ClassInfo;
import com.imaginea.rest.model.ClassResponseEntity;
import com.imaginea.rest.util.JsonUtil;
import com.imaginea.rest.util.RestApiClassUtil;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/apidocs")
public class ApiDocumenter {

	private ClassDocumenter apiDoc;
	private Map<String,String> pathJsonMap = null;
	
	@Context
	ServletContext servletContext;
	
	String basePath;
	public ApiDocumenter() throws FileNotFoundException, IOException, ClassNotFoundException {
		
		apiDoc = new ClassDocumenter();
	//	ApiDocumenResourceConfig cong= new ApiDocumenResourceConfig();
		init();
	}

	/**
	 * This is the base function for Api docs, it will fetch the list of all
	 * resources
	 * 
	 * @return
	 * @throws ClassNotFoundException 
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getMetaInfo() throws ClassNotFoundException {
		String[] classPaths = new String[] { "/WEB-INF/lib", "/WEB-INF/classes" };
		Set<Class> allClasses = RestApiClassUtil.getPathAnnotatedClasses(classPaths, servletContext);
		preparePathJsonMap(allClasses);
		List<ClassInfo> classInfoList = getListClassMetaData();
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("apis", classInfoList);
		return jsonObj.toString();

	}

	/**
	 * @param allClasses
	 * @throws ClassNotFoundException
	 */
	private void preparePathJsonMap(Set<Class> allClasses) throws ClassNotFoundException {
		pathJsonMap = new HashMap<String, String>();
		for (Class className : allClasses) {
			ClassResponseEntity extractClassInfo = apiDoc.extractClassInfo(className);
			pathJsonMap.put(extractClassInfo.getResourcePath(), JsonUtil.toJson(extractClassInfo,basePath).toString());
		}
	}

	/**
	 * It will returns the class information in json information
	 * 
	 * @param className
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@GET
	@Path("/{class}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getClassInfo(@PathParam("class") String className) throws IOException, ClassNotFoundException {
		// return
		// JsonUtil.toJson(apiDoc.extractClassInfo(className)).toString();
		
		return pathJsonMap.get("/" + className);
	}

	/**
	 * This Method will return the list of Classes having @Path annotation. from
	 * the file already created and stored.
	 * 
	 * @return
	 */
	public List<ClassInfo> getListClassMetaData() {
		List<ClassInfo> apis = new ArrayList<ClassInfo>();
		Set<String> pathKeySet = pathJsonMap.keySet();
		for (String path : pathKeySet) {
			ClassInfo classDesc = new ClassInfo();
			classDesc.setPath(path);
			apis.add(classDesc);
		}
		return apis;
	}
	
	
	private void init() throws ClassNotFoundException, IOException {
		Properties appProps = new Properties();
		appProps.load(new FileInputStream(this.getClass().getResource("/SwaggerConfig.properties").getPath()));
		basePath=appProps.getProperty("base.path.url");
	}

}
