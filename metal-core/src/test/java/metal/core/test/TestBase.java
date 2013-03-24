/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.test;

import static org.junit.Assert.assertEquals;

import metal.core.message.Message;
import metal.core.message.MessageMapper;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:conf/spring/*.xml")
public class TestBase {

	@Resource(name="metal-core.messageMapper")
	private MessageMapper mapper;
	
	protected InputStream source(String name) throws IOException {
		InputStream in = this.getClass().getResourceAsStream(name);
		if (in != null) return in;
		throw new IOException("not found: " + name);
	}
	
	protected void fail(Throwable error) {
		Throwable next = error;
		while (next != null) {
			if (next instanceof Message) {
				System.err.print(next.getClass().getName());
				System.err.print(": ");
				System.err.println(mapper.getMessage((Message)next));
			}
			next = next.getCause();
		}
		error.printStackTrace();
		Assert.fail(error.getMessage());
	}
	
	protected static void assertEqualsIgnoreWhitespace(String expected, String actual) {
		assertEquals(StringUtils.deleteWhitespace(expected), StringUtils.deleteWhitespace(actual));
	}
	
}
