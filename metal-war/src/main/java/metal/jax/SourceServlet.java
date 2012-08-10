package metal.jax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Serves source files across domain.
 */
@SuppressWarnings("serial")
public class SourceServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		handleSourceHeaders(response);
		handleSourceContent(response, getSourceReader(request), getSourceCallback(request));
	}
	
	private static final String CACHE_HEADER = "Cache-Control";
	private static final String CACHE_AGE = "public,max-age=300";
	private static final String CONTENT_TYPE = "text/plain";
	private static void handleSourceHeaders(HttpServletResponse response) {
		response.setHeader(CACHE_HEADER, CACHE_AGE);
		response.setContentType(CONTENT_TYPE);
	}

	private static final String BEGIN_CONTENT = "({content:\"";
	private static final String BEGIN_ERROR = "({error:\"";
	private static final String ERROR_NOTFOUND = "not found";
	private static final String END = "\"});";
	private static void handleSourceContent(HttpServletResponse response, BufferedReader reader, String callback) throws IOException {
		PrintWriter out = response.getWriter();
		out.write(callback);
		if (reader == null) {
			out.write(BEGIN_ERROR);
			out.write(ERROR_NOTFOUND);
		} else {
			out.write(BEGIN_CONTENT);
			translate(reader, out);
		}
		out.write(END);
	}
	
	private static final String THIS = "this";
	private static final String OPEN = "[\"";
	private static final String CLOSE = "\"]";
	private static final String VERSION = "metal-jax-1.0.0";
	private static String getSourceCallback(HttpServletRequest request) {
		return new StringBuilder(THIS).append(OPEN).append(VERSION).append(CLOSE)
			.append(OPEN).append(request.getRequestURL()).append(CLOSE).toString();
	}
	
	private BufferedReader getSourceReader(HttpServletRequest request) {
		String path = getSourcePath(request);
		InputStream in = getServletContext().getResourceAsStream(path);
		return in != null ? new BufferedReader(new InputStreamReader(in)) : null;
	}
	
	private static final String SLASH = "/";
	private static String getSourcePath(HttpServletRequest request) {
		String[] parts = request.getRequestURI().substring(request.getContextPath().length()).split(SLASH);
		return join(parts, 2, parts.length, SLASH);
	}
	
	private static String join(String[] parts, int start, int end, String delim) {
		StringBuilder buf = new StringBuilder();
		for (int i = start; i < end; i++) {
			buf.append(delim).append(parts[i]);
		}
		return buf.toString();
	}
	
	private static final String LINE_DELIM = System.getProperty("line.separator", "\n");
	private static void translate(BufferedReader reader, Writer out) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			StringEscapeUtils.escapeJavaScript(out, line);
			StringEscapeUtils.escapeJavaScript(out, LINE_DELIM);
		}
	}
}
