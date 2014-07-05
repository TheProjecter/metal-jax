/**
 * @resource
 * @imports Controller
 * @imports modus.core.System
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@static
function parseSource(source) {
	var node = System.$document.createElement("div");
	node.innerHTML = source.content;
	source.model = toDocFrag(node);
}

//@protected
function initResource(scope, source) {
	copy(scope.$imports, source.$imports);
	copy(scope.$requires, source.$requires);
}

//@static
function parseModel(source, node) {
	node = node || source.model;
	source.$imports = source.$imports || {};
	source.$requires = source.$requires || {};
	for (var i = 0; i < node.childNodes.length; i++) {
		var childNode = node.childNodes[i];
		if (childNode.nodeType == 1) {
			var done = parseNode(source, childNode);
			if (!done) {
				parseModel(source, childNode);
			}
		}
	}
}

//@private
function parseNode(source, node) {
	if (parseResourceNode(source, node)) {
		return true;
	}
	var setting = parseViewSetting(node, source.name);
	if (setting.controller) {
		source.$imports[setting.controller] = setting.controller;
	}
	if (setting.view == source.name) {
		log("warn", "modus.resource.cycle", setting.view);
	} else if (setting.view) {
		source.$imports[$class.getName()] = $class.getName();
		source.$requires[setting.view] = $class.getName();
	}
}

//@private
function parseResourceNode(source, node) {
	if (node.parentNode.nodeName.toLowerCase() != "body" && !node.parentNode.offsetParent) {
		if (node.nodeName.toLowerCase() == "img") {
			node.src = resolvePath(node.attributes["alt"].value||node.attributes["src"].value, source);
			return true;
		} else if (node.nodeName.toLowerCase() == "link") {
			System.$head.appendChild(node);
			node.href = resolvePath(node.attributes["href"].value, source);
			return true;
		}
	}
}

//@private
var _settingRE_ = /([^:]+)(?::|\s*)(.*)/;

//@private
function parseViewSetting(node, view) {
	var setting = {}, match;
	var tokens = node.className && node.className.split(" ") || [];
	for (var i = 0; i < tokens.length; i++) {
		if (match = _settingRE_.exec(tokens[i])) {
			setting[match[1]] = match[2];
		}
	}
	if ("view" in setting) {
		setting.view = System.parseSourceName(setting.view, view);
	}
	if ("controller" in setting) {
		setting.controller = System.parseSourceName(setting.controller, view);
		if ("view" in setting) {
			setting.view = setting.view || setting.controller;
			setting.controller = setting.controller || setting.view;
		} else {
			setting.view = "";
		}
	}
	return setting;
}

//@private
function initObject(view, node, controller, setting) {
	view.node = node;
	view.controller = controller;
	view.setting = setting || {};
	view.nodes = {};
	view.parts = {};
	view.placeholders = [];
	controller.init(view);
	initModel(view, node);
	afterInit(view);
	controller.afterInit(view);
}

//@private
function initModel(view, node) {
	for (var i = 0; i < node.childNodes.length; i++) {
		var childNode = node.childNodes[i];
		if (childNode.nodeType == 1) {
			var done = initNode(view, childNode);
			view.controller.bind(view, childNode);
			if (!done) {
				initModel(view, childNode);
			}
		}
	}
}

//@private
function initNode(view, node) {
	var setting = parseViewSetting(node, view.setting.view);
	if (node.id) view.nodes[node.id] = node;
	if ("part" in setting) {
		view.parts[setting.part] = view.parts[setting.part] || {count:0};
		view.parts[setting.part].node = node;
	} else if ("placeholder" in setting) {
		view.placeholders.push({part:setting.placeholder, node:node});
		view.parts[setting.placeholder] = view.parts[setting.placeholder] || {count:0};
		view.parts[setting.placeholder].count++;
	} else if ("view" in setting) {
		var controllerClass = view.getClass().getContext().findClass(setting.controller);
		var controller = controllerClass ? new controllerClass() : view.controller;
		
		if (setting.view) {
			var resource = $class.findResource(setting.view, true).cloneNode(true);
			if (resource) {
				var part = toDocFrag(node, System.$document.createElement("div"));
				part.className = "part";
				node.appendChild(resource);
				node.appendChild(part);
			}
		}
		view.views = view.views || {};
		view.views[node.id] = new $class(node, controller, setting);
		return true;
	}
}

//@private
function afterInit(view) {
	for (var i = 0; i < view.placeholders.length; i++) {
		var placeholder = view.placeholders[i];
		var part = view.parts[placeholder.part];
		if (part && part.node) {
			if (part.node.parentNode) part.node.parentNode.removeChild(part.node);
			replace(placeholder, part);
		}
	}
}

//@private
function replace(placeholder, part) {
	part.count--;
	var partNode = part.count ? part.node.cloneNode(true) : part.node;
	var parts = toArray(partNode);
	if (parts.length) {
		var frag = toDocFrag(placeholder.node);
		for (var i = 0; i < parts.length; i++) {
			var count = parts.length-i-1;
			var placemark = count ? frag.cloneNode(true) : frag;
			findPlacemark(placemark).appendChild(parts[i]);
			placeholder.node.appendChild(placemark);
		}
	}
}

//@private
function findPlacemark(placemark) {
	return findNodeByStyles(placemark, ["placemark"]) || placemark;
}

//@private
function findNodeByStyles(node, styles) {
	if (_filterStyle(node, styles)) return node;
	for (var i = 0; i < node.childNodes.length; i++) {
		var found = findNodeByStyles(node.childNodes[i], styles);
		if (found) return found;
	}
}

//@static
function toDocFrag(source, doc) {
	var doc = doc || System.$document.createDocumentFragment();
	while (source.firstChild) {
		if (source.firstChild.nodeType === 1) {
			doc.appendChild(source.firstChild);
		} else {
			source.removeChild(source.firstChild);
		}
	}
	return doc;
}

//@private
function toArray(source) {
	var result = [];
	for (var i = 0; i < source.childNodes.length; i++) {
		if (source.childNodes[i].nodeType === 1) {
			result.push(source.childNodes[i]);
		}
	}
	return result;
}

//@public
function refresh(content) {
	if (this.controller && content) {
		this.controller.refresh(this, content);
	}
	return this;
}

//@private
function resolvePath(path, source) {
	var fromBase = path && path.indexOf("../") == 0;
	if (fromBase) {
		path = System.splitPath(path, 0, 1 - source.path.split("/").length);
	} else {
		path = System.resolvePath(path, source.path);
	}
	return System.resolvePath(path, source.module + "/");
}

//@public
function toggleStyle(node, a, b) {
	a = a || ""; b = b || "";
	var tokens = node.className && node.className.split(" ") || [];
	var indexa = tokens.indexOf(a);
	var indexb = tokens.indexOf(b);
	if (indexa >= 0) {
		if (b && indexb < 0) {
			tokens.splice(indexa, 1, b);
		} else if (indexa != indexb) {
			tokens.splice(indexa, 1);
		}
	} else if (a) {
		if (indexb >= 0) {
			tokens.splice(indexb, 1, a);
		} else if (b) {
			tokens.push(b);
		} else {
			tokens.push(a);
		}
	}
	if (a) {
		node.className = tokens.join(" ");
	}
}

//@public
function filterStyle(node, styles, defaultToFirst) {
	return _filterStyle(node, styles, defaultToFirst);
}

//@private
function _filterStyle(node, styles, defaultToFirst) {
	var tokens = node.className && node.className.split(" ") || [];
	for (var i = 0; i < styles.length; i++) {
		if (tokens.indexOf(styles[i]) >= 0) {
			return styles[i];
		}
	}
	return defaultToFirst && styles[0] || "";
}

//@public
function filterSetting(setting, names, defaultToFirst) {
	for (var i = 0; i < names.length; i++) {
		if (names[i] in setting) {
			return setting[names[i]] || names[i];
		}
	}
	return defaultToFirst && names[0] || "";
}

//@public
function indexOfChild(node, child) {
	for (var i = 0, c = 0; i < node.childNodes.length; i++) {
		var childNode = node.childNodes[i];
		if (childNode.nodeType == 1) {
			if (childNode == child) return c;
			else c++;
		}
	}
}

//@public
function getChildByIndex(node, index) {
	for (var i = 0, c = 0; i < node.childNodes.length; i++) {
		var childNode = node.childNodes[i];
		if (childNode.nodeType == 1) {
			if (c == index) return childNode;
			else c++;
		}
	}
}

//@protected
function indexOfChildById(node, id) {
	for (var i = 0, c = 0; i < node.childNodes.length; i++) {
		var childNode = node.childNodes[i];
		if (childNode.nodeType == 1) {
			if (childNode.id == id) return c;
			else c++;
		}
	}
}

//@public
function getChildById(node, id) {
	for (var i = 0; i < node.childNodes.length; i++) {
		var childNode = node.childNodes[i];
		if (childNode.nodeType == 1) {
			if (childNode.id == id) return childNode;
		}
	}
}

//@public
function clear(nodeId) {
	return clearHTML(this.nodes[nodeId]);
}

//@protected
function clearHTML(node) {
	if (node) {
		while (node.firstChild) node.removeChild(node.firstChild);
		return node;
	}
}

//@private
var _events_ = [
	"keydown", "keypress", "keyup",
	"click", "dblclick",
	"mousedown", "mouseup", "mousemove", "mouseout", "mouseover"
];

//@private
var _touchEvents_ = [
	"touchstart", "touchend", "touchmove", "touchenter", "touchleave", "touchcancel"
];

//@protected
function isTouchReady() {
	return typeof TouchEvent != "undefined";
}

//@public
function toggleEvent(event, callback, positive, target) {
	positive = positive || arguments.length == 2;
	target = target || System.$document;
	if (callback) {
		if (target.addEventListener) {
			if (positive) {
				target.addEventListener(event, callback, false);
			} else {
				target.removeEventListener(event, callback, false);
			}
		} else if (target.attachEvent) {
			event = "on"+event;
			if (positive) {
				target.attachEvent(event, callback);
			} else {
				target.detachEvent(event, callback);
			}
		} else {
			throwError("event not supported: ${0}", event);
		}
	}
	return callback;
}

//@public
function bindEvent(event, callback, node) {
	var binding = System.callback.call(this, dispatch, callback, node);
	if (event && node) this.toggleEvent(event, binding, true, node);
	return binding;
}

//@private
function dispatch(callback, node, event) {
	var status = callback(this, node, event);
	if (status) return true;
	
	/*stop propagation*/
	if (event.stopPropagation) event.stopPropagation();
	event.cancelBubble = true;
	
	/*prevent default action in FF*/
	if (event.preventDefault) event.preventDefault();
	
	/*false to prevent default action in IE*/
	return false;
}
