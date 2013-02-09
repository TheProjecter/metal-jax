/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CrossDomainRequestHandler extends BaseRequestHandler {
	
	private static final String VERSION_DEFAULT = "metal-jax";
	private String version = VERSION_DEFAULT;
	
	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CrossDomainResponseWrapper responseWrapper = new CrossDomainResponseWrapper(response, request.getRequestURL().toString(), version);
		request.getRequestDispatcher(request.getPathInfo()).include(request, responseWrapper);
		responseWrapper.flushBuffer();
	}
	
}
