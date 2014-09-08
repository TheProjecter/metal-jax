/**
 * @resource
 * @imports Internal
 * @imports Input
 * @imports Template
 * @imports Binding
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
	
	// resolve view references
	view.controller.initView(view);
	forEach(node.childNodes, initView, view.scope);
	
	// resolve placeholder references
	view.controller.initModel(view);
	initModel(view);

	// resolve scope references
	view.controller.initScope(view);
	forEach(view.scopes, initScope);
	
	// custom node init
	forEach(view.nodes, initNode, view);
}

//@private
function initNode(id, node, view) {
	view.controller.initNode(view, node);
}

//traverse content nodes: element, text
//@private
function initView(index, node, scope) {
	var hasContent = false, setting = null, nextScope = null;
	if (node.id) scope.view.nodes[node.id] = node;
	switch (node.nodeType) {
	case 1:
		setting = Internal.parseNodeSetting(node, scope.view.setting.view);
		switch (setting.nodeType) {
		case "view":
			hasContent = true;
			Internal.newView(scope.view, node, setting);
			break;
		case "placeholder":
			hasContent = true;
			nextScope = scope;
			Internal.newPlaceholder(scope.view, node, setting);
			break;
		case "part":
			nextScope = scope;
			Internal.newPart(scope.view, node, setting);
			break;
		case "scope":
			hasContent = true;
			nextScope = Internal.newScope(scope.view, node, setting);
			break;
		case "json":
			Internal.newJson(scope.view, node, setting);
			break;
		default:
			hasContent = true;
			nextScope = scope;
			break;
		}
		break;
	case 3:
		hasContent = true;
		break;
	}
	if (hasContent) {
		initContent(index, node, scope.bean, setting);
	}
	if (nextScope) {
		forEach(node.childNodes, initView, nextScope);
	}
}

//init: template, input, event
//@private
function initContent(index, node, bean, setting) {
	Template.initTemplate(index, node, bean);
	if (node.nodeType == 1) {
		Input.initInput(bean, node);
		forEach(setting, Binding.initBinding, bean, node);
	}
}

//@private
function initModel(view) {
	forEach(view.placeholders, initPlaceholder);
	forEach(view.parts, clearPart);
	var style = System.parseBaseName(view.setting.view);
	if (style) {
		view.toggleStyle(view.node, style);
	}
}

//@private
function clearPart(id, part) {
	part.node.parentNode.removeChild(part.node);
}

//@private
function initPlaceholder(name, placeholder) {
	var part = placeholder.view.parts[name];
	if (part) {
		var nodes = toArray(part.node);
		if (nodes.length) {
			var placemark = findPlacemark(placeholder.node);
			for (var i = 0; i < nodes.length; i++) {
				placemark.appendChild(nodes[i]);
				placeholder.view.controller.initPlaceholder(placeholder.view, placeholder, nodes[i]);
				if (placeholder.repeatText && i < nodes.length-1) {
					var holderNode = Internal.toDocFrag(placeholder.repeatText);
					if (placemark != placeholder.node) {
						placemark = findPlacemark(holderNode);
					}
					placeholder.node.appendChild(holderNode);
				}
			}
		}
	}
}

//@private
function initScope(index, scope) {
	var model = scope.name && scope.view.get(scope.name) || scope.view.$;
	if (!scope.repeatText || !(model instanceof Array)) {
		formatBean(scope.bean, model);
		forEach(scope.bean.inputs, Input.bindInput);
		forEach(scope.bean.bindings, Binding.formatBinding);
	} else { // scope.repeatText && model instanceof Array
		for (var i = 0; i < model.length; i++) {
			formatBean(scope.beans[i], model[i]);
			forEach(scope.beans[i].inputs, Input.bindInput);
			forEach(scope.beans[i].bindings, Binding.formatBinding);
			if (i < model.length-1) {
				// add bean
				var node = Internal.toDocFrag(scope.repeatText);
				forEach(node.childNodes, initBean, Internal.newBean(scope));
				scope.node.appendChild(node);
			}
		}
	}
}

//@static
function updateScope(view, name, index) {
	var model = name && view.get(name) || view.$;
	forEach(view.scopes, updateScope2, name, model, index);
}

//@private
function updateScope2(index, scope, name, model, bindex) {
	if ((!name && !scope.name) || scope.name == name) {
		if (!scope.repeatText || !(model instanceof Array)) {
			formatBean(scope.bean, model);
		} else { // scope.repeatText && model instanceof Array
			for (var i = 0; i < model.length; i++) {
				formatBean(scope.beans[i], model[i]);
			}
		}
	}
}

//@private
function initBean(index, node, bean) {
	var setting = null;
	switch (node.nodeType) {
	case 1:
		setting = Internal.parseNodeSetting(node, bean.view.setting.view);
		initContent(index, node, bean, setting);
		forEach(node.childNodes, initBean, bean);
		break;
	case 3:
		initContent(index, node, bean, setting);
		break;
	}
}

//@private
function formatBean(bean, model) {
	forEach(bean.templates, Template.formatTemplate, model);
	forEach(bean.inputs, Input.formatInput, model);
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

//@private
function toArray(node) {
	var result = [];
	for (var i = 0; i < node.childNodes.length; i++) {
		if (node.childNodes[i].nodeType === 1) {
			result.push(node.childNodes[i]);
		}
	}
	return result;
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
