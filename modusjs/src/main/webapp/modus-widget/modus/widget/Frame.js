/**
 * @controller
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@public
function init(view) {
	view.bindings = {};
}

//@public
function afterInit(view) {
	view.bindEvent("mouseover", toggleHighlight, view.node);
	view.bindEvent("mouseout", toggleHighlight, view.node);
}

//@public
function bind(view, node) {
	switch (node.id) {
	case "top":
	case "topLeft":
	case "topRight":
	case "bottom":
	case "bottomLeft":
	case "bottomRight":
	case "left":
	case "leftTop":
	case "leftBottom":
	case "right":
	case "rightTop":
	case "rightBottom":
	case "handle":
		view.bindings[node.id] = view.bindEvent("mousedown", dispatch, node);
		break;
	}
}

//@private
function dispatch(view, node, event) {
	switch (event.type) {
	case "mousedown":
		node.id == "handle" ? startMove(view, node, event) : startResize(view, node, event);
		break;
	case "mousemove":
		node.id == "handle" ? doMove(view, node, event) : doResize(view, node, event);
		break;
	case "mouseup":
		release(view, node, event);
		break;
	}
}

//@private
function toggleHighlight(view, node, event) {
	view.toggleStyle(node, "highlight");
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

/**
 * Handles frame move event.
 * Takes a profile snapshot
 * and binds mouse events.
 */
//@private
function startMove(view, node, event) {
	view.setting.x = event.clientX;
	view.setting.y = event.clientY;
	view.setting.top = parseInt(view.node.style.top) || view.node.offsetTop;
	view.setting.left = parseInt(view.node.style.left) || view.node.offsetLeft;
	
	capture(view, node, event);
}

//@private
function doMove(view, node, event) {
	var dx = event.clientX - view.setting.x;
	var dy = event.clientY - view.setting.y;
	view.node.style.top = "".concat(view.setting.top + dy, "px");
	view.node.style.left = "".concat(view.setting.left + dx, "px");
}

/**
 * Handles frame resize event.
 * Takes a profile snapshot
 * and binds resize/release.
 */
//@private
function startResize(view, node, event) {
	view.setting.x = event.clientX;
	view.setting.y = event.clientY;
	view.setting.top = parseInt(view.node.style.top) || view.node.offsetTop;
	view.setting.left = parseInt(view.node.style.left) || view.node.offsetLeft;
	view.setting.width = parseInt(view.node.style.width) || view.node.offsetWidth;
	view.setting.height = parseInt(view.node.style.height) || view.node.offsetHeight;
	view.setting.topWidth = view.nodes.top.offsetWidth;
	view.setting.leftHeight = view.nodes.left.offsetHeight;
	view.setting.handleWidth = view.nodes.handle.offsetWidth;
	view.setting.handleHeight = view.nodes.handle.offsetHeight;
	
	capture(view, node, event);
}

//@private
function doResize(view, node, event) {
	var dx = event.clientX - view.setting.x;
	var dy = event.clientY - view.setting.y;
	
	switch (node) {
	case view.nodes.top:
	case view.nodes.topLeft:
	case view.nodes.topRight:
	case view.nodes.leftTop:
	case view.nodes.rightTop:
		setHeight(view, -dy, true);
		break;
	case view.nodes.bottom:
	case view.nodes.bottomLeft:
	case view.nodes.bottomRight:
	case view.nodes.leftBottom:
	case view.nodes.rightBottom:
		setHeight(view, dy);
		break;
	}
	
	switch (node) {
	case view.nodes.left:
	case view.nodes.leftTop:
	case view.nodes.leftBottom:
	case view.nodes.topLeft:
	case view.nodes.bottomLeft:
		setWidth(view, -dx, true);
		break;
	case view.nodes.right:
	case view.nodes.rightTop:
	case view.nodes.rightBottom:
	case view.nodes.topRight:
	case view.nodes.bottomRight:
		setWidth(view, dx);
		break;
	}
}

//@private
function setWidth(view, dx, isLeft) {
	var topWidth = view.setting.topWidth + dx;
	var handleWidth = view.setting.handleWidth + dx;
	if (handleWidth <= 10) return;
	view.node.style.width = "".concat(view.setting.width + dx, "px");
	if (isLeft) view.node.style.left = "".concat(view.setting.left - dx, "px");
	view.nodes.handle.style.width = "".concat(handleWidth, "px");
}

//@private
function setHeight(view, dy, isTop) {
	var leftHeight = view.setting.leftHeight + dy;
	if (leftHeight <= 20) return;
	var contentHeight = view.setting.height + dy - view.setting.handleHeight - 5;
	view.node.style.height = "".concat(view.setting.height + dy, "px");
	if (isTop) view.node.style.top = "".concat(view.setting.top - dy, "px");
	view.nodes.content.style.height = "".concat(contentHeight, "px");
}
