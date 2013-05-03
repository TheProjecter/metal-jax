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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import metal.jax.front.config.MethodSetting;
import metal.jax.front.config.ParamSetting;
import metal.jax.front.config.ServiceSetting;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class ServiceRegistry implements BeanPostProcessor {

	private Map<String, ServiceSetting> serviceMap = new HashMap<String, ServiceSetting>();
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Class<?> beanClass = bean.getClass();
		XmlRootElement serviceTag = beanClass.getAnnotation(XmlRootElement.class);
		if (serviceTag != null) {
			Map<String, MethodSetting> methodMap = new HashMap<String, MethodSetting>();
			for (Method method : beanClass.getMethods()) {
				XmlElement methodTag = method.getAnnotation(XmlElement.class);
				XmlAttribute paramTag = method.getAnnotation(XmlAttribute.class);
				if (methodTag != null) {
					Class<?>[] types = method.getParameterTypes();
					String paramName = paramTag != null ? paramTag.name() : null;
					ParamSetting paramSetting = types.length != 0 ? new ParamSetting(paramName, types[0]) : null;
					MethodSetting methodSetting = new MethodSetting(method.getName(), paramSetting);
					methodMap.put(ensureName(methodTag.name(), method.getName()), methodSetting);
				}
			}
			ServiceSetting serviceSetting = new ServiceSetting(beanName, methodMap);
			serviceMap.put(ensureName(serviceTag.name(), beanClass.getSimpleName()), serviceSetting);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	public String getServiceBeanName(String serviceName) {
		ServiceSetting serviceSetting = serviceMap.get(serviceName);
		if (serviceSetting != null) {
			return serviceSetting.getName();
		}
		return null;
	}
	
	public MethodSetting getServiceMethodSetting(String serviceName, String methodName) {
		ServiceSetting serviceSetting = serviceMap.get(serviceName);
		if (serviceSetting != null) {
			MethodSetting methodSetting = serviceSetting.getMethodSetting(methodName);
			return methodSetting;
		}
		return null;
	}
	
}
