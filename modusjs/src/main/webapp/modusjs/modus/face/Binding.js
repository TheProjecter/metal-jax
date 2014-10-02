/**
 * @class
 * @imports Bean
 * @imports Node
 * 
 * @copyright Jay Tang 2014. All rights reserved.
 */

//@private
var _events_ = [
	"keydown", "keypress", "keyup",
	"click", "dblclick",
	"mousedown", "mouseup", "mousemove", "mouseout", "mouseover",
	"change", "submit"
];

//@private
var _touchEvents_ = [
	"touchstart", "touchend", "touchmove", "touchenter", "touchleave", "touchcancel"
];

//@static
function scanBinding(event, action, bean, node) {
	if (_events_.indexOf(event) >= 0) {
		newBinding(bean, node, event, action);
	}
}

//@private
function newBinding(bean, node, event, action) {
	var binding = { bean:bean, node:node, view:bean.view, event:event, action:action };
	bean.bindings.push(binding);
	return binding;
}

//@static
function bindEvent(index, binding) {
	if (binding.event && binding.view.controller[binding.action]) {
		binding.callback = binding.view.bindEvent(binding.event, dispatch, binding.node, binding);
	}
}

//@static
function unbindEvent(index, binding) {
	if (binding.callback) {
		Node.toggleEvent(binding.event, binding.callback, false, binding.node);
		delete binding.callback;
	}
}

//@private
function dispatch(view, node, event, binding) {
	var action = binding.view.controller[binding.action];
	if (typeof action == "function") {
		if (action.call(binding.view.controller)) {
			Bean.updateBean(binding.bean);
		}
	}
}
