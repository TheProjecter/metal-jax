/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import metal.core.mapper.Property;
import metal.core.mapper.PropertyListAdapter;

@XmlRootElement
public class Request {

	private String method;
	private List<Property<Class<?>, Object>> parameters = Collections.emptyList();

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@XmlJavaTypeAdapter(PropertyListAdapter.class)
	public List<Property<Class<?>, Object>> getParameters() {
		return parameters;
	}

	public void setParameters(List<Property<Class<?>, Object>> parameters) {
		this.parameters = parameters;
	}

	public Class<?>[] getParameterTypes() {
		Class<?>[] types = new Class[parameters.size()];
		for (int i = 0; i < parameters.size(); i++) {
			types[i] = (Class<?>) parameters.get(i).getKey();
		}
		return types;
	}

	public Object[] getParameterValues() {
		Object[] values = new Object[parameters.size()];
		for (int i = 0; i < parameters.size(); i++) {
			values[i] = parameters.get(i).getValue();
		}
		return values;
	}

}
