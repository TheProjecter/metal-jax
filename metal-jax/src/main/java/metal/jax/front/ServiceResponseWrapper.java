/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class ServiceResponseWrapper extends BaseResponseWrapper {
	
	private Object value;
	private Throwable error;
	
	public ServiceResponseWrapper(HttpServletResponse response, Object value, Throwable error) {
		super(response);
		this.value = value;
		this.error = error;
	}
	
	@Override
	public void flushBuffer() throws IOException {
		PrintWriter writer = getResponse().getWriter();
		sendError(SC_INTERNAL_SERVER_ERROR, "not ready message");
		writer.write("not ready");
		writer.flush();
	}

}
