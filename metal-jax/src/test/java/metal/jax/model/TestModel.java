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

import metal.jax.mapper.Model;
import metal.jax.mapper.Property;

@XmlRootElement(name = "model")
@XmlType(propOrder={"property1", "property2", "properties"})
public class TestModel extends Model {

	private String property1;
	private String property2;

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

	public Property[] getProperties() {
		return properties;
	}
	
}
