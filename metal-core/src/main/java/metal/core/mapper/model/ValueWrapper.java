/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import metal.core.mapper.ValueAdapter;

@XmlRootElement
public class ValueWrapper {

	private Object value;

	public ValueWrapper() {}

	public ValueWrapper(Object value) {
		this.value = value;
	}

	@XmlJavaTypeAdapter(ValueAdapter.class)
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
