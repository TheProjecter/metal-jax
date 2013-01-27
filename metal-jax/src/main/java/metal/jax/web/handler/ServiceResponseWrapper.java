/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.web.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.remoting.support.RemoteInvocationResult;

public class ServiceResponseWrapper extends BaseResponseWrapper {
	
	private RemoteInvocationResult result;
	
	public ServiceResponseWrapper(HttpServletResponse response, RemoteInvocationResult result) {
		super(response);
		this.result = result;
	}
	
	@Override
	public void flushBuffer() throws IOException {
		PrintWriter writer = getResponse().getWriter();
		sendError(SC_INTERNAL_SERVER_ERROR, "not ready message");
		writer.write("not ready");
		writer.flush();
	}

}
