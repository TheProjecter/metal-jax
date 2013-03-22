/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import metal.core.mapper.DefaultAdapter;

@XmlRootElement
public class GenericObject {

	private Object intObject;
	private Object longObject;
	private Object doubleObject;
	private Object booleanObject;
	private Object stringObject;
	private Object dateObject;
	private Object defaultObject;
	private Object listObject;
	private Object mapObject;
	private Object objectObject;
	private Object nullObject;
	
	@XmlJavaTypeAdapter(DefaultAdapter.class)
	public Object getIntObject() {
		return intObject;
	}
	public void setIntObject(Object intObject) {
		this.intObject = intObject;
	}
	@XmlJavaTypeAdapter(DefaultAdapter.class)
	public Object getLongObject() {
		return longObject;
	}
	public void setLongObject(Object longObject) {
		this.longObject = longObject;
	}
	@XmlJavaTypeAdapter(DefaultAdapter.class)
	public Object getDoubleObject() {
		return doubleObject;
	}
	public void setDoubleObject(Object doubleObject) {
		this.doubleObject = doubleObject;
	}
	@XmlJavaTypeAdapter(DefaultAdapter.class)
	public Object getBooleanObject() {
		return booleanObject;
	}
	public void setBooleanObject(Object booleanObject) {
		this.booleanObject = booleanObject;
	}
	@XmlJavaTypeAdapter(DefaultAdapter.class)
	public Object getStringObject() {
		return stringObject;
	}
	public void setStringObject(Object stringObject) {
		this.stringObject = stringObject;
	}
	@XmlJavaTypeAdapter(DefaultAdapter.class)
	public Object getDateObject() {
		return dateObject;
	}
	public void setDateObject(Object dateObject) {
		this.dateObject = dateObject;
	}
	@XmlJavaTypeAdapter(DefaultAdapter.class)
	public Object getDefaultObject() {
		return defaultObject;
	}
	public void setDefaultObject(Object defaultObject) {
		this.defaultObject = defaultObject;
	}
	@XmlJavaTypeAdapter(DefaultAdapter.class)
	public Object getListObject() {
		return listObject;
	}
	public void setListObject(Object listObject) {
		this.listObject = listObject;
	}
	@XmlJavaTypeAdapter(DefaultAdapter.class)
	public Object getMapObject() {
		return mapObject;
	}
	public void setMapObject(Object mapObject) {
		this.mapObject = mapObject;
	}
	@XmlJavaTypeAdapter(DefaultAdapter.class)
	public Object getObjectObject() {
		return objectObject;
	}
	public void setObjectObject(Object objectObject) {
		this.objectObject = objectObject;
	}
	@XmlJavaTypeAdapter(DefaultAdapter.class)
	public Object getNullObject() {
		return nullObject;
	}
	public void setNullObject(Object nullObject) {
		this.nullObject = nullObject;
	}
	
}
