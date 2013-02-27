/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.message;

import javax.annotation.Resource;

import metal.core.test.TestBase;

import org.junit.Test;

public class LoggerTest extends TestBase {

	@Resource(name="metal-core.logger")
	private Logger logger;
	
	@Test
	public void testLogger() {
		logger.logTrace(this, "logTrace", "a test message");
		logger.logDebug(this, "logDebug", "a test message");
		logger.logInfo(this, "logInfo", "a test message");
		logger.logWarn(this, "logWarn", "a test message");
		logger.logError(this, "logError", new Exception("a test message"));
	}

}
