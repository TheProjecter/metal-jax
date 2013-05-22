/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mapper;

import java.io.InputStream;

import metal.core.mop.Model;

public interface Reader {

	<T> T read(Class<T> type, InputStream input);

	<T> T read(Class<T> type, String input);

	<T extends Model> T read(T value, InputStream input);

	<T extends Model> T read(T value, String input);

}
