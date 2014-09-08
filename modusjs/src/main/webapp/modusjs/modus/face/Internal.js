/**
 * @class
 * @imports View
 * @imports modus.core.System
 * 
 * @copyright Jay Tang 2014. All rights reserved.
 */

//@static
function newView(view, node, setting) {
	if (setting.view) {
		var resource = view.getClass().findResource(setting.view, true).cloneNode(true);
		if (resource) {
			var part = toDocFrag(node, System.$document.createElement("div"));
			part.className = "part";
			node.appendChild(resource);
			node.appendChild(part);
		}
	}
	return view.views[node.id] = new (view.getClass())(node, setting, view.controller);
}

//@static
function initView(view, node, setting, controller) {
	var ccls = view.getClass().getContext().findClass(setting.controller);
	view.controller = ccls ? new ccls() : controller;
	view.node = node;
	view.setting = setting;
	view.views = {};
	view.nodes = {};
	view.parts = {};
	view.placeholders = {};
	view.bindings = [];
	view.scopes = [];
	view.scope = newScope(view, node, setting);
}

//@static
function newPlaceholder(view, node, setting) {
	var placeholder = { name:setting.placeholder, view:view, node:node };
	if ("repeat" in setting) placeholder.repeatText = node.innerHTML;
	view.placeholders[placeholder.name] = placeholder;
	return placeholder;
}

//@static
function newPart(view, node, setting) {
	var part = { name:setting.part, view:view, node:node };
	view.parts[part.name] = part;
	return part;
}

//@static
function newJson(view, node, setting) {
	var json = System.parseJSONFromText(node.text);
	if (setting.scope) {
		view.set(setting.scope, json);
	} else {
		for (var key in json) {
			view.set(key, json[key]);
		}
	}
	return json;
}

//@static
function newScope(view, node, setting) {
	var scope = { name:setting.scope, view:view, node:node, beans:[] };
	if ("repeat" in setting) scope.repeatText = node.innerHTML;
	scope.bean = newBean(scope);
	view.scopes.push(scope);
	return scope;
}

//@static
function newBean(scope) {
	var bean = { scope:scope, view:scope.view, index:scope.beans.length, templates:[], inputs:[], bindings:[] };
	scope.beans.push(bean);
	return bean;
}

//@static
function newTemplate(bean, node) {
	var template = { bean:bean, node:node, view:bean.view, text:node.nodeValue };
	bean.templates.push(template);
	return template;
}

//@static
function newInput(bean, node) {
	var input = { bean:bean, node:node, name:node.name, type:node.type };
	bean.inputs.push(input);
	return input;
}

//@static
function newBinding(bean, node, event, action) {
	var binding = { bean:bean, node:node, view:bean.view, event:event, action:action };
	bean.bindings.push(binding);
	return binding;
}

//@static
function toDocFrag(source, doc) {
	var node = source;
	if (typeof source == "string") {
		node = System.$document.createElement("div");
		node.innerHTML = source;
	}
	doc = doc || System.$document.createDocumentFragment();
	while (node.firstChild) {
		if (node.firstChild.nodeType === 1) {
			doc.appendChild(node.firstChild);
		} else {
			node.removeChild(node.firstChild);
		}
	}
	return doc;
}

//@private
var _settingRE_ = /([^:]+)(?::|\s*)(.*)/;

//@private
var _nodeTypes_ = [
	"view", "placeholder", "part", "scope"
];

//@static
function parseNodeSetting(node, base) {
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
	if (!setting.nodeType && node.nodeName.toLowerCase() == "script" && node.type == "application/json") {
		setting.nodeType = "json";
	}
	return setting;
}
