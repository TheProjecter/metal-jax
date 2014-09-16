/**
 * @class
 * @imports Internal
 * @imports Scope
 * 
 * @copyright Jay Tang 2014. All rights reserved.
 */

//@private
var _inputs_ = [ "text", "checkbox", "password", "radio", "submit", "textarea" ];

//@static
function scanInput(bean, node) {
	if (_inputs_.indexOf(node.type) >= 0) {
		Internal.newInput(bean, node);
	}
}

//@static
function initInput(index, input, model) {
	switch (input.type) {
	case "checkbox":
		input.node.checked = model[input.name] == true;
		break;
	}
}

//@static
function bindInput(index, input) {
	switch (input.type) {
	case "checkbox":
		input.callback = input.bean.view.bindEvent("click", changeCheckbox, input.node, input);
		break;
	}
}

//@private
function changeCheckbox(view, node, event, input) {
	var model = view.get(input.bean.scope.name);
	model[input.bean.index][input.name] = node.checked;
	Scope.updateScope(view, input.bean.scope.name, input.bean.index);
	return true;
}
