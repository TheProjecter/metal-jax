/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.web.handler;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.remoting.support.RemoteInvocation;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import demo.model.HelloModel;

public class ServiceRequestWrapper extends BaseRequestWrapper {
	
	public ServiceRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	@Override
	public String getPathInfo() {
		String pathInfo = (String)getAttribute("INCLUDE_PATH_INFO_ATTR");
		if (pathInfo == null) pathInfo = super.getPathInfo();
		return pathInfo;
	}

	public RemoteInvocation getInvocation() throws IOException {
//		Document doc = parseInput(request.getInputStream());
		RemoteInvocation i = new RemoteInvocation();
		i.setMethodName("hello");
		i.setParameterTypes(new Class[]{HelloModel.class});
		i.setArguments(new Object[]{new HelloModel()});
		return i;
	}

	protected Document parseInput(InputStream input) throws ServletException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			return factory.newDocumentBuilder().parse(input);
		} catch (SAXException e) {
			throw new ServletException(e);
		} catch (ParserConfigurationException e) {
			throw new ServletException(e);
		}
	}
}
