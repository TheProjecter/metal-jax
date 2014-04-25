/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package modus.front.crossdomain;

import static modus.front.common.HttpHeaderField.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.HttpRequestHandler;

import modus.front.common.HttpContentType;

public class CrossDomainRequestHandler implements HttpRequestHandler {
	
	private static final String VERSION_DEFAULT = "modus";
	private String version = VERSION_DEFAULT;
	
	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader(CacheControl.name, CacheControl.value);
		response.setContentType(HttpContentType.JS.contentType);
		CrossDomainResponse xdResponse = new CrossDomainResponse(response, request.getRequestURL().toString(), version);
		request.getRequestDispatcher(request.getPathInfo()).include(request, xdResponse);
		xdResponse.flushBuffer();
	}
	
}
