/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import metal.jax.mapper.Model;

@XmlRootElement
@XmlType(propOrder={"method", "properties"})
public class Request extends Model {
	
	private String method;
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	public Class<?>[] getParameterTypes() {
		Class<?>[] types = new Class[properties.length];
		for (int i = 0; i < properties.length; i++) {
			types[i] = properties[i].getType();
		}
		return types;
	}
	
	public Object[] getParameterValues() {
		Object[] values = new Object[properties.length];
		for (int i = 0; i < properties.length; i++) {
			values[i] = properties[i].getValue();
		}
		return values;
	}
	
}
