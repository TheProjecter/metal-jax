/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder={"status", "properties"})
public class Response extends Model {
	
	private String status;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Class<?>[] getResultTypes() {
		Class<?>[] types = new Class[properties.length];
		for (int i = 0; i < properties.length; i++) {
			types[i] = properties[i].getType();
		}
		return types;
	}
	
	public Object[] getResultValues() {
		Object[] values = new Object[properties.length];
		for (int i = 0; i < properties.length; i++) {
			values[i] = properties[i].getValue();
		}
		return values;
	}
	
}
