/**
 * @class
 * @imports Internal
 * @imports Template
 * @imports Binding
 * @imports Input
 * 
 * @copyright Jay Tang 2014. All rights reserved.
 */

//@static
function scanScope(index, node, scope) {
	var hasContent = false, nextScope = null, setting = null;
	switch (node.nodeType) {
	case 1: // element
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
		scanContent(index, node, scope.bean, setting);
	}
	if (nextScope) {
		forEach(node.childNodes, scanScope, nextScope);
	}
}

//@private
function scanContent(index, node, bean, setting) {
	Template.scanTemplate(index, node, bean);
	if (node.nodeType == 1) {
		Input.scanInput(bean, node);
		forEach(setting, Binding.scanBinding, bean, node);
	}
}

//@private
function scanBean(index, node, bean) {
	var setting = null;
	switch (node.nodeType) {
	case 1: // elem
		setting = Internal.parseNodeSetting(node, bean.view.setting.view);
		scanContent(index, node, bean, setting);
		forEach(node.childNodes, scanBean, bean);
		break;
	case 3: // text
		scanContent(index, node, bean, setting);
		break;
	}
}

//@static
function normalize(view) {
	forEach(view.scopes, initScope);
}

//@private
function initScope(index, scope) {
	var model = scope.name && scope.view.get(scope.name) || scope.view.$;
	if (!scope.repeatText || !(model instanceof Array)) {
		initBean(scope.bean, model);
		forEach(scope.bean.inputs, Input.bindInput);
		forEach(scope.bean.bindings, Binding.initBinding);
	} else { // scope.repeatText && model instanceof Array
		for (var i = 0; i < model.length; i++) {
			initBean(scope.beans[i], model[i]);
			forEach(scope.beans[i].inputs, Input.bindInput);
			forEach(scope.beans[i].bindings, Binding.initBinding);
			if (i < model.length-1) {
				// add bean
				var node = Internal.toDocFrag(scope.repeatText);
				forEach(node.childNodes, scanBean, Internal.newBean(scope));
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
			initBean(scope.bean, model);
		} else { // scope.repeatText && model instanceof Array
			for (var i = 0; i < model.length; i++) {
				initBean(scope.beans[i], model[i]);
			}
		}
	}
}

//@private
function initBean(bean, model) {
	forEach(bean.templates, Template.normalize, model);
	forEach(bean.inputs, Input.initInput, model);
}
