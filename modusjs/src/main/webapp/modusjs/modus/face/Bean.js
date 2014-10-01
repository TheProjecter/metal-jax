/**
 * @class
 * @imports Internal
 * @imports Template
 * @imports Binding
 * @imports Input
 * @imports Scope
 * 
 * @copyright Jay Tang 2014. All rights reserved.
 */

//@static
function scanContent(index, node, bean, setting) {
	Template.scanTemplate(index, node, bean);
	if (node.nodeType == 1) {
		Input.scanInput(bean, node);
		forEach(setting, Binding.scanBinding, bean, node);
	}
}

//@static
function newBean(index, model, scope) {
	var node = Internal.toNode(scope.repeatDiv, scope.repeatText);
	forEach(node.childNodes, scanBean, Internal.newBean(scope));
	Internal.moveContent(scope.node, node);
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
function normalizeBean(index, bean, model) {
	model = (index >= 0) ? model[index] : model;
	forEach(bean.inputs, Input.normalizeInput, model);
	forEach(bean.templates, Template.normalizeTemplate, model);
}

//@static
function bindBean(index, bean) {
	forEach(bean.inputs, Input.bindInput);
	forEach(bean.bindings, Binding.bindEvent);
}

//@static
function updateBean(bean) {
	var model = Scope.getModel(bean.scope);
	forEach(bean.view.scopes, Scope.updateScope, bean.scope.name, model);
}

//@static
function getModel(bean, name) {
	var model = Scope.getModel(bean.scope);
	if (bean.scope.repeatText) {
		model = model[bean.index];
	}
	if (name && !(name in model)) {
		model = bean.view.get();
	}
	return model;
}
