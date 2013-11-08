/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseObject {

	private int intValue;
	private long longValue;
	private double doubleValue;
	private boolean booleanValue;
	private Integer intObject;
	private Long longObject;
	private Double doubleObject;
	private Boolean booleanObject;
	private String stringObject;
	private Date dateObject;
	private String defaultObject;
	private Object nullObject;
	
	public int getIntValue() {
		return intValue;
	}
	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}
	public long getLongValue() {
		return longValue;
	}
	public void setLongValue(long longValue) {
		this.longValue = longValue;
	}
	public double getDoubleValue() {
		return doubleValue;
	}
	public void setDoubleValue(double doubleValue) {
		this.doubleValue = doubleValue;
	}
	public boolean getBooleanValue() {
		return booleanValue;
	}
	public void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}
	public Integer getIntObject() {
		return intObject;
	}
	public void setIntObject(Integer intObject) {
		this.intObject = intObject;
	}
	public Long getLongObject() {
		return longObject;
	}
	public void setLongObject(Long longObject) {
		this.longObject = longObject;
	}
	public Double getDoubleObject() {
		return doubleObject;
	}
	public void setDoubleObject(Double doubleObject) {
		this.doubleObject = doubleObject;
	}
	public Boolean getBooleanObject() {
		return booleanObject;
	}
	public void setBooleanObject(Boolean booleanObject) {
		this.booleanObject = booleanObject;
	}
	public String getStringObject() {
		return stringObject;
	}
	public void setStringObject(String stringObject) {
		this.stringObject = stringObject;
	}
	public Date getDateObject() {
		return dateObject;
	}
	public void setDateObject(Date dateObject) {
		this.dateObject = dateObject;
	}
	public String getDefaultObject() {
		return defaultObject;
	}
	public void setDefaultObject(String defaultObject) {
		this.defaultObject = defaultObject;
	}
	public Object getNullObject() {
		return nullObject;
	}
	public void setNullObject(Object nullObject) {
		this.nullObject = nullObject;
	}
	
}
