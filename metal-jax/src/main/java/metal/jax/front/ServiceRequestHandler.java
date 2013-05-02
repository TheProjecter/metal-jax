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
import metal.core.mapper.JavaType;
import metal.core.mapper.ModelMapper;
import metal.core.mapper.Property;
import metal.core.message.MessageMapper;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
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
			Property<String, Property<String, Class<?>>> methodDef = getRequestMethodDef(service, request);
			Property<String, Class<?>> paramDef = methodDef.getValue();
			Object param = getRequestParameter(request, paramDef);
			return invoke(methodDef.getKey(), target, param, paramDef);
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
	
	protected ResponseMessage invoke(String method, Object target, Object param, Property<String, Class<?>> paramDef) {
		RemoteInvocation invocation;
		Class<?> paramType = (paramDef != null) ? paramDef.getValue() : param != null ? param.getClass() : null;
		if (paramType != null) invocation = new RemoteInvocation(method, new Class<?>[]{paramType}, new Object[]{param});
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
	
	protected Property<String, Property<String, Class<?>>> getRequestMethodDef(Service service, ServiceRequest request) {
		return serviceRegistry.getServiceMethodDef(service.getServicePath(request), service.getRequestMethod(request));
	}
	
	protected Object getRequestParameter(ServiceRequest request, Property<String, Class<?>> paramDef) throws Exception {
		Object param = null;
		switch (HttpContentType.typeOf(request.getContentType())) {
		case XML:
			param = modelMapper.read(paramDef.getValue(), request.getInputStream());
			break;
		case FORM:
			if (paramDef != null) {
				Class<?> paramType = paramDef.getValue();
				switch (JavaType.typeOf(paramType)) {
				case OBJECT:
					param = paramType.newInstance();
					BeanUtils.populate(param, request.getParameterMap());
					break;
				default:
					String value = request.getParameter(paramDef.getKey());
					param = BeanUtilsBean.getInstance().getConvertUtils().convert(value, paramType);
					break;
				}
			}
			break;
		}
		return param;
	}
	
}
