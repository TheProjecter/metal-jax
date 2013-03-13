/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

import java.io.IOException;
import java.util.Map;

import metal.jax.mapper.Mapper;
import metal.jax.model.Request;

public class ServiceResolver {

	private String servicePath;
	private Map<String,String> methodMap;
	private Map<String,String> modelMap;
	private Mapper mapper;
	
	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}

	public void setMethodMap(Map<String, String> methodMap) {
		this.methodMap = methodMap;
	}

	public void setModelMap(Map<String, String> modelMap) {
		this.modelMap = modelMap;
	}

	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	public String resolveServicePath(ServiceRequestWrapper request) {
		if (servicePath != null) return servicePath;
		return request.getDirName();
	}
	
	public String resolveServiceMethod(ServiceRequestWrapper request) {
		String method = null;
		if (methodMap != null) {
			method = methodMap.get(request.getMethod());
		}
		return method;
	}
	
	public Request parseRequest(ServiceRequestWrapper request) throws IOException {
		return mapper.deserialize(Request.class, request.getInputStream());
	}
	
}
