/**
 * @controller
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@private
function initObject(object, node) {
	var label = node.title || object.nodes.label.innerHTML;
	object.nodes.label.innerHTML = label;
}

//@public
function bind(view, node) {
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
function toggleTree() {
	toggleStyle(this.nodes.toggle, "expanded");
	toggleStyle(this.nodes.label, "expanded");
	toggleStyle(this.nodes.content, "expanded");
}
