/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.aop;

import static metal.core.aop.AopMessageCode.*;

import metal.core.common.AnyException;

public class ExceptionAdvice {

	public void handle(Exception ex) {
		if (ex instanceof AnyException) throw (AnyException) ex;
		throw new AopException(UnhandledException, ex, ex.getClass().getName(), ex.getMessage());
	}

}
