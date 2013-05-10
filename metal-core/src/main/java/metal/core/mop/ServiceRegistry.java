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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class ServiceRegistry implements BeanPostProcessor {

	private Map<String, ServiceDeclaration> serviceMap = new HashMap<String, ServiceDeclaration>();
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Class<?> beanClass = bean.getClass();
		XmlRootElement serviceTag = beanClass.getAnnotation(XmlRootElement.class);
		if (serviceTag != null) {
			Map<String, MethodDeclaration> methodMap = new HashMap<String, MethodDeclaration>();
			for (Method method : beanClass.getMethods()) {
				XmlElement methodTag = method.getAnnotation(XmlElement.class);
				XmlAttribute paramTag = method.getAnnotation(XmlAttribute.class);
				if (methodTag != null) {
					String[] paramNames = paramTag != null ? ensureName(paramTag.name(), "").split(",") : new String[0];
					Class<?>[] paramTypes = method.getParameterTypes();
					if (paramNames.length != paramTypes.length) {
						throw new MopException(UnexpectedParamNameCount, paramNames.length, paramTypes.length, method.getName(), beanClass.getName());
					}
					List<NameDeclaration> paramDecls = new ArrayList<NameDeclaration>();
					for (int i = 0; i < paramNames.length; i++) {
						String paramName = paramNames[i];
						Class<?> paramType = paramTypes[i];
						paramDecls.add(new NameDeclaration(paramName, paramType));
					}
					MethodDeclaration methodSetting = new MethodDeclaration(method.getName(), paramDecls);
					methodMap.put(ensureName(methodTag.name(), method.getName()), methodSetting);
				}
			}
			ServiceDeclaration serviceSetting = new ServiceDeclaration(beanName, methodMap);
			serviceMap.put(ensureName(serviceTag.name(), beanClass.getSimpleName()), serviceSetting);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	public String getServiceBeanName(String serviceName) {
		ServiceDeclaration serviceSetting = serviceMap.get(serviceName);
		if (serviceSetting != null) {
			return serviceSetting.getName();
		}
		return null;
	}
	
	public MethodDeclaration getServiceMethodSetting(String serviceName, String methodName) {
		ServiceDeclaration serviceSetting = serviceMap.get(serviceName);
		if (serviceSetting != null) {
			MethodDeclaration methodSetting = serviceSetting.getMethodDeclaration(methodName);
			return methodSetting;
		}
		return null;
	}
	
}
