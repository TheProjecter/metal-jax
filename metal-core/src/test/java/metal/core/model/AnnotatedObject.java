/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import metal.core.mapper.JavaTypeAdapter;

@XmlRootElement
public class AnnotatedObject {

	private Integer intObject;
	private Long longObject;
	private Double doubleObject;
	private Boolean booleanObject;
	private String stringObject;
	private Date dateObject;
	private String defaultObject;
	
	@XmlJavaTypeAdapter(JavaTypeAdapter.class)
	public Integer getIntObject() {
		return intObject;
	}
	public void setIntObject(Integer intObject) {
		this.intObject = intObject;
	}
	@XmlJavaTypeAdapter(JavaTypeAdapter.class)
	public Long getLongObject() {
		return longObject;
	}
	public void setLongObject(Long longObject) {
		this.longObject = longObject;
	}
	@XmlJavaTypeAdapter(JavaTypeAdapter.class)
	public Double getDoubleObject() {
		return doubleObject;
	}
	public void setDoubleObject(Double doubleObject) {
		this.doubleObject = doubleObject;
	}
	@XmlJavaTypeAdapter(JavaTypeAdapter.class)
	public Boolean getBooleanObject() {
		return booleanObject;
	}
	public void setBooleanObject(Boolean booleanObject) {
		this.booleanObject = booleanObject;
	}
	@XmlJavaTypeAdapter(JavaTypeAdapter.class)
	public String getStringObject() {
		return stringObject;
	}
	public void setStringObject(String stringObject) {
		this.stringObject = stringObject;
	}
	@XmlJavaTypeAdapter(JavaTypeAdapter.class)
	public Date getDateObject() {
		return dateObject;
	}
	public void setDateObject(Date dateObject) {
		this.dateObject = dateObject;
	}
	@XmlJavaTypeAdapter(JavaTypeAdapter.class)
	public String getDefaultObject() {
		return defaultObject;
	}
	public void setDefaultObject(String defaultObject) {
		this.defaultObject = defaultObject;
	}
	
}
