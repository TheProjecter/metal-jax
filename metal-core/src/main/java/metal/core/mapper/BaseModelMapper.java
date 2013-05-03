/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

import static metal.core.common.XmlAnnotationUtils.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import metal.core.mapper.config.ModelSetting;

import org.springframework.core.annotation.AnnotationUtils;

public abstract class BaseModelMapper implements ModelMapper {

	private List<ModelSetting> modelSettings;

	protected BaseModelMapper() {
		this.modelSettings = new ArrayList<ModelSetting>();
	}

	public void setModelClasses(List<Class<?>> modelClasses) {
		for (Class<?> modelClass : modelClasses) {
			XmlRootElement model = AnnotationUtils.findAnnotation(modelClass, XmlRootElement.class);
			String name = ensureName((model != null) ? model.name() : DEFAULT, modelClass.getSimpleName());
			this.modelSettings.add(new ModelSetting(name, modelClass));
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
		for (ModelSetting setting : modelSettings) {
			if (setting.getModelClass().equals(clazz))
				return setting.getName();
		}
		return null;
	}

	protected Class<?> modelClass(String name) {
		for (ModelSetting setting : modelSettings) {
			if (setting.getName().equals(name))
				return setting.getModelClass();
		}
		return null;
	}

}
