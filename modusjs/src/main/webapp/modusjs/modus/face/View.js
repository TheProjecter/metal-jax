/**
 * @resource
 * @imports Controller
 * @imports Handler
 * @imports modus.core.System
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@static
function parseSource(source) {
	source.model = Handler.toDocFrag(source.content);
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
var _nodeTypes_ = [
	"view", "placeholder", "part", "scope"
];

//@private
function parseViewSetting(node, base) {
	var setting = {}, match;
	var tokens = node.className && node.className.split(" ") || [];
	for (var i = 0; i < tokens.length; i++) {
		if (match = _settingRE_.exec(tokens[i])) {
			setting[match[1]] = match[2];
		}
	}
	if ("view" in setting) {
		setting.view = System.parseSourceName(setting.view, base);
	}
	if ("controller" in setting) {
		setting.controller = System.parseSourceName(setting.controller, base);
		if ("view" in setting) {
			setting.view = setting.view || setting.controller;
			setting.controller = setting.controller || setting.view;
		} else {
			setting.view = "";
		}
	}
	setting.nodeType = "";
	for (var i = 0; i < _nodeTypes_.length; i++) {
		if (_nodeTypes_[i] in setting) {
			setting.nodeType = _nodeTypes_[i];
		}
	}
	if (!setting.nodeType && node.nodeName.toLowerCase() == "script" && node.type == "text/json") {
		setting.nodeType = "json";
	}
	return setting;
}

//@private
function initObject(view, node, controller, setting) {
	view.node = node;
	view.controller = controller;
	view.setting = setting || {};

	view.views = {};
	view.nodes = {};
	view.parts = {};
	view.placeholders = {};
	view.scopes = [];
	view.bindings = [];
	
	// resolve view references
	view.controller.initView(view);
	forEach(node.childNodes, initView, view, Handler.newScope(view, node, setting));
	
	// resolve placeholder references
	view.controller.initModel(view);
	initModel(view);

	// resolve scope references
	view.controller.initScope(view);
	forEach(view.scopes, initScope, view);
	for (var id in view.nodes) {
		view.controller.bind(view, view.nodes[id]);
	}
}

//traverse content nodes: element, text
//@private
function initView(index, node, view, scope) {
	if (node.id) view.nodes[node.id] = node;
	switch (node.nodeType) {
	case 1:
		var setting = parseViewSetting(node, view.setting.view);
		switch (setting.nodeType) {
		case "view":
			Handler.newView(view, node, setting);
			initContent(index, node, scope, setting);
			break;
		case "placeholder":
			Handler.newPlaceholder(view, node, setting);
			initContent(index, node, scope, setting);
			forEach(node.childNodes, initView, view, scope);
			break;
		case "part":
			Handler.newPart(view, node, setting);
			forEach(node.childNodes, initView, view, scope);
			break;
		case "scope":
			setting.scope = Handler.newScope(view, node, setting);
			initContent(index, node, scope, setting);
			forEach(node.childNodes, initView, view, setting.scope);
			break;
		case "json":
			Handler.newJson(view, node, setting);
			break;
		default:
			initContent(index, node, scope, setting);
			forEach(node.childNodes, initView, view, scope);
			break;
		}
		break;
	case 3:
		initContent(index, node, scope);
		break;
	}
}

//handle: template, input, event
//@private
function initContent(index, node, scope, setting, bean) {
	bean = bean || scope.beans[0];
	initTemplate(index, node, scope, bean);
	if (node.nodeType == 1) {
		initInput(bean, node);
		forEach(setting, initBinding, scope, node);
	}
}

//handle: element, attr, text
//@private
function initTemplate(index, node, scope, bean) {
	switch (node.nodeType) {
	case 1: // element node
		forEach(node.attributes, initTemplate, scope, bean);
		break;
	case 2: // attr node
	case 3: // text node
		if (_templateRE_.test(node.nodeValue)) {
			Handler.newTemplate(scope, node, bean);
		}
		break;
	}
}

//@private
function initInput(bean, node) {
	if (_inputs_.indexOf(node.type) >= 0) {
		Handler.newInput(bean, node);
	}
}

//@private
function initBinding(event, action, scope, node) {
	if (_events_.indexOf(event) >= 0) {
		Handler.newBinding(scope, node, event, action);
	}
}

//@private
function initModel(view) {
	forEach(view.placeholders, initPlaceholder, view);
	for (var id in view.parts) {
		var part = view.parts[id];
		part.node.parentNode.removeChild(part.node);
	}
	var style = System.parseBaseName(view.setting.view);
	if (style) {
		view.toggleStyle(view.node, style);
	}
}

//@private
function initPlaceholder(name, placeholder, view) {
	var part = view.parts[name];
	if (part) {
		var parts = toArray(part.node);
		if (parts.length) {
			var placemark = findPlacemark(placeholder.node);
			for (var i = 0; i < parts.length; i++) {
				placemark.appendChild(parts[i]);
				view.controller.initPart(view, placeholder, parts[i]);
				if (placeholder.repeat && i < parts.length-1) {
					var holderNode = Handler.toDocFrag(placeholder.repeat);
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
function initScope(index, scope, view) {
	var model = scope.name && view.get(scope.name) || view.$;
	if (!scope.repeat || !(model instanceof Array)) {
		formatBean(scope.beans[0], model, scope, view);
		formatBinding(scope.beans[0], scope, view);
	} else { // scope.repeat && model instanceof Array
		for (var i = 0; i < model.length; i++) {
			formatBean(scope.beans[i], model[i], scope, view);
			formatBinding(scope.beans[i], scope, view);
			if (i < model.length-1) {
				// add bean
				var node = Handler.toDocFrag(scope.repeat);
				forEach(node.childNodes, initBean, view, scope, Handler.newBean(scope));
				scope.node.appendChild(node);
			}
		}
	}
}

//@static
function updateScope(view, name, index) {
	var model = name && view.get(name) || view.$;
	var scope = view.scopes[2];
	if (!scope.repeat || !(model instanceof Array)) {
		formatBean(scope.beans[0], model, scope, view);
	} else { // scope.repeat && model instanceof Array
		for (var i = 0; i < model.length; i++) {
			formatBean(scope.beans[i], model[i], scope, view);
		}
	}
}

//@private
function initBean(index, node, view, scope, bean) {
	switch (node.nodeType) {
	case 1:
		var setting = parseViewSetting(node, view.setting.view);
		initContent(index, node, scope, setting, bean);
		forEach(node.childNodes, initBean, view, scope, bean);
		break;
	case 3:
		initContent(index, node, scope, null, bean);
		break;
	}
}

//@private
function formatBean(bean, model, scope, view) {
	forEach(bean.templates, formatTemplate, model, view);
	forEach(bean.inputs, formatInput, model, view);
}

//@private
function formatBinding(bean, scope, view) {
	for (var i = 0; i < bean.bindings.length; i++) {
		bind(view, scope, bean.bindings[i]);
	}
	
}

//@private
var _inputs_ = [ "text", "checkbox", "password", "radio", "submit", "textarea" ];

//@private
function formatInput(index, input, model, view) {
	switch (input.type) {
	case "checkbox":
		input.node.checked = model[input.name] == true;
		break;
	}
}

//@private
function formatTemplate(index, template, model, view) {
	template.node.nodeValue = format(template.text, model, view);
}


//@private
function bind(view, scope, binding) {
	if (binding.event && view.controller[binding.action]) {
		view.bindEvent(binding.event, view.controller[binding.action], binding.node);
	}
}

//@private
var _templateRE_ = /\$\{([^\s\}:|]+)([:|][^\}]*)?\}/;

//@private
var _formatRE_ = /\$\{([^\s\}:|]+)([:|][^\}]*)?\}/g;

//@private
function format(text, bean, view) {
	return text.replace(_formatRE_, function(match, name, value) {
		var result = bean[name] || view.get(name);
		result = (typeof result == "function") ? result() : result;
		return result ? ensureValue(result, value) : value ? value.substring(1) : match;
	});
}

//@private
function ensureValue(value, fallback) {
	var solid = fallback && fallback.charAt(0) == "|";
	return value || (solid && fallback.substring(1)) || value;
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
function toArray(source) {
	var result = [];
	for (var i = 0; i < source.childNodes.length; i++) {
		if (source.childNodes[i].nodeType === 1) {
			result.push(source.childNodes[i]);
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

//@private
var _events_ = [
	"keydown", "keypress", "keyup",
	"click", "dblclick",
	"mousedown", "mouseup", "mousemove", "mouseout", "mouseover",
	"change"
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
