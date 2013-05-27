/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.jax.front;

import static metal.jax.front.FrontMessageCode.*;
import static metal.jax.front.HttpContentType.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import metal.core.common.AnyException;
import metal.core.mapper.ContentMapper;
import metal.core.message.MessageMapper;
import metal.core.mop.MethodDeclaration;
import metal.core.mop.NameDeclaration;
import metal.core.mop.ServiceRegistry;
import metal.jax.front.model.RequestMessage;
import metal.jax.front.model.ResponseMessage;

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
	private ContentMapper contentMapper;
	
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

	public void setContentMapper(ContentMapper contentMapper) {
		this.contentMapper = contentMapper;
	}

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServiceRequest serviceRequest = new ServiceRequest(request);
		ResponseMessage message = invokeService(serviceRequest);
		sendResponse(message, new ServiceResponse(response), serviceRequest.getExtName());
	}

	protected ResponseMessage invokeService(ServiceRequest request) {
		try {
			Service service = serviceMap.get(request.getServletPath());
			Object target = getInvocationTarget(service, request);
			MethodDeclaration method = getInvocationMethod(service, request);
			Object[] params = getRequestParameters(request, method.getParamDeclarations());
			return invoke(method.getName(), target, params, method.getParamTypes());
		} catch (AnyException ex) {
			return new ResponseMessage(null, messageMapper.getMessage(ex), ex.getMessage());
		} catch (Exception ex) {
			return new ResponseMessage(null, ex.getMessage(), ex.getClass().getSimpleName());
		}
	}
	
	protected void sendResponse(ResponseMessage message, ServiceResponse response, String ext) throws IOException {
		String contentType = HttpContentType.typeOf(ext, HttpContentType.JSON).name();
		contentMapper.write(message, response.getOutputStream(), contentType);
		response.flushBuffer();
	}
	
	protected ResponseMessage invoke(String method, Object target, Object[] params, Class<?>[] paramTypes) {
		RemoteInvocation invocation = new RemoteInvocation(method, paramTypes, params);
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
			values = contentMapper.read(message, request.getInputStream(), contentType.name()).getValues();
			break;
		}
		return values;
	}
	
}
