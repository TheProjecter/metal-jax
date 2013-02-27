/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Map;

import javax.annotation.Resource;

import metal.core.test.TestBase;

import org.junit.Test;

public class MapUnionTest extends TestBase {

	@Resource(name="test-core.testMapUnion")
	private Map<String,String> map;
	
	@Test
	public void testMapUnion() {
		assertEquals(map.size(), 3);
		assertTrue(map.keySet().containsAll(Arrays.asList("name1", "name2", "name3")));
		assertTrue(map.values().containsAll(Arrays.asList("value1", "value2", "value3")));
	}

}
