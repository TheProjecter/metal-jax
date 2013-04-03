/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import metal.core.mapper.PropertyListAdapter;
import metal.core.mapper.Property;

@XmlRootElement(name = "model")
public class TestModel {

	private String property1;
	private String property2;
	private List<Property<Class<?>,Object>> properties;

	public String getProperty1() {
		return property1;
	}

	public void setProperty1(String property1) {
		this.property1 = property1;
	}

	public String getProperty2() {
		return property2;
	}

	public void setProperty2(String property2) {
		this.property2 = property2;
	}

	@XmlJavaTypeAdapter(PropertyListAdapter.class)
	public List<Property<Class<?>,Object>> getProperties() {
		return properties;
	}

	public void setProperties(List<Property<Class<?>,Object>> properties) {
		this.properties = properties;
	}

}
