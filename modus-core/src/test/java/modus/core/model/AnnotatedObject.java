/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package modus.core.model;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import modus.core.mapper.ValueAdapter;

@XmlRootElement
public class AnnotatedObject {

	private Integer intObject;
	private Long longObject;
	private Double doubleObject;
	private Boolean booleanObject;
	private String stringObject;
	private Date dateObject;
	private String defaultObject;
	private List<Integer> intList;
	
	@XmlJavaTypeAdapter(ValueAdapter.class)
	public Integer getIntObject() {
		return intObject;
	}
	public void setIntObject(Integer intObject) {
		this.intObject = intObject;
	}
	@XmlJavaTypeAdapter(ValueAdapter.class)
	public Long getLongObject() {
		return longObject;
	}
	public void setLongObject(Long longObject) {
		this.longObject = longObject;
	}
	@XmlJavaTypeAdapter(ValueAdapter.class)
	public Double getDoubleObject() {
		return doubleObject;
	}
	public void setDoubleObject(Double doubleObject) {
		this.doubleObject = doubleObject;
	}
	@XmlJavaTypeAdapter(ValueAdapter.class)
	public Boolean getBooleanObject() {
		return booleanObject;
	}
	public void setBooleanObject(Boolean booleanObject) {
		this.booleanObject = booleanObject;
	}
	@XmlJavaTypeAdapter(ValueAdapter.class)
	public String getStringObject() {
		return stringObject;
	}
	public void setStringObject(String stringObject) {
		this.stringObject = stringObject;
	}
	@XmlJavaTypeAdapter(ValueAdapter.class)
	public Date getDateObject() {
		return dateObject;
	}
	public void setDateObject(Date dateObject) {
		this.dateObject = dateObject;
	}
	@XmlJavaTypeAdapter(ValueAdapter.class)
	public String getDefaultObject() {
		return defaultObject;
	}
	public void setDefaultObject(String defaultObject) {
		this.defaultObject = defaultObject;
	}
	@XmlJavaTypeAdapter(ValueAdapter.class)
	public List<Integer> getIntList() {
		return intList;
	}
	public void setIntList(List<Integer> intList) {
		this.intList = intList;
	}
	
}
