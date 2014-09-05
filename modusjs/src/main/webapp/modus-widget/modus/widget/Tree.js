/**
 * @controller
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@public
function initModel(view) {
	var label = view.node.title || view.nodes.label.innerHTML;
	view.nodes.label.innerHTML = label;
}

//@public
function initNode(view, node) {
	switch (node.id) {
	case "toggle":
		view.bindEvent("click", toggleTree, node);
		break;
	case "label":
		view.bindEvent("dblclick", toggleTree, node);
		break;
	}
}

//@private
function toggleTree(view, node, event) {
	view.toggleStyle(view.nodes.toggle, "open");
	view.toggleStyle(view.nodes.label, "open");
	view.toggleStyle(view.nodes.content, "open");
}
