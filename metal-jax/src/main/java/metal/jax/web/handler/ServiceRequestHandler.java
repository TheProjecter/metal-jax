/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.web.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.remoting.support.RemoteInvocationTraceInterceptor;

public class ServiceRequestHandler extends HttpInvokerServiceExporter implements ApplicationContextAware {
	
	private ApplicationContext context;
	private Object lock = new Object();
	private Map<String,Object> proxyMap = new HashMap<String,Object>();
	private Map<String,Object> serviceMap;
	
	protected Object getProxy(HttpServletRequest request) {
		String key = request.getPathInfo();
		Object proxy = proxyMap.get(key);
		if (proxy == null) {
			synchronized (lock) {
				if (proxy == null) {
					proxy = createProxy(key);
					proxyMap.put(key, proxy);
				}
			}
		}
		return proxy;
	}
	
	protected Object createProxy(String key) {
		Object service = context.getBean(key);
		Class<?> face = service.getClass().getInterfaces()[0];
		ProxyFactory factory = new ProxyFactory();
		factory.addInterface(face);
		factory.setTarget(service);
		factory.addAdvice(new RemoteInvocationTraceInterceptor(service.getClass().getSimpleName()));
		return factory.getProxy(getBeanClassLoader());
	}
	
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}
	
	public void setServiceMap(Map<String,Object> serviceMap) {
		this.serviceMap = serviceMap;
	}

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServiceRequestWrapper requestWrapper = new ServiceRequestWrapper(request);
		RemoteInvocation invocation = requestWrapper.getInvocation();
		Object proxy = getProxy(requestWrapper);
		RemoteInvocationResult result = invokeAndCreateResult(invocation, proxy);
		ServiceResponseWrapper responseWrapper = new ServiceResponseWrapper(response, result);
		responseWrapper.flushBuffer();
	}

}
