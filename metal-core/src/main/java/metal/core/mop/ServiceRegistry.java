/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mop;

import static metal.core.common.XmlAnnotationUtils.*;
import static metal.core.mop.MopMessageCode.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import metal.core.mop.annotation.Method;
import metal.core.mop.annotation.Service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;

public class ServiceRegistry implements BeanPostProcessor {

	private Map<String, ServiceDeclaration> serviceMap = new HashMap<String, ServiceDeclaration>();
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		registerService(beanName, bean);
		return bean;
	}

	private void registerService(String beanName, Object bean) {
		Class<?> beanClass = bean.getClass();
		Service serviceTag = AnnotationUtils.findAnnotation(beanClass, Service.class);
		if (serviceTag != null) {
			Map<String, MethodDeclaration> methodMap = new HashMap<String, MethodDeclaration>();
			for (java.lang.reflect.Method method : beanClass.getMethods()) {
				Method methodTag = AnnotationUtils.findAnnotation(method, Method.class);
				if (methodTag != null) {
					String[] paramNames = methodTag.params();
					Class<?>[] paramTypes = method.getParameterTypes();
					if (paramNames.length != paramTypes.length) {
						throw new MopException(UnexpectedParamNameCount, paramNames.length, paramTypes.length, method.getName(), beanClass.getName());
					}
					List<NameDeclaration> params = new ArrayList<NameDeclaration>();
					for (int i = 0; i < paramNames.length; i++) {
						params.add(new NameDeclaration(paramNames[i], paramTypes[i]));
					}
					methodMap.put(ensureName(methodTag.name(), method.getName()), new MethodDeclaration(method.getName(), params));
				}
			}
			serviceMap.put(ensureName(serviceTag.path(), beanClass.getSimpleName()), new ServiceDeclaration(beanName, methodMap));
		}
	}
	
	public String getServiceBeanName(String serviceName) {
		ServiceDeclaration service = serviceMap.get(serviceName);
		if (service != null) {
			return service.getName();
		}
		return null;
	}
	
	public MethodDeclaration getServiceMethodDeclaration(String serviceName, String methodName) {
		ServiceDeclaration service = serviceMap.get(serviceName);
		if (service != null) {
			return service.getMethodDeclaration(methodName);
		}
		return null;
	}
	
}
