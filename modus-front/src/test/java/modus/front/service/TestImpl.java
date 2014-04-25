/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package modus.front.service;

import org.springframework.stereotype.Service;

@Service("modus.front.service.TestService")
public class TestImpl implements TestService {

	public String hello(Long message, String from) {
		return "test: " + message + ", from: " + from;
	}
	
}
