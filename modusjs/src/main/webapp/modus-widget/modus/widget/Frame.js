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
function bind(view, node) {
	switch (node.id) {
	case "frame":
		view.bindEvent("mouseover", toggleHighlight, node);
		view.bindEvent("mouseout", toggleHighlight, node);
		break;
	case "handle":
	case "topLeft":
	case "top":
	case "topRight":
	case "bottomLeft":
	case "bottom":
	case "bottomRight":
	case "leftTop":
	case "left":
	case "leftBottom":
	case "rightTop":
	case "right":
	case "rightBottom":
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
	view.setting.top = view.nodes.frame.offsetTop;
	view.setting.left = view.nodes.frame.offsetLeft;
	
	capture(view, node, event);
}

//@private
function doMove(view, node, event) {
	var dx = event.clientX - view.setting.x;
	var dy = event.clientY - view.setting.y;
	view.nodes.frame.style.top = "".concat(view.setting.top + dy, "px");
	view.nodes.frame.style.left = "".concat(view.setting.left + dx, "px");
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
	view.setting.top = view.nodes.frame.offsetTop;
	view.setting.left = view.nodes.frame.offsetLeft;
	view.setting.width = view.nodes.frame.offsetWidth;
	view.setting.height = view.nodes.frame.offsetHeight;
	view.setting.topWidth = view.nodes.top.offsetWidth;
	view.setting.leftHeight = view.nodes.left.offsetHeight;
	view.setting.handleWidth = view.nodes.handle.offsetWidth;
	view.setting.handleHeight = view.nodes.handle.offsetHeight;
	view.setting.contentWidth = view.nodes.content.offsetWidth;
	view.setting.contentHeight = view.nodes.content.offsetHeight;
	if (view.setting.contentHeight == view.setting.height) {
		view.setting.contentHeight -= view.setting.handleHeight;
	}
	
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
	var contentWidth = view.setting.contentWidth + dx;
	if (contentWidth <= 0) return;
	view.nodes.frame.style.width = "".concat(view.setting.width + dx, "px");
	if (isLeft) view.nodes.frame.style.left = "".concat(view.setting.left - dx, "px");
	view.nodes.handle.style.width = "".concat(handleWidth, "px");
	view.nodes.content.style.width = "".concat(contentWidth, "px");
}

//@private
function setHeight(view, dy, isTop) {
	var leftHeight = view.setting.leftHeight + dy;
	var contentHeight = view.setting.contentHeight + dy;
	if (contentHeight <= 0) return;
	view.nodes.frame.style.height = "".concat(view.setting.height + dy, "px");
	if (isTop) view.nodes.frame.style.top = "".concat(view.setting.top - dy, "px");
	view.nodes.content.style.height = "".concat(contentHeight, "px");
}