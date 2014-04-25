/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package modus.front.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.io.IOUtils;

public abstract class BufferedResponse extends HttpServletResponseWrapper {

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
	
	public BufferedResponse(HttpServletResponse response) {
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
	
	public InputStream getInputStream() throws IOException {
		flushInternal();
		return new ByteArrayInputStream(buffer.toByteArray());
	}

	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
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

	@Override
	public void flushBuffer() throws IOException {
		flushInternal();
		IOUtils.write(buffer.toByteArray(), getResponse().getOutputStream());
		super.flushBuffer();
	}

	protected void flushInternal() throws IOException {
		if (output != null) output.flush();
		else if (writer != null) writer.flush();
	}
	
}
