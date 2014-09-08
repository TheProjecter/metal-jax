/**
 * @class
 * @imports Internal
 * @imports View
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
function initBinding(event, action, bean, node) {
	if (_events_.indexOf(event) >= 0) {
		Internal.newBinding(bean, node, event, action);
	}
}

//@static
function formatBinding(index, binding) {
	if (binding.event && binding.view.controller[binding.action]) {
		binding.view.bindEvent(binding.event, binding.view.controller[binding.action], binding.node, binding.bean);
	}
}
