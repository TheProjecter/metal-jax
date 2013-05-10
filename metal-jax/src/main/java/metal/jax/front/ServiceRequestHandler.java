/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

import static metal.jax.front.FrontMessageCode.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import metal.core.common.AnyException;
import metal.core.mapper.ModelMapper;
import metal.core.message.MessageMapper;
import metal.core.mop.MethodDeclaration;
import metal.core.mop.NameDeclaration;
import metal.core.mop.ServiceRegistry;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

public class ServiceRequestHandler extends HttpInvokerServiceExporter implements ApplicationContextAware {
	
	private ApplicationContext context;
	private Map<String,Service> serviceMap = Collections.emptyMap();
	private ServiceRegistry serviceRegistry;
	private MessageMapper messageMapper;
	private ModelMapper modelMapper;
	
	@Override
	public void afterPropertiesSet() {}

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}
	
	public void setServiceMap(Map<String,Service> serviceMap) {
		this.serviceMap = serviceMap;
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	public void setMessageMapper(MessageMapper messageMapper) {
		this.messageMapper = messageMapper;
	}

	public void setModelMapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServiceRequest serviceRequest = new ServiceRequest(request);
		ResponseMessage message = invokeService(serviceRequest);
		sendResponse(message, new ServiceResponse(response));
	}

	protected ResponseMessage invokeService(ServiceRequest request) {
		try {
			Service service = serviceMap.get(request.getServletPath());
			Object target = getInvocationTarget(service, request);
			MethodDeclaration methodDecl = getInvocationMethod(service, request);
			Object[] params = getRequestParameters(request, methodDecl.getParamDeclarations());
			return invoke(methodDecl.getName(), target, params, methodDecl.getParamTypes());
		} catch (AnyException ex) {
			return new ResponseMessage(null, messageMapper.getMessage(ex), ex.getMessage());
		} catch (Exception ex) {
			return new ResponseMessage(null, ex.getMessage(), ex.getClass().getSimpleName());
		}
	}
	
	protected void sendResponse(ResponseMessage message, ServiceResponse response) throws IOException {
		modelMapper.write(message, response.getOutputStream());
		response.flushBuffer();
	}
	
	protected ResponseMessage invoke(String method, Object target, Object[] params, Class<?>[] paramTypes) {
		RemoteInvocation invocation;
		if (paramTypes != null) invocation = new RemoteInvocation(method, paramTypes, params);
		else invocation = new RemoteInvocation(method, new Class<?>[0], new Object[0]);
		RemoteInvocationResult result = invokeAndCreateResult(invocation, target);
		Throwable ex = result.getException();
		if (ex instanceof AnyException) {
			return new ResponseMessage(result.getValue(), messageMapper.getMessage((AnyException)ex), ex.getMessage());
		} else if (ex != null) {
			return new ResponseMessage(result.getValue(), ex.toString(), ex.getMessage());
		} else {
			return new ResponseMessage(result.getValue(), null, null);
		}
	}
	
	protected Object getInvocationTarget(Service service, ServiceRequest request) {
		if (service == null) {
			throw new FrontException(UnknownService, request.getServletPath());
		}
		String servicePath = service.getServicePath(request);
		try {
			String beanName = serviceRegistry.getServiceBeanName(servicePath);
			return context.getBean(beanName);
		} catch (Exception e) {
			throw new FrontException(UnknownServicePath, e, servicePath);
		}
	}
	
	protected MethodDeclaration getInvocationMethod(Service service, ServiceRequest request) {
		return serviceRegistry.getServiceMethodSetting(service.getServicePath(request), service.getRequestMethod(request));
	}
	
	protected Object[] getRequestParameters(ServiceRequest request, NameDeclaration[] paramDecls) throws Exception {
		Object[] params = null;
		RequestMessage message = new RequestMessage(paramDecls);
		switch (HttpContentType.typeOf(request.getContentType())) {
		case XML:
			params = modelMapper.read(message, request.getInputStream()).getValues();
			break;
		case FORM:
			BeanUtils.populate(message, request.getParameterMap());
			params = message.getValues();
			break;
		}
		return params;
	}
	
}
