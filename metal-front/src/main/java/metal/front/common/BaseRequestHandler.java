/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.front.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.HttpRequestHandler;

public abstract class BaseRequestHandler implements HttpRequestHandler {
	
	protected static final String CACHE_HEADER = "Cache-Control";
	protected static final String CACHE_AGE = "public,max-age=300";
	
	protected void setResponseHeaders(HttpServletResponse response) {
		response.setHeader(CACHE_HEADER, CACHE_AGE);
	}
	
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setResponseHeaders(response);
		process(request, response);
	}
	
	protected abstract void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
}
