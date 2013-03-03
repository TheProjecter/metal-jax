/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.demo.service;

import metal.demo.model.HelloModel;
public class EchoImpl implements EchoService {
	
	public HelloModel hello(HelloModel model) {
		model.setName1("echo: " + model.getName1());
		model.setName2("echo: " + model.getName2());
		model.setName3("echo: " + model.getName3());
		return model;
	}
	
}
