/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.model;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import metal.core.model.BaseObject;

@XmlRootElement
public class TestObject extends BaseObject {

	private Object objectValue;
	private Object[] arrayValue;
	private List<?> listValue;
	private Map<?, ?> mapValue;

	public Object getObjectValue() {
		return objectValue;
	}

	public void setObjectValue(Object objectValue) {
		this.objectValue = objectValue;
	}

	public Object[] getArrayValue() {
		return arrayValue;
	}

	public void setArrayValue(Object[] arrayValue) {
		this.arrayValue = arrayValue;
	}

	public List<?> getListValue() {
		return listValue;
	}

	public void setListValue(List<?> listValue) {
		this.listValue = listValue;
	}

	public Map<?, ?> getMapValue() {
		return mapValue;
	}

	public void setMapValue(Map<?, ?> mapValue) {
		this.mapValue = mapValue;
	}

}
