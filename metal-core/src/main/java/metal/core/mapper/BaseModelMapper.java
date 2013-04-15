/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

public abstract class BaseModelMapper implements ModelMapper {

	private static final String DEFAULT = "##default";

	private List<Property<String, Class<?>>> modelClasses;

	protected BaseModelMapper() {
		this.modelClasses = new ArrayList<Property<String, Class<?>>>();
	}

	public void setModelClasses(List<Class<?>> modelClasses) {
		for (Class<?> modelClass : modelClasses) {
			XmlRootElement model = AnnotationUtils.findAnnotation(modelClass, XmlRootElement.class);
			String name = ensureName((model != null) ? model.name() : DEFAULT, modelClass.getSimpleName());
			this.modelClasses.add(new Property<String, Class<?>>(name, modelClass));
		}
	}

	@Override
	public <T> T read(Class<T> type, String input) {
		return read(type, new ByteArrayInputStream(input.getBytes()));
	}

	@Override
	public String write(Object object) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		write(object, out);
		return new String(out.toByteArray());
	}

	protected String modelName(Class<?> clazz) {
		for (Property<String, Class<?>> modelClass : modelClasses) {
			if (modelClass.getValue().equals(clazz))
				return modelClass.getKey();
		}
		return null;
	}

	protected Class<?> modelClass(String name) {
		for (Property<String, Class<?>> modelClass : modelClasses) {
			if (modelClass.getKey().equals(name))
				return modelClass.getValue();
		}
		return null;
	}

	protected String ensureName(String name, String defaultName) {
		if (StringUtils.isEmpty(name) || StringUtils.equals(name, DEFAULT)) {
			name = defaultName;
			if (!Character.isLowerCase(name.charAt(0))) {
				char[] chars = name.toCharArray();
				chars[0] = Character.toLowerCase(chars[0]);
				name = new String(chars);
			}
		}
		return name;
	}

}
