/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package modus.demo.service;

import org.springframework.stereotype.Service;

import modus.demo.model.Hello;

@Service("modus.demo.service.EchoService")
public class EchoImpl implements EchoService {
	
	public Hello hello(Hello model) {
		model.setName1("echo: " + model.getName1());
		model.setName2("echo: " + model.getName2());
		model.setName3("echo: " + model.getName3());
		return model;
	}
	
}
