/**
 * @class
 * @imports Internal
 * @imports View
 * 
 * @copyright Jay Tang 2014. All rights reserved.
 */

//@private
var _inputs_ = [ "text", "checkbox", "password", "radio", "submit", "textarea" ];

//@static
function initInput(bean, node) {
	if (_inputs_.indexOf(node.type) >= 0) {
		Internal.newInput(bean, node);
	}
}

//@static
function formatInput(index, input, model) {
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
		input.bean.view.bindEvent("change", changeCheckbox, input.node, input);
		break;
	}
}

//@private
function changeCheckbox(view, node, event, input) {
	var model = view.get(input.bean.scope.name);
	model[input.bean.index][input.name] = node.checked;
	View.updateScope(view, input.bean.scope.name, input.bean.index);
}
