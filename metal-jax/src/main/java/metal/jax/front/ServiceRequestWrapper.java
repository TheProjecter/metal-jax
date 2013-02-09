/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.catalina.Globals;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ServiceRequestWrapper extends BaseRequestWrapper {
	
	private String dirName;
	private String baseName;
	private String extName;
	private boolean methodAsBaseName;
	
	public ServiceRequestWrapper(HttpServletRequest request) {
		super(request);
		init(getPathInfo());
	}
	
	protected void init(String path) {
		int index = path.lastIndexOf('/');
		if (index >= 0) {
			dirName = index == 0 ? "/" : path.substring(0, index);
			path = path.substring(index+1);
		}
		index = path.lastIndexOf('.');
		if (index < 0) {
			baseName = path;
		} else if (index > 0) {
			baseName = path.substring(0, index);
		}
		if (index >= 0) {
			extName = path.substring(index+1);
		}
	}
	
	public String getDirName() {
		return dirName;
	}

	public String getBaseName() {
		return baseName;
	}

	public String getExtName() {
		return extName;
	}

	public boolean isMethodAsBaseName() {
		return methodAsBaseName;
	}

	public void setMethodAsBaseName() {
		this.methodAsBaseName = true;
	}

	@Override
	public String getServletPath() {
		String value = (String)getAttribute(Globals.INCLUDE_SERVLET_PATH_ATTR);
		if (value == null) value = super.getServletPath();
		return value;
	}

	@Override
	public String getPathInfo() {
		String value = (String)getAttribute(Globals.INCLUDE_PATH_INFO_ATTR);
		if (value == null) value = super.getPathInfo();
		return value;
	}

	public Object[] getValues() {
//		Document doc = parseInput(request.getInputStream());
//		RemoteInvocation i = new ServiceInvocation();
//		i.setMethodName(baseName);
//		i.setParameterTypes(new Class[]{HelloModel.class});
//		i.setArguments(new Object[]{new HelloModel()});
		return null;
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
