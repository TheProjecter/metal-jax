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

public class ServiceResponse extends BaseResponse {
	
	private ResponseMessage message;
	
	public ServiceResponse(HttpServletResponse response, ResponseMessage message) {
		super(response);
		this.message = message;
	}
	
	@Override
	public void flushBuffer() throws IOException {
		PrintWriter writer = getResponse().getWriter();
		sendError(SC_INTERNAL_SERVER_ERROR, "not ready message");
		writer.write("not ready");
		writer.flush();
	}

}
