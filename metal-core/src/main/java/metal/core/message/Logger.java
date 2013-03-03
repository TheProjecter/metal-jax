/**
 * @copyright Jay Tang 2012
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.message;

import org.slf4j.LoggerFactory;

public class Logger {

	public static final String TRACE_ENTRY = "entry";
	public static final String TRACE_EXIT = "exit";

	private boolean errorEnabled = true;
	private boolean warnEnabled = true;
	private boolean infoEnabled = true;
	private boolean debugEnabled = false;
	private boolean traceEnabled = false;
	private boolean stackTraceEnabled = false;

	public void setDebugEnabled(boolean debugEnabled) {
		this.debugEnabled = debugEnabled;
	}

	public void setErrorEnabled(boolean errorEnabled) {
		this.errorEnabled = errorEnabled;
	}

	public void setInfoEnabled(boolean infoEnabled) {
		this.infoEnabled = infoEnabled;
	}

	public void setStackTraceEnabled(boolean stackTraceEnabled) {
		this.stackTraceEnabled = stackTraceEnabled;
	}

	public void setTraceEnabled(boolean traceEnabled) {
		this.traceEnabled = traceEnabled;
	}

	public void setWarnEnabled(boolean warnEnabled) {
		this.warnEnabled = warnEnabled;
	}

	private org.slf4j.Logger getLogger(Object target) {
		return LoggerFactory.getLogger(target.getClass());
	}

	public void logError(Object target, String method, Throwable ex) {
		if (errorEnabled) {
			org.slf4j.Logger logger = getLogger(target);
			if (logger.isErrorEnabled()) {
				if (stackTraceEnabled) {
					logger.error("exception while calling " + method, ex);
				} else {
					logger.error("exception while calling " + method, ex.getMessage());
				}
			}
		}
	}

	public void logDebug(Object target, String method, String message) {
		if (debugEnabled) {
			org.slf4j.Logger logger = getLogger(target);
			if (logger.isDebugEnabled()) {
				logger.debug(method + ": " + message);
			}
		}
	}

	public void logInfo(Object target, String method, String message) {
		if (infoEnabled) {
			org.slf4j.Logger logger = getLogger(target);
			if (logger.isInfoEnabled()) {
				logger.info(method + ": " + message);
			}
		}
	}

	public void logTrace(Object target, String method, String message) {
		if (traceEnabled) {
			org.slf4j.Logger logger = getLogger(target);
			if (logger.isTraceEnabled()) {
				logger.trace(method + ": " + message);
			}
		}
	}

	public void logWarn(Object target, String method, String message) {
		if (warnEnabled) {
			org.slf4j.Logger logger = getLogger(target);
			if (logger.isWarnEnabled()) {
				logger.warn(method + ": " + message);
			}
		}
	}

}
