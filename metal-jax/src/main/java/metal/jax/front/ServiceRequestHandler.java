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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

public class ServiceRequestHandler extends HttpInvokerServiceExporter implements ApplicationContextAware {
	
	private ApplicationContext context;
	private Map<String,Service> serviceMap = Collections.emptyMap();
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

	public void setMessageMapper(MessageMapper messageMapper) {
		this.messageMapper = messageMapper;
	}

	public void setModelMapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServiceRequest serviceRequest = new ServiceRequest(request);
		Service service = serviceMap.get(serviceRequest.getServletPath());
		ResponseMessage message = invokeService(service, serviceRequest);
		new ServiceResponse(response, message).flushBuffer();
	}

	protected ResponseMessage invokeService(Service service, ServiceRequest request) {
		try {
			String method = service.getRequestMethod(request);
			Object target = getInvocationTarget(service, request);
			RequestMessage message = modelMapper.read(RequestMessage.class, request.getInputStream());
			return invoke(method, target, message);
		} catch (AnyException ex) {
			return new ResponseMessage(null, messageMapper.getMessage(ex), ex.getMessage());
		} catch (Exception ex) {
			return new ResponseMessage(null, ex.getMessage(), ex.getMessage());
		}
	}
	
	protected ResponseMessage invoke(String method, Object target, RequestMessage message) {
		method = StringUtils.isEmpty(message.getMethod()) ? method : message.getMethod();
		RemoteInvocation invocation = new RemoteInvocation(method, message.getParameterTypes(), message.getParameterValues());
		RemoteInvocationResult result = invokeAndCreateResult(invocation, target);
		Throwable ex = result.getException();
		if (ex instanceof AnyException) {
			return new ResponseMessage(result.getValue(), messageMapper.getMessage((AnyException)ex), ex.getMessage());
		} else if (ex != null) {
			return new ResponseMessage(result.getValue(), ex.getMessage(), ex.getMessage());
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
			return context.getBean(servicePath);
		} catch (Exception e) {
			throw new FrontException(UnknownServicePath, e, servicePath);
		}
	}
	
}
