/**
 * @resource
 * @imports Internal
 * @imports Scope
 * @imports Placeholder
 * @imports modus.core.System
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@static
function parseSource(source) {
	source.model = Internal.toDocFrag(source.content);
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
	var setting = Internal.parseNodeSetting(node, source.name);
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
function initObject(view, node, setting, controller) {
	Internal.initView(view, node, setting, controller);
	
	view.controller.initView(view);
	forEach(view.node.childNodes, Scope.scanScope, view.scope);
	
	view.controller.initModel(view);
	Placeholder.normalize(view);

	view.controller.initScope(view);
	Scope.normalize(view);
	
	Internal.afterInit(view);
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
	return Internal.filterStyle(node, styles, defaultToFirst);
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
	index = index || 0;
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
function bindEvent(event, callback, node, bean) {
	bean = bean || this.scope.bean;
	var binding = System.callback.call(this, dispatch, callback, node, bean);
	if (event && node) this.toggleEvent(event, binding, true, node);
	return binding;
}

//@private
function dispatch(callback, node, bean, event) {
	var status = callback(this, node, event, bean);
	if (status) return true;
	
	/*stop propagation*/
	if (event.stopPropagation) event.stopPropagation();
	event.cancelBubble = true;
	
	/*prevent default action in FF*/
	if (event.preventDefault) event.preventDefault();
	
	/*false to prevent default action in IE*/
	return false;
}
