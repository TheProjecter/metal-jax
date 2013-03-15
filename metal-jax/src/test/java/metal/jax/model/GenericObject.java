/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import metal.jax.mapper.ObjectAdapter;

@XmlRootElement
public class GenericObject {

	private Object intValue;
	private Object longValue;
	private Object doubleValue;
	private Object booleanValue;
	private Object stringValue;
	private Object dateValue;
	private Object defaultValue;
	private Object nullValue;
	private Object objectValue;

	@XmlJavaTypeAdapter(ObjectAdapter.class)
	public Object getIntValue() {
		return intValue;
	}

	public void setIntValue(Object intValue) {
		this.intValue = intValue;
	}

	@XmlJavaTypeAdapter(ObjectAdapter.class)
	public Object getLongValue() {
		return longValue;
	}

	public void setLongValue(Object longValue) {
		this.longValue = longValue;
	}

	@XmlJavaTypeAdapter(ObjectAdapter.class)
	public Object getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(Object doubleValue) {
		this.doubleValue = doubleValue;
	}

	@XmlJavaTypeAdapter(ObjectAdapter.class)
	public Object getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(Object booleanValue) {
		this.booleanValue = booleanValue;
	}

	@XmlJavaTypeAdapter(ObjectAdapter.class)
	public Object getStringValue() {
		return stringValue;
	}

	public void setStringValue(Object stringValue) {
		this.stringValue = stringValue;
	}

	@XmlJavaTypeAdapter(ObjectAdapter.class)
	public Object getDateValue() {
		return dateValue;
	}

	public void setDateValue(Object dateValue) {
		this.dateValue = dateValue;
	}

	@XmlJavaTypeAdapter(ObjectAdapter.class)
	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	@XmlJavaTypeAdapter(ObjectAdapter.class)
	public Object getNullValue() {
		return nullValue;
	}

	public void setNullValue(Object nullValue) {
		this.nullValue = nullValue;
	}

	@XmlJavaTypeAdapter(ObjectAdapter.class)
	public Object getObjectValue() {
		return objectValue;
	}

	public void setObjectValue(Object objectValue) {
		this.objectValue = objectValue;
	}

}
