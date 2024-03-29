/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package modus.core.mapper;

import static modus.core.mapper.MapperMessageCode.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;

import org.junit.Assert;

import modus.core.model.AnnotatedObject;
import modus.core.model.BaseObject;
import modus.core.model.GenericObject;

public class TestUtils {

	public static BaseObject init(BaseObject object) {
		object.setIntValue(4);
		object.setLongValue(5L);
		object.setDoubleValue(7.123);
		object.setBooleanValue(true);
		object.setIntObject(4);
		object.setLongObject(5L);
		object.setDoubleObject(7.123);
		object.setBooleanObject(true);
		object.setStringObject("ABC");
		object.setDateObject(dateValue("20131231", "yyyyMMdd"));
		object.setDefaultObject("");
		return object;
	}
	
	public static AnnotatedObject init(AnnotatedObject object) {
		object.setIntObject(4);
		object.setLongObject(5L);
		object.setDoubleObject(7.123);
		object.setBooleanObject(true);
		object.setStringObject("ABC");
		object.setDateObject(dateValue("20131231", "yyyyMMdd"));
		object.setDefaultObject("");
		object.setIntList(Arrays.asList(1, 2, 3));
		return object;
	}
	
	public static GenericObject init(GenericObject object) {
		object.setIntObject(4);
		object.setLongObject(5L);
		object.setDoubleObject(7.123);
		object.setBooleanObject(true);
		object.setStringObject("ABC");
		object.setDateObject(dateValue("20131231", "yyyyMMdd"));
		object.setDefaultObject("");
		object.setObjectObject(init(new BaseObject()));
		object.setListObject(init(new ArrayList<Object>()));
		object.setMapObject(init(new LinkedHashMap<String,Object>()));
		return object;
	}
	
	public static List<Object> init(List<Object> list) {
		list.add(4);
		list.add(5L);
		list.add(7.123);
		list.add(true);
		list.add("ABC");
		list.add(dateValue("20131231", "yyyyMMdd"));
		list.add("");
		list.add(null);
		list.add(init(new BaseObject()));
		return list;
	}
	
