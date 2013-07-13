/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.front.service;

import static metal.front.common.FrontMessageCode.*;
import static metal.front.common.HttpContentType.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import metal.core.common.AnyException;
import metal.core.mapper.ValueMapper;
import metal.core.mop.MethodDeclaration;
import metal.core.mop.NameDeclaration;
import metal.core.mop.ServiceRegistry;
import metal.core.validation.ValidationException;
import metal.core.validation.Validator;
import metal.front.common.DisplayMessageMapper;
import metal.front.common.FrontException;
import metal.front.common.HttpContentType;
import metal.front.model.RequestMessage;
import metal.front.model.ResponseMessage;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.web.HttpRequestHandler;

public class ServiceRequestHandler extends HttpInvokerServiceExporter implements HttpRequestHandler, ApplicationContextAware {
	
	private ApplicationContext context;
	private Map<String,ServiceSetting> serviceMap = Collections.emptyMap();
	
	@Resource
	private ServiceRegistry serviceRegistry;
	
	@Resource
	private DisplayMessageMapper messageMapper;
	
	@Resource
	private ValueMapper valueMapper;
	
	@Resource
	private Validator validator;
	
	@Override
	public void afterPropertiesSet() {}

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}
	
	public void setServiceMap(Map<String,ServiceSetting> serviceMap) {
		this.serviceMap = serviceMap;
	}

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServiceRequest serviceRequest = new ServiceRequest(request);
		ResponseMessage message = invokeService(serviceRequest);
		sendResponse(message, new ServiceResponse(response), serviceRequest.getExtName());
	}

	protected ResponseMessage invokeService(ServiceRequest request) {
		try {
			ServiceSetting service = serviceMap.get(request.getServletPath());
			Object target = getInvocationTarget(service, request);
			MethodDeclaration method = getInvocationMethod(service, request);
			Object[] params = getRequestParameters(request, method.getParamDeclarations());
			return invoke(method.getName(), target, params, method.getParamTypes());
		} catch (ValidationException ex) {
			return new ResponseMessage(null, messageMapper.createMessages(null, ex.getMessages()));
		} catch (AnyException ex) {
			return new ResponseMessage(null, messageMapper.createMessages(null, ex));
		} catch (Exception ex) {
			return new ResponseMessage(null, messageMapper.createMessages(null, ex));
		}
	}
	
	protected void sendResponse(ResponseMessage message, ServiceResponse response, String ext) throws IOException {
		HttpContentType httpContentType = HttpContentType.typeOf(ext, HttpContentType.JSON);
		response.setContentType(httpContentType.contentType);
		valueMapper.write(message, response.getOutputStream(), httpContentType.name());
		response.flushBuffer();
	}
	
	protected ResponseMessage invoke(String method, Object target, Object[] params, Class<?>[] paramTypes) {
		RemoteInvocation invocation = new RemoteInvocation(method, paramTypes, params);
		RemoteInvocationResult result = invokeAndCreateResult(invocation, target);
		Throwable ex = result.getException();
		if (ex instanceof AnyException) {
			return new ResponseMessage(result.getValue(), messageMapper.createMessages(null, ex));
		} else if (ex != null) {
			return new ResponseMessage(result.getValue(), messageMapper.createMessages(null, ex));
		} else {
			return new ResponseMessage(result.getValue());
		}
	}
	
	protected Object getInvocationTarget(ServiceSetting service, ServiceRequest request) {
		if (service == null) {
			throw new FrontException(UndefinedService, request.getServletPath());
		}
		String servicePath = service.getServicePath(request);
		try {
			String beanName = serviceRegistry.getServiceBeanName(servicePath);
			return context.getBean(beanName);
		} catch (Exception e) {
			throw new FrontException(UndefinedService, e, servicePath);
		}
	}
	
	protected MethodDeclaration getInvocationMethod(ServiceSetting service, ServiceRequest request) {
		return serviceRegistry.getServiceMethodDeclaration(service.getServicePath(request), service.getRequestMethod(request));
	}
	
	protected Object[] getRequestParameters(ServiceRequest request, NameDeclaration[] params) throws Exception {
		Object[] values = null;
		RequestMessage message = new RequestMessage(params);
		HttpContentType contentType = typeOf(request.getContentType(), FORM);
		switch (contentType) {
		case FORM:
			BeanUtils.populate(message, request.getParameterMap());
			values = message.getValues();
			break;
		default:
			values = valueMapper.read(message, request.getInputStream(), contentType.name()).getValues();
			break;
		}
		validator.assertValid(values);
		return values;
	}
	
}
