/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.front.service;

import java.util.Collections;
import java.util.Map;

public class ServiceSetting {

	private String servicePath;
	private Map<String,String> methodMap = Collections.emptyMap();
	
	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}

	public void setMethodMap(Map<String, String> methodMap) {
		this.methodMap = methodMap;
	}

	public String getServicePath(ServiceRequest request) {
		if (servicePath != null) return servicePath;
		return request.getDirName();
	}
	
	public String getRequestMethod(ServiceRequest request) {
		String method = methodMap.get(request.getBaseName());
		return method != null ? method : request.getBaseName();
	}
	
}
