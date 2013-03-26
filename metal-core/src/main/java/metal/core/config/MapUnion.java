/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.config;

import static metal.core.config.ConfigMessageCode.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.config.MapFactoryBean;
import org.springframework.core.GenericCollectionTypeResolver;

@SuppressWarnings("rawtypes")
public class MapUnion extends MapFactoryBean {

	private Map<?,?> sourceMap;
	private Class targetMapClass;
	
	@Override
	public void setSourceMap(Map sourceMap) {
		super.setSourceMap(sourceMap);
		this.sourceMap = sourceMap;
	}

	@Override
	public void setTargetMapClass(Class targetMapClass) {
		super.setTargetMapClass(targetMapClass);
		this.targetMapClass = targetMapClass;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Map createInstance() {
		Map result = null;
		if (this.sourceMap == null) {
			throw new ConfigException(SettingRequired, "sourceMap");
		}
		if (this.targetMapClass != null) {
			result = (Map) BeanUtils.instantiateClass(this.targetMapClass);
		} else {
			result = new LinkedHashMap(this.sourceMap.size());
		}
		Class keyType = null;
		Class valueType = null;
		TypeConverter converter = null;
		if (this.targetMapClass != null) {
			keyType = GenericCollectionTypeResolver.getMapKeyType(this.targetMapClass);
			valueType = GenericCollectionTypeResolver.getMapValueType(this.targetMapClass);
			if (keyType != null || valueType != null) {
				converter = getBeanTypeConverter();
			}
		}
		for (Map.Entry entry : this.sourceMap.entrySet()) {
			Object key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof Map) {
				putAll(result, (Map)value, converter, keyType, valueType);
			} else {
				put(result, key, value, converter, keyType, valueType);
			}
		}
		return result;
	}
	
	protected void putAll(Map result, Map<?,?> sourceMap, TypeConverter converter, Class keyType, Class valueType) {
		for (Map.Entry entry : sourceMap.entrySet()) {
			Object key = entry.getKey();
			Object value = entry.getValue();
			put(result, key, value, converter, keyType, valueType);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void put(Map result, Object key, Object value, TypeConverter converter, Class keyType, Class valueType) {
		if (converter != null) {
			key = converter.convertIfNecessary(key, keyType);
			value = converter.convertIfNecessary(value, valueType);
		}
		result.put(key, value);
	}
	
}
