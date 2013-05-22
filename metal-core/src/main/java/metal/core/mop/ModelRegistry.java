/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mop;

import static metal.core.common.XmlAnnotationUtils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;

public class ModelRegistry implements BeanPostProcessor {
	
	private Map<String, NameDeclaration> modelMap = new HashMap<String, NameDeclaration>();

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof ClassFinder) {
			registerModelClasses((ClassFinder)bean);
		}
		return bean;
	}
	
	private void registerModelClasses(ClassFinder classFinder) {
		List<Class<?>> modelClasses = classFinder.findClasses();
		for (Class<?> modelClass : modelClasses) {
			XmlRootElement modelTag = AnnotationUtils.findAnnotation(modelClass, XmlRootElement.class);
			if (modelTag != null) {
				String name = ensureName(modelTag.name(), modelClass.getSimpleName());
				this.modelMap.put(modelClass.getName(), new NameDeclaration(name, modelClass));
			}
		}
	}
	
	public Class<?>[] getModelClasses() {
		Class<?>[] modelClasses = new Class<?>[modelMap.size()];
		int i = 0;
		for (NameDeclaration model : modelMap.values()) {
			modelClasses[i++] = model.getType();
		}
		return modelClasses;
	}
	
	public String getModelName(Class<?> clazz) {
		for (NameDeclaration model : modelMap.values()) {
			if (model.getType().equals(clazz))
				return model.getName();
		}
		return null;
	}

	public Class<?> getModelClass(String name) {
		for (NameDeclaration model : modelMap.values()) {
			if (model.getName().equals(name))
				return model.getType();
		}
		return null;
	}

}
