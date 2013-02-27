/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.test;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;

import java.io.InputStream;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:conf/spring/*.xml")
public class TestBase {

	protected InputStream source(String name) {
		return this.getClass().getResourceAsStream(name);
	}
	
	protected static void fail(Throwable error) {
		error.printStackTrace();
		Assert.fail(error.getMessage());
	}
	
	protected static void assertEqualsIgnoreWhitespace(String expected, String actual) {
		assertEquals(StringUtils.deleteWhitespace(expected), StringUtils.deleteWhitespace(actual));
	}
	
}
