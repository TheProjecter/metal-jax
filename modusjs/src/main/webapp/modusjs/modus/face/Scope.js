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
			nextScope = Internal.newScope(scope.view, node, setting);
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
function initScope(index, scope) {
	var model = getModel(scope);
	switch (scope.type) {
	case "list":
		forEach(model, Bean.newBean, scope);
		forEach(scope.beans, Bean.bindBean);
		forEach(scope.beans, Bean.normalizeBean, model);
		break;
	default:
		Bean.bindBean(-1, scope.bean);
		Bean.normalizeBean(-1, scope.bean, model);
	}
}

//@static
function updateScope(index, scope, name, model) {
	if (!scope.name && !name || scope.name == name) {
		switch (scope.type) {
		case "list":
			Node.clearContent(scope.node);
			Internal.clearArray(scope.beans);
			
			forEach(model, Bean.newBean, scope);
			forEach(scope.beans, Bean.bindBean);
			forEach(scope.beans, Bean.normalizeBean, model);
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
