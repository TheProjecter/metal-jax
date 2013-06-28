/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.front.service;

import javax.servlet.http.HttpServletResponse;

import metal.front.common.BufferedResponse;

public class ServiceResponse extends BufferedResponse {
	
	public ServiceResponse(HttpServletResponse response) {
		super(response);
	}
	
}
