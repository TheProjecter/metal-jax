/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

import java.util.Map;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class ServiceResolver {

	private String servicePath;
	private Map<String,String> methodMap;
	private Map<String,String> modelMap;
	private Jaxb2Marshaller marshaller;
	
	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}


	public void setMethodMap(Map<String, String> methodMap) {
		this.methodMap = methodMap;
	}


	public void setModelMap(Map<String, String> modelMap) {
		this.modelMap = modelMap;
	}


	public void setMarshaller(Jaxb2Marshaller marshaller) {
		this.marshaller = marshaller;
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
	
	public Class<?>[] parseParameters(ServiceRequestWrapper request) {
		return null;
	}
	
}
