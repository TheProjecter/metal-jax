/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.aop;

import javax.annotation.Resource;

import metal.core.common.AnyException;
import metal.core.message.Logger;

import org.aspectj.lang.ProceedingJoinPoint;

public class InvocationAdvice {

	@Resource
	private Logger logger;

	public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
		try {
			if (logger != null) logger.logTrace(pjp.getTarget(), pjp.getSignature().getName(), Logger.TRACE_ENTRY);
			return pjp.proceed();
		} catch (AnyException ex) {
			if (logger != null) logger.logError(pjp.getTarget(), pjp.getSignature().getName(), ex);
			throw ex;
		} catch (Exception ex) {
			if (logger != null) logger.logError(pjp.getTarget(), pjp.getSignature().getName(), ex);
			throw ex;
		} finally {
			if (logger != null) logger.logTrace(pjp.getTarget(), pjp.getSignature().getName(), Logger.TRACE_EXIT);
		}
	}

}
