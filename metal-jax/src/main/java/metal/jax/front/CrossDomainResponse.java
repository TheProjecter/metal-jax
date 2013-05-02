/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

public class CrossDomainResponse extends BufferedResponse {
	
	private String requestURL;
	private String version;
	
	public CrossDomainResponse(HttpServletResponse response, String requestURL, String version) {
		super(response);
		this.requestURL = requestURL;
		this.version = version;
	}
	
	private static final String BEGIN_CONTENT = "({content:\"";
	private static final String BEGIN_ERROR = "({error:\"";
	private static final String DELIM_ERROR = " - ";
	private static final String END_CONTENT = "\"});";
	
	@Override
	public void flushBuffer() throws IOException {
		BufferedReader reader = getReader();
		PrintWriter writer = getResponse().getWriter();
		writer.write(getCallbackString());
		if (isStatusOK()) {
			writer.write(BEGIN_CONTENT);
			translate(reader, writer);
		} else {
			writer.write(BEGIN_ERROR);
			writer.print(getStatus());
			writer.write(DELIM_ERROR);
			translate(getMessage(), writer);
			
		}
		writer.write(END_CONTENT);
	}

	private static final String LINE_DELIM = System.getProperty("line.separator", "\n");
	protected static void translate(BufferedReader reader, Writer writer) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			StringEscapeUtils.escapeJavaScript(writer, line);
			StringEscapeUtils.escapeJavaScript(writer, LINE_DELIM);
		}
	}
	
	protected static void translate(String line, Writer writer) throws IOException {
		if (line != null) {
			StringEscapeUtils.escapeJavaScript(writer, line);
		}
	}
	
	private static final String THIS = "this";
	private static final String BEGIN_TOKEN = "[\"";
	private static final String END_TOKEN = "\"]";
	private String getCallbackString() {
		return new StringBuilder(THIS).append(BEGIN_TOKEN).append(version).append(END_TOKEN)
			.append(BEGIN_TOKEN).append(requestURL).append(END_TOKEN).toString();
	}
	
}
