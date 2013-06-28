/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.front.service;

import javax.servlet.http.HttpServletRequest;

import metal.front.common.BaseRequest;

public class ServiceRequest extends BaseRequest {
	
	private String dirName;
	private String baseName;
	private String extName;
	
	public ServiceRequest(HttpServletRequest request) {
		super(request);
		init(getPathInfo());
	}
	
	protected void init(String path) {
		int index = path.lastIndexOf('/');
		if (index >= 0) {
			dirName = index == 0 ? "/" : path.substring(0, index);
			path = path.substring(index+1);
		}
		index = path.lastIndexOf('.');
		if (index < 0) {
			baseName = path;
		} else if (index > 0) {
			baseName = path.substring(0, index);
		}
		if (index >= 0) {
			extName = path.substring(index+1);
		}
	}
	
	public String getDirName() {
		return dirName;
	}

	public String getBaseName() {
		return baseName;
	}

	public String getExtName() {
		return extName;
	}

	@Override
	public String getServletPath() {
		String value = (String)getAttribute(INCLUDE_SERVLET_PATH_ATTR);
		if (value == null) value = super.getServletPath();
		return value;
	}

	@Override
	public String getPathInfo() {
		String value = (String)getAttribute(INCLUDE_PATH_INFO_ATTR);
		if (value == null) value = super.getPathInfo();
		return value;
	}
	
}