	public static Map<String,Object> init(Map<String,Object> map) {
		map.put("intObject", 4);
		map.put("longObject", 5L);
		map.put("doubleObject", 7.123);
		map.put("booleanObject", true);
		map.put("stringObject", "ABC");
		map.put("dateObject", dateValue("20131231", "yyyyMMdd"));
		map.put("defaultObject", "");
		map.put("nullObject", null);
		map.put("objectObject", init(new BaseObject()));
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public static void assertEquals(Object o1, Object o2) {
		if (o1 instanceof BaseObject) {
			assertEquals((BaseObject)o1, (BaseObject)o2);
		} else if (o1 instanceof AnnotatedObject) {
			assertEquals((AnnotatedObject)o1, (AnnotatedObject)o2);
		} else if (o1 instanceof GenericObject) {
			assertEquals((GenericObject)o1, (GenericObject)o2);
		} else if (o1 instanceof List) {
			assertEquals((List<Object>)o1, (List<Object>)o2);
		} else if (o1 instanceof Map) {
			assertEquals((Map<String,Object>)o1, (Map<String,Object>)o2);
		} else {
			Assert.assertEquals(o1, o2);
		}
	}
	
	public static void assertEquals(BaseObject o1, BaseObject o2) {
		Assert.assertTrue(o1.getIntValue() == o2.getIntValue());
		Assert.assertTrue(o1.getLongValue() == o2.getLongValue());
		Assert.assertTrue(o1.getDoubleValue() == o2.getDoubleValue());
		Assert.assertTrue(o1.getBooleanValue() == o2.getBooleanValue());
		Assert.assertTrue(o1.getIntObject().equals(o2.getIntObject()));
		Assert.assertTrue(o1.getLongObject().equals(o2.getLongObject()));
		Assert.assertTrue(o1.getDoubleObject().equals(o2.getDoubleObject()));
		Assert.assertTrue(o1.getBooleanObject().equals(o2.getBooleanObject()));
		Assert.assertTrue(o1.getStringObject().equals(o2.getStringObject()));
		Assert.assertTrue(o1.getDateObject().equals(o2.getDateObject()));
		Assert.assertTrue(o1.getDefaultObject().equals(o2.getDefaultObject()));
	}
	
	public static void assertEquals(AnnotatedObject o1, AnnotatedObject o2) {
		Assert.assertTrue(o1.getIntObject().equals(o2.getIntObject()));
		Assert.assertTrue(o1.getLongObject().equals(o2.getLongObject()));
		Assert.assertTrue(o1.getDoubleObject().equals(o2.getDoubleObject()));
		Assert.assertTrue(o1.getBooleanObject().equals(o2.getBooleanObject()));
		Assert.assertTrue(o1.getStringObject().equals(o2.getStringObject()));
		Assert.assertTrue(o1.getDateObject().equals(o2.getDateObject()));
		Assert.assertTrue(o1.getDefaultObject().equals(o2.getDefaultObject()));
		Assert.assertTrue(o1.getIntList().equals(o2.getIntList()));
	}
	
	@SuppressWarnings("unchecked")
	public static void assertEquals(GenericObject o1, GenericObject o2) {
		Assert.assertTrue(o1.getIntObject().equals(o2.getIntObject()));
		Assert.assertTrue(o1.getLongObject().equals(o2.getLongObject()));
		Assert.assertTrue(o1.getDoubleObject().equals(o2.getDoubleObject()));
		Assert.assertTrue(o1.getBooleanObject().equals(o2.getBooleanObject()));
		Assert.assertTrue(o1.getStringObject().equals(o2.getStringObject()));
		Assert.assertTrue(o1.getDateObject().equals(o2.getDateObject()));
		Assert.assertTrue(o1.getDefaultObject().equals(o2.getDefaultObject()));
		assertEquals((BaseObject)o1.getObjectObject(), (BaseObject)o2.getObjectObject());
		assertEquals((List<Object>)o1.getListObject(), (List<Object>)o2.getListObject());
		assertEquals((Map<String,Object>)o1.getMapObject(), (Map<String,Object>)o2.getMapObject());
	}
	
	public static void assertEquals(List<Object> o1, List<Object> o2) {
		Assert.assertTrue(o1.get(0).equals(o2.get(0)));
		Assert.assertTrue(o1.get(1).equals(o2.get(1)));
		Assert.assertTrue(o1.get(2).equals(o2.get(2)));
		Assert.assertTrue(o1.get(3).equals(o2.get(3)));
		Assert.assertTrue(o1.get(4).equals(o2.get(4)));
		Assert.assertTrue(o1.get(5).equals(o2.get(5)));
		Assert.assertTrue(o1.get(6).equals(o2.get(6)));
		Assert.assertTrue(o1.get(7) == o2.get(7));
		assertEquals((BaseObject)o1.get(8), (BaseObject)o2.get(8));
	}
	
	public static void assertEquals(Map<String,Object> o1, Map<String,Object> o2) {
		Assert.assertTrue(o1.get("intObject").equals(o2.get("intObject")));
		Assert.assertTrue(o1.get("longObject").equals(o2.get("longObject")));
		Assert.assertTrue(o1.get("doubleObject").equals(o2.get("doubleObject")));
		Assert.assertTrue(o1.get("booleanObject").equals(o2.get("booleanObject")));
		Assert.assertTrue(o1.get("stringObject").equals(o2.get("stringObject")));
		Assert.assertTrue(o1.get("dateObject").equals(o2.get("dateObject")));
		Assert.assertTrue(o1.get("defaultObject").equals(o2.get("defaultObject")));
		Assert.assertTrue(o1.get("nullObject") == o2.get("nullObject"));
		assertEquals((BaseObject)o1.get("objectObject"), (BaseObject)o2.get("objectObject"));
	}
	
	public static Date dateValue(String value, String format) {
		try {
			return DateUtils.parseDate(value, new String[]{format});
		} catch (ParseException e) {
			throw new MapperException(UnexpectedException, e.getMessage());
		}
	}
	
}
