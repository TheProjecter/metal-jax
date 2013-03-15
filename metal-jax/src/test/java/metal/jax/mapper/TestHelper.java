/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.mapper;

import java.text.ParseException;

import org.apache.commons.lang.time.DateUtils;

import metal.jax.model.BaseObject;
import metal.jax.model.GenericObject;

public class TestHelper {

	public static <T extends BaseObject> T initBase(T object) {
		object.setIntValue(4);
		object.setLongValue(5L);
		object.setDoubleValue(7.123);
		object.setBooleanValue(true);
		object.setStringValue("ABC");
		setDateValue(object, "20131231", "yyyyMMdd");
		object.setDefaultValue("");
		return object;
	}
	
	public static <T extends GenericObject> T initBase(T object) {
		object.setIntValue(4);
		object.setLongValue(5L);
		object.setDoubleValue(7.123);
		object.setBooleanValue(true);
		object.setStringValue("ABC");
		setDateValue(object, "20131231", "yyyyMMdd");
		object.setDefaultValue("");
		return object;
	}
	
	public static <T extends BaseObject> boolean baseEquals(T object1, T object2) {
		if (object1.getIntValue() != object2.getIntValue()) return false;
		if (object1.getLongValue() != object2.getLongValue()) return false;
		if (object1.getDoubleValue() != object2.getDoubleValue()) return false;
		if (object1.getBooleanValue() != object2.getBooleanValue()) return false;
		if (object1.getNullValue() != object2.getNullValue()) return false;
		if (!object1.getStringValue().equals(object2.getStringValue())) return false;
		if (!object1.getDefaultValue().equals(object2.getDefaultValue())) return false;
		if (!object1.getDateValue().equals(object2.getDateValue())) return false;
		return true;
	}
	
	public static <T extends GenericObject> boolean baseEquals(T object1, T object2) {
		if (!object1.getIntValue().equals(object2.getIntValue())) return false;
		if (!object1.getLongValue().equals(object2.getLongValue())) return false;
		if (!object1.getDoubleValue().equals(object2.getDoubleValue())) return false;
		if (!object1.getBooleanValue().equals(object2.getBooleanValue())) return false;
		if (!object1.getNullValue().equals(object2.getNullValue())) return false;
		if (!object1.getStringValue().equals(object2.getStringValue())) return false;
		if (!object1.getDefaultValue().equals(object2.getDefaultValue())) return false;
		if (!object1.getDateValue().equals(object2.getDateValue())) return false;
		return true;
	}
	
	private static void setDateValue(BaseObject object, String value, String format) {
		try {
			object.setDateValue(DateUtils.parseDate(value, new String[]{format}));
		} catch (ParseException e) {
			new MapperException("UnexpectedException", e.getMessage());
		}
	}
	
	private static void setDateValue(GenericObject object, String value, String format) {
		try {
			object.setDateValue(DateUtils.parseDate(value, new String[]{format}));
		} catch (ParseException e) {
			new MapperException("UnexpectedException", e.getMessage());
		}
	}
	
}
