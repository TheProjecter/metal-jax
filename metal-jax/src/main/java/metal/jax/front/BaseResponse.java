/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

abstract class BaseResponse extends HttpServletResponseWrapper {

	private class BufferedOutputStream extends ServletOutputStream {
		public void write(int b) throws IOException { buffer.write(b); }
	}
	private class BufferedWriter extends PrintWriter {
		public BufferedWriter() { super(new OutputStreamWriter(buffer)); }
	}
	
	private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	private BufferedOutputStream output;
	private PrintWriter writer;
	private String message;
	private int status;
	
	public BaseResponse(HttpServletResponse response) {
		super(response);
	}
	
	public String getMessage() {
		return message;
	}
	
	public int getStatus() {
		return status;
	}
	
	public boolean isStatusOK() {
		return status == 0 || status == SC_OK;
	}
	
	public BufferedReader getReader() throws IOException {
		if (output != null) output.flush();
		else if (writer != null) writer.flush();
		return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer.toByteArray())));
	}
	
	@Override
	public int getBufferSize() {
		return buffer.size();
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (output == null) output = new BufferedOutputStream();
		return output;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (writer == null) writer = new BufferedWriter();
		return writer;
	}

	@Override
	public HttpServletResponse getResponse() {
		return (HttpServletResponse)super.getResponse();
	}
	
	@Override
	public void sendError(int status) throws IOException {
		super.sendError(status);
		this.status = status;
		this.message = String.valueOf(status);
	}

	@Override
	public void sendError(int status, String message) throws IOException {
		super.sendError(status, message);
		this.status = status;
		this.message = message;
	}

}
