/**
 * @class
 * @imports Internal
 * 
 * @copyright Jay Tang 2014. All rights reserved.
 */

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

//@static
function scanBinding(event, action, bean, node) {
	if (_events_.indexOf(event) >= 0) {
		Internal.newBinding(bean, node, event, action);
	}
}

//@static
function initBinding(index, binding) {
	if (binding.event && binding.view.controller[binding.action]) {
		binding.callback = binding.view.bindEvent(binding.event, binding.view.controller[binding.action], binding.node, binding.bean);
	}
}

//@static
function resetBinding(index, binding) {
	if (binding.callback) {
		binding.view.toggleEvent(binding.event, binding.callback, false, binding.node);
		delete binding.callback;
	}
}
