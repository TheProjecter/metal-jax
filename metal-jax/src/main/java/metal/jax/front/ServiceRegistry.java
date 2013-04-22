/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

import static metal.core.common.XmlAnnotationUtils.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import metal.core.mapper.Property;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class ServiceRegistry implements BeanPostProcessor {

	private Map<String, Property<String, Map<String, Property<String, Class<?>>>>> serviceMap = new HashMap<String, Property<String, Map<String, Property<String, Class<?>>>>>();
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Class<?> beanClass = bean.getClass();
		XmlRootElement serviceTag = beanClass.getAnnotation(XmlRootElement.class);
		if (serviceTag != null) {
			Property<String, Map<String, Property<String, Class<?>>>> serviceDef = new Property<String, Map<String, Property<String, Class<?>>>>(beanName, new HashMap<String, Property<String, Class<?>>>()); 
			serviceMap.put(ensureName(serviceTag.name(), beanClass.getSimpleName()), serviceDef);
			for (Method method : beanClass.getMethods()) {
				XmlElement methodTag = method.getAnnotation(XmlElement.class);
				if (methodTag != null) {
					Class<?>[] types = method.getParameterTypes();
					Property<String, Class<?>> methodDef = new Property<String, Class<?>>(method.getName(), types.length != 0 ? types[0] : null);
					serviceDef.getValue().put(ensureName(methodTag.name(), method.getName()), methodDef);
				}
			}
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	public String getServiceBeanName(String serviceName) {
		Property<String, Map<String, Property<String, Class<?>>>> serviceDef = serviceMap.get(serviceName);
		if (serviceDef != null) {
			return serviceDef.getKey();
		}
		return null;
	}
	
	public String getServiceMethodName(String serviceName, String methodName) {
		Property<String, Map<String, Property<String, Class<?>>>> serviceDef = serviceMap.get(serviceName);
		if (serviceDef != null) {
			Property<String, Class<?>> methodDef = serviceDef.getValue().get(methodName);
			if (methodDef != null) {
				return methodDef.getKey();
			}
		}
		return null;
	}
	
	public Class<?> getServiceMethodParamType(String serviceName, String methodName) {
		Property<String, Map<String, Property<String, Class<?>>>> serviceDef = serviceMap.get(serviceName);
		if (serviceDef != null) {
			Property<String, Class<?>> methodDef = serviceDef.getValue().get(methodName);
			if (methodDef != null) {
				return methodDef.getValue();
			}
		}
		return null;
	}
	
}
