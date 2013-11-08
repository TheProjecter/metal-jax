/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.front.common;

public enum HttpHeaderField {
	CacheControl("Cache-Control", "public,max-age=300");
	
	public final String name;
	public final String value;
	private HttpHeaderField(String name, String value) {
		this.name = name; 
		this.value = value;
	}
	
}
