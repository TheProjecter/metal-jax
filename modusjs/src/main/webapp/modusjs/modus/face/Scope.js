/**
 * @class
 * @imports Internal
 * @imports Bean
 * @imports Node
 * 
 * @copyright Jay Tang 2014. All rights reserved.
 */

//@static
function scanScope(index, node, scope) {
	var hasContent = false, nextScope = null, setting = null;
	switch (node.nodeType) {
	case 1: // elem
		setting = Internal.parseNodeSetting(node, scope.view.setting.view);
		switch (setting.type) {
		case "view":
			hasContent = true;
			nextScope = null;
			Internal.newView(scope.view, node, setting);
			break;
		case "placeholder":
			hasContent = true;
			nextScope = scope;
			Internal.newPlaceholder(scope.view, node, setting);
			break;
		case "part":
			hasContent = false;
			nextScope = scope;
			Internal.newPart(scope.view, node, setting);
			break;
		case "scope":
			hasContent = true;
			nextScope = newScope(scope.view, node, setting);
			break;
		case "json":
			hasContent = false;
			nextScope = null;
			Internal.newJson(scope.view, node, setting);
			break;
		default:
			hasContent = true;
			nextScope = scope;
			break;
		}
		break;
	case 3: // text
		hasContent = true;
		nextScope = null;
		break;
	}
	if (hasContent) {
		if (node.id) scope.view.nodes[node.id] = node;
		Bean.scanContent(index, node, (nextScope||scope).bean, setting);
	}
	if (nextScope) {
		forEach(node.childNodes, scanScope, nextScope);
	}
}

//@static
function newScope(view, node, setting) {
	var scope = { name:setting.scope||"", view:view, node:node, beans:[] };
	if ("list" in setting) {
		scope.type = "list";
		scope.model = setting.list;
		scope.frag = Node.toFrag(node);
		scope["default"] = Node.split(scope.frag, "default")[0];
	} else {
		scope.bean = Scope.newBean(scope);
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
function initScope(index, scope) {
	var model = getModel(scope);
	switch (scope.type) {
	case "list":
		initList(model[scope.model], scope);
		break;
	default:
		Bean.bindBean(-1, scope.bean);
		Bean.normalizeBean(-1, scope.bean, model);
	}
}

//@private
function initList(items, scope) {
	if (items.length) {
		forEach(items, Bean.newBean, scope);
		forEach(scope.beans, Bean.bindBean);
		forEach(scope.beans, Bean.normalizeBean, items);
	} else if (scope["default"]){
		var node = scope["default"].cloneNode(true);
		scope.node.appendChild(node);
	}
}

//@static
function updateScope(index, scope, name, model) {
	if (!name || scope.name == name) {
		if (scope.name != name) {
			model = model[scope.name];
		}
		switch (scope.type) {
		case "list":
			Node.clearContent(scope.node);
			Internal.clearArray(scope.beans);
			initList(model[scope.model], scope);
			break;
		default:
			Bean.normalizeBean(-1, scope.bean, model);
		}
	}
}

//@static
function getModel(scope) {
	return scope.name ? scope.view.get(scope.name) : scope.view.get();
}
