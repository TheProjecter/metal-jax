/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import metal.jax.model.Request;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

public class ServiceRequestHandler extends HttpInvokerServiceExporter implements ApplicationContextAware {
	
	private ApplicationContext context;
	private Map<String,ServiceResolver> serviceMap;
	
	@Override
	public void afterPropertiesSet() {}

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}
	
	public void setServiceMap(Map<String,ServiceResolver> serviceMap) {
		this.serviceMap = serviceMap;
	}

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServiceRequestWrapper requestWrapper = new ServiceRequestWrapper(request);
		ServiceResolver resolver = getServiceResolver(requestWrapper);
		Object service = resolveService(resolver, requestWrapper);
		RemoteInvocation invocation = resolveInvocation(resolver, requestWrapper);
		RemoteInvocationResult result = invokeAndCreateResult(invocation, service);
		new ServiceResponseWrapper(response, result.getValue(), result.getException()).flushBuffer();
	}

	protected ServiceResolver getServiceResolver(ServiceRequestWrapper request) {
		String key = request.getServletPath();
		ServiceResolver resolver = serviceMap != null ? serviceMap.get(key) : null;
		if (resolver == null) {
			throw new FrontException("Unknown service type: " + key);
		}
		return resolver;
	}
	
	protected Object resolveService(ServiceResolver resolver, ServiceRequestWrapper request) {
		String key = resolver.resolveServicePath(request);
		try {
			return context.getBean(key);
		} catch (Exception e) {
			throw new FrontException("No service found for path: " + key, e);
		}
	}
	
	protected RemoteInvocation resolveInvocation(ServiceResolver resolver, ServiceRequestWrapper requestWrapper) throws IOException {
		Request request = resolver.parseRequest(requestWrapper);
		String methodName = request.getMethod();
		methodName = (methodName != null && methodName.length() != 0) ? methodName : resolver.resolveServiceMethod(requestWrapper);
		return new RemoteInvocation(methodName, request.getParameterTypes(), request.getParameterValues());
	}
	
}
