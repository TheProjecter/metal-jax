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

@XmlRootElement(name="response")
public class ResponseMessage {

	private String status;
	private List<Property<Class<?>, Object>> results = Collections.emptyList();

	public ResponseMessage() {}
	
	public ResponseMessage(Object value, Throwable exception) {
		
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@XmlJavaTypeAdapter(PropertyListAdapter.class)
	public List<Property<Class<?>, Object>> getResults() {
		return results;
	}

	public void setResults(List<Property<Class<?>, Object>> results) {
		this.results = results;
	}

	public Class<?>[] getResultTypes() {
		Class<?>[] types = new Class[results.size()];
		for (int i = 0; i < results.size(); i++) {
			types[i] = (Class<?>) results.get(i).getKey();
		}
		return types;
	}

	public Object[] getResultValues() {
		Object[] values = new Object[results.size()];
		for (int i = 0; i < results.size(); i++) {
			values[i] = results.get(i).getValue();
		}
		return values;
	}

}
