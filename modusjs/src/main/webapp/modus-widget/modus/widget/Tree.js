/**
 * @controller
 * @imports modus.face.Node
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@static
function initModel(view) {
	var label = view.node.title || view.nodes.label.innerHTML;
	view.nodes.label.innerHTML = label;
}

//@static
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
	Node.toggleStyle(view.nodes.toggle, "open");
	Node.toggleStyle(view.nodes.label, "open");
	Node.toggleStyle(view.nodes.content, "open");
}
