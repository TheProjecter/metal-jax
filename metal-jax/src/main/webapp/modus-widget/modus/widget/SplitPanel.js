/**
 * @controller
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@private
var _panelStyles_ = [ "splitX", "splitY" ];

//@public
function init(view) {
	view.setting.style = view.filterSetting(view.setting, _panelStyles_, true);
	view.toggleStyle(view.node, view.setting.style, "splitPanel");
	view.bindings = {};
}

//@public
function bind(view, node) {
	switch (node.id) {
	case "handle":
		view.bindEvent("mouseover", toggleHighlight, node);
		view.bindEvent("mouseout", toggleHighlight, node);
		view.bindings[node.id] = view.bindEvent("mousedown", dispatch, node);
		// fall through
	case "panel1":
	case "panel2":
		var style = view.filterStyle(node, _panelStyles_, true);
		view.toggleStyle(node, style, view.setting.style);
		break;
	}
}

//@private
function dispatch(view, node, event) {
	switch (event.type) {
	case "mousedown":
		startResize(view, node, event);
		break;
	case "mousemove":
		doResize(view, node, event);
		break;
	case "mouseup":
		release(view, node, event);
		break;
	}
}

//@private
function toggleHighlight(view, node, event) {
	view.toggleStyle(node, "highlight");
	return true;
}

//@private
function capture(view, node, event) {
	view.toggleEvent("mousemove", view.bindings[node.id], true);
	view.toggleEvent("mouseup", view.bindings[node.id], true);
}

//@private
function release(view, node, event) {
	view.toggleEvent("mousemove", view.bindings[node.id], false);
	view.toggleEvent("mouseup", view.bindings[node.id], false);
}

//@private
function startResize(view, node, event) {
	view.setting.x = event.clientX;
	view.setting.y = event.clientY;
	view.setting.panel1Width = view.nodes.panel1.offsetWidth;
	view.setting.panel1Height = view.nodes.panel1.offsetHeight;
	view.setting.panel2Width = view.nodes.panel2.offsetWidth;
	view.setting.panel2Height = view.nodes.panel2.offsetHeight;
	
	capture(view, node, event);
}

//@private
function doResize(view, node, event) {
	var dx = event.clientX - view.setting.x;
	var dy = event.clientY - view.setting.y;
	
	switch (view.setting.style) {
	case "splitY":
		setHeight(view, dy);
		break;
	case "splitX":
	default:
		setWidth(view, dx);
		break;
	}
}

//@private
function setWidth(view, dx) {
	var panel1Width = view.setting.panel1Width + dx;
	var panel2Width = view.setting.panel2Width - dx;
	if (panel1Width <= 0 || panel2Width <= 0) return;
	view.nodes.panel1.style.width = "".concat(panel1Width, "px");
	view.nodes.panel2.style.width = "".concat(panel2Width, "px");
}

//@private
function setHeight(view, dy) {
	var panel1Height = view.setting.panel1Height + dy;
	var panel2Height = view.setting.panel2Height - dy;
	if (panel1Height <= 0 || panel2Height <= 0) return;
	view.nodes.panel1.style.height = "".concat(panel1Height, "px");
	view.nodes.panel2.style.height = "".concat(panel2Height, "px");
}
