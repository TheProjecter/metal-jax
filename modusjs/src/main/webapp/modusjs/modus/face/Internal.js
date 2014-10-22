/**
 * @class
 * @imports Node
 * @imports Controller
 * @imports modus.core.System
 * 
 * @copyright Jay Tang 2014. All rights reserved.
 */

//@static
function newView(view, node, setting) {
	if (setting.view) {
		//TODO: check cyclic reference before proceeding
		var resource = view.getClass().findResource(setting.view, true).cloneNode(true);
		if (resource) {
			var part = Node.toNode("div", node);
			part.className = "part";
			node.appendChild(resource);
			node.appendChild(part);
		}
	}
	var child = view.getClass().newObject(node, setting, view.controller);
	view.views[node.id] = child;
	return child;
}

//@static
function initView(view, node, setting, controller) {
	var ccls = view.getClass().getContext().findClass(setting.controller);
	view.controller = ccls ? new ccls(view) : controller || new Controller(view);
	view.node = node;
	view.setting = setting;
	view.views = {};
	view.nodes = {};
	view.parts = {};
	view.placeholders = {};
	view.bindings = {};
	view.scopes = [];
	view.scope = newScope(view, node, setting);
}

//@static
function afterInit(view) {
	forEach(view.nodes, initNode, view);
	
	var style = System.parseBaseName(view.setting.view);
	if (style) {
		Node.toggleStyle(view.node, style);
	}
}

//@private
function initNode(id, node, view) {
	view.controller.getClass().initNode(view, node);
}

//@static
function newPlaceholder(view, node, setting) {
	var placeholder = { name:setting.placeholder||"", view:view, node:node };
	if ("list" in setting) {
		placeholder.type = "list";
		placeholder.frag = Node.toFrag(node);
	}
	view.placeholders[placeholder.name] = placeholder;
	return placeholder;
}

//@static
function newPart(view, node, setting) {
	var part = { name:setting.part||"", view:view, node:node };
	view.parts[part.name] = part;
	return part;
}

//@static
function newJson(view, node, setting) {
	var json = System.parseJSONFromText(node.innerHTML);
	if (setting.scope) {
		setValue(setting.scope, json, view);
	} else {
		forEach(json, setValue, view);
	}
	return json;
}

//@private
function setValue(key, value, view) {
	view.set(key, value);
}

//@static
function newScope(view, node, setting) {
	var scope = { name:setting.scope||"", view:view, node:node, beans:[] };
	if ("list" in setting) {
		scope.type = "list";
		scope.list = setting.list;
		scope.frag = Node.toFrag(node);
		scope["default"] = Node.split(scope.frag, "default")[0];
	} else {
		scope.bean = newBean(scope);
	}
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
function clearArray(array) {
	while (array.length) array.pop();
}

//@private
var _settingTypes_ = [ "view", "placeholder", "part", "scope" ];

//@private
var _jsonTypes_ = [ "json", "text/json", "application/json" ];

//@static
function parseNodeSetting(node, base) {
	var setting = Node.parseSetting(node);
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
	setting.type = "";
	for (var i = 0; i < _settingTypes_.length; i++) {
		if (_settingTypes_[i] in setting) {
			setting.type = _settingTypes_[i];
			break;
		}
	}
	if (!setting.type) {
		if (node.nodeName.toLowerCase() == "script" && _jsonTypes_.indexOf(node.type) >= 0 || "json" in setting) {
			setting.type = "json";
		}
	}
	if ("list" in setting) {
		setting.type = setting.type || "scope";
	}
	return setting;
}

//@static
function resolvePath(path, source) {
	var fromBase = path && path.indexOf("../") == 0;
	if (fromBase) {
		path = System.splitPath(path, 0, 1 - source.path.split("/").length);
	} else {
		path = System.resolvePath(path, source.path);
	}
	return System.resolvePath(path, source.module + "/");
}
