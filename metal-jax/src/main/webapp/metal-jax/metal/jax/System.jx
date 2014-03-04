/**
 * @class
 * @imports Context
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@private
function initClass(scope) {
	Array.prototype.indexOf = Array.prototype.indexOf || indexOf;
	Date.prototype.toISOString = Date.prototype.toISOString || toISOString;
	$class.$window = window;
	$class.$document = window.document;
	$class.$head = window.document.body.parentNode.firstChild;
	// mixes
	$class.splitPath = Context.splitPath;
	$class.resolvePath = Context.resolvePath;
	$class.isSameDomain = Context.isSameDomain;
	$class.formatText = Context.formatText;
	$class.parseBaseName = Context.parseBaseName;
	$class.parseSourceName = Context.parseSourceName;
}

//@static
function callback() {
	var target = this, args = arguments;
	var action = Array.prototype.shift.call(arguments);
	var callback = (typeof action == "string") ? target[action] : action;
	if (callback) {
		return function() {
			Array.prototype.unshift.apply(arguments, args);
			return callback.apply(target === $class ? this : target, arguments);
		};
	}
}

//@static
function loadback(source, callback) {
	var action = (!source.url && $class.isSameDomain(source.base)) ? loadSameDomain : loadCrossDomain;
	action.call(this, source, callback);
}

//@private
function loadSameDomain(source, callback) {
	var caller = this;
	var request = requestSameDomain(source, function() {
		if (!request || request.readyState != 4) return;
		try {
			if (request.status == 200 || request.status == 0) {
				source.content = (source.resourceType == "xml") ? request.responseXML.firstChild ?
					checkXML(request.responseXML) : parseXML(request.responseText) : request.responseText;
			} else {
				source.status = request.status;
				source.error = request.statusText;
			}
		} catch (ex) {
			source.error = ex.message;
		}
		callback.call(caller, source);
	});
	try {
		request.send(requestInput(source));
	} catch (ex) {
		throwError("failed to load URL: ${0}, error: ${1}", source.url, ex.message);
	}
}

//@private
function requestSameDomain(source, callback) {
	var request;
	if (typeof XMLHttpRequest != "undefined" && typeof ActiveXObject == "undefined") {
		request = new XMLHttpRequest();
	} else if (typeof XMLHttpRequest != "undefined" && typeof ActiveXObject != "undefined" && $("baseScheme") != "file") {
		request = new XMLHttpRequest();
	} else if (typeof ActiveXObject != "undefined") {
		request = new ActiveXObject("Microsoft.XMLHTTP");
	} else {
		throwError("XHR not supported");
	}
	var path = source.path.charAt(0) == "/" ? source.path : "/".concat(source.path);
	source.url = [source.base, source.module].join("/").concat(path);
	switch (requestMethod(source)) {
	case "GET":
		request.open("GET", requestURL(source), true);
		break;
	case "POST":
		request.open("POST", source.url, true);
		request.setRequestHeader("Content-Type", source.contentType || "application/x-www-form-urlencoded");
		request.setRequestHeader("Content-Length", requestInput(source).length);
		break;
	}
	request.onreadystatechange = callback;
	return request;
}

//@private
function loadCrossDomain(source, callback) {
	var caller = this;
	var request = requestCrossDomain(source, function(response) {
		var done = (source.$class||$class).checkResource();
		var timeout = request.time > request.timeout;
		if (!done && !timeout && typeof response != "object") {
			request.time += request.time;
			request.timeoutId = setTimeout(request.callback, request.time);
			return;
		}
		clearRequest(request, source);
		if (done) {
			source.content = "";
		} else if (timeout) {
			source.error = "request timeout";
		} else if (response.error) {
			source.error = response.error;
		} else {
			try {
				source.content = (source.resourceType == "xml") ?
					parseXML(response.content) : response.content;
			} catch (ex) {
				source.error = ex.message;
			}
		}
		callback.call(caller, source);
	});
	$class.$head.appendChild(source.node);
}

//@private
function requestCrossDomain(source, callback) {
	var request = {};
	(source.$class||$class).initResourceHandle(source);
	request.callback = callback;
	request.time = parseInt($("requestTime"));
	request.timeout = parseInt($("requestTimeout"));
	request.timeoutId = setTimeout(request.callback, request.time);
	$class.$window[$("bootModule")][source.url] = request.callback;
	return request;
}

//@private
function clearRequest(request, source) {
	clearTimeout(request.timeoutId);
	delete $class.$window[$("bootModule")][source.url];
	(source.$class||$class).clearResourceHandle(source);
}

//@static
function initResourceHandle(source) {
	source.url = source.url || [source.base, $("crossDomain"), source.module, source.path].join("/");
	source.node = $class.$document.createElement("script");
	source.node.src = requestURL(source);
}

//@static
function clearResourceHandle(source) {
	$class.$head.removeChild(source.node);
	delete source.node;
}

//@static
function checkResource() {
	//empty
}

//@private
function requestMethod(source) {
	return source.method && source.method.toUpperCase() == "POST" ? "POST" : "GET";
}

//@private
function requestInput(source) {
	return requestMethod(source) == "POST" ? (source.input||"") : null;
}

//@private
function requestURL(source) {
	return source.input ? source.url.concat("?", source.input) : source.url;
}

//@static
function parseValue(text, type) {
	switch (type) {
	case "boolean":
		return text == "true";
	case "date":
		return Date.parse(text);
	case "float":
		return parseFloat(text);
	case "integer":
		return parseInt(text);
	case "string":
	default:
		return text;
	}
}

//@private
var _lineRE_ = /(.*)(?:\r?\n)?/g;

//@static
function parseCSV(text, delimiter) {
	delimiter = delimiter || "\t";
	var result = [], match;
	while ((match = _lineRE_.exec(text)) && match[1]) {
		result.push(match[1].split(delimiter));
	}
	return result;
}

//@private
var _settingRE_= /\s*([-\w.]+)\s*=\s*(.+)(?:\r?\n)?/g;

//@static
function parseProperties(text) {
	var result = {}, match;
	while ((match = _settingRE_.exec(text))) {
		result[match[1]] = match[2];
	}
	return result;
}

//@static
function parseJSONFromText(text) {
	return eval("(".concat(text, ")"));
}

//@private
var _spacesRE_ = /^\s*$/;

//@static
function parseJSONFromXML(node) {
	var result = {};
	for (var i = 0; i < node.childNodes.length; i++) {
		var childNode = node.childNodes[i];
		if (childNode.nodeType == 1) {
			result[childNode.nodeName] = parseJSONValueFromXML(childNode);
		}
	}
	return result;
}

//@private
function parseJSONValueFromXML(node) {
	var index = node.childNodes.length == 1 ? 0 : indexOfFirstChild(node, true);
	if (index >= 0) {
		node = node.childNodes[index];
		return (node.nodeType == 3) ? node.data : parseJSONFromXML(node);
	}
	var result = [];
	for (var i = 0; i < node.childNodes.length; i++) {
		var childNode = node.childNodes[i];
		if (childNode.nodeType == 1) {
			result.push(parseJSONFromXML(childNode));
		}
	}
	return result;
}

//@static
function indexOfFirstChild(node, only) {
	var index = -1;
	for (var i = 0; i < node.childNodes.length; i++) {
		if (node.childNodes[i].nodeType == 1) {
			if (!only) return i;
			if (index >= 0) return -1;
			index = i;
		}
	}
	return index;
}

//@static
function parseHTML(text) {
	var doc = $class.$document.createDocumentFragment();
	var node = $class.$document.createElement("div");
	node.innerHTML = text;
	while (node.firstChild) {
		doc.appendChild(node.firstChild);
	}
	return doc;
}

//@static
function parseXML(text) {
	if (typeof DOMParser != "undefined") {
		return checkXML(new DOMParser().parseFromString(text, "text/xml"));
	} else if (typeof ActiveXObject != "undefined") {
		var doc = new ActiveXObject("Microsoft.XMLDOM");
		doc.async = false;
		doc.loadXML(text);
		return checkXML(doc);
	} else {
		throwError("XML parsing not supported");
	}
}

//@private
function checkXML(doc) {
	var content, error;
	if (doc.documentElement.nodeName == "parsererror") {
		error = doc.documentElement.childNodes[0].data;
		content = doc.documentElement.childNodes[1].childNodes[0].data;
		throwError("XML Error: ${0}, content: ${1}", error, content);
	} else if (doc.parseError && doc.parseError.errorCode) {
		error = doc.parseError;
		content = error.srcText;
		throwError("XML Error: ${0}, at line: ${1}, column: ${2}, content: ${3}",
				error.reason, error.line, error.linepos, content);
	}
	return doc;
}

//@static
function formatXML(doc) {
	if (typeof XMLSerializer != "undefined") {
		return new XMLSerializer().serializeToString(doc);
	} else if (typeof doc.xml != "undefined") {
		return doc.xml;
	} else {
		throwError("XML serialization not supported");
	}
}

//@static
function indexOf(item, index) {
	index = index || 0;
	for ( ; index < this.length; index++) {
		if (index in this && this[index] === item) {
			return index;
		}
	};
	return -1;
}

//@static
function copyArray(items) {
	//return Array.prototype.slice.call(items, 0);
	var result = [];
	for (var i = 0; i < items.length; i++) result.push(items[i]);
	return result;
}

//@static
function toISOString() {
	return $class.formatText("${0}-${1}-${2}T${3}:${4}:${5}.${6}Z",
			this.getUTCFullYear(), pad(this.getUTCMonth()+1, 2), pad(this.getUTCDate(), 2),
			pad(this.getUTCHours(), 2), pad(this.getUTCMinutes(), 2), pad(this.getUTCSeconds(), 2),
			pad(this.getUTCMilliseconds(), 3));
}

//@private
function pad(number, digits) {
	number = "".concat(number);
	while (number.length < digits) {
		number = "0" + number;
	}
	return number;
}
