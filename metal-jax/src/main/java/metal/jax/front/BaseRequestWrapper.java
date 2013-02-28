/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

abstract class BaseRequestWrapper extends HttpServletRequestWrapper {
	
	public static final String INCLUDE_SERVLET_PATH_ATTR = "javax.servlet.include.servlet_path";
	
	public static final String INCLUDE_PATH_INFO_ATTR = "javax.servlet.include.path_info";
	
	public BaseRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	@Override
	public HttpServletRequest getRequest() {
		return (HttpServletRequest)super.getRequest();
	}
	
}
