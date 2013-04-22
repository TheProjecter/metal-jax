/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.common;

import java.beans.Introspector;

import org.apache.commons.lang.StringUtils;

public class XmlAnnotationUtils {

	public static final String DEFAULT = "##default";
	
	public static String ensureName(String name, String defaultName) {
		if (StringUtils.isEmpty(name) || StringUtils.equals(name, DEFAULT)) {
			name = defaultName;
			if (!Character.isLowerCase(name.charAt(0))) {
				name = Introspector.decapitalize(name);
			}
		}
		return name;
	}

}
