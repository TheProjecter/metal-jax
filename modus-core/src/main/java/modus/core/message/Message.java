/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package modus.core.message;

import org.springframework.context.MessageSourceResolvable;

public interface Message extends MessageSourceResolvable {
	MessageCode getCode();
}
