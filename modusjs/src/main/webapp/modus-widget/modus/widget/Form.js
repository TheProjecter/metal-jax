/**
 * @controller
 * @imports modus.core.System
 * @imports modus.face.Node
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@private
var $count = 0;

//@static
function initView(view) {
	view.inputs = {};
}

//@static
function initNode(view, node) {
	switch (node.nodeName.toLowerCase()) {
	case "form":
		node.name = System.parseBaseName(this.getClass().getName()).concat($count++);
		view.nodes["form"] = node;
		break;
	case "fieldset":
		view.nodes["fieldset"] = node;
		break;
	case "legend":
		view.bindEvent("click", toggleForm, node);
		break;
	case "label":
		node.htmlFor = view.nodes["form"].name.concat(".", node.htmlFor);
		break;
	case "input":
	case "select":
	case "textarea":
		view.inputs[node.name] = node;
		// fall through
	case "button":
		node.id = view.nodes["form"].name.concat(".", node.name);
		view.nodes[node.name] = node;
		break;
	}
}

//@private
function toggleForm(view, node, event) {
	Node.toggleStyle(view.nodes.fieldset, "collapsed", "expanded");
}

//@public
function get(view, name) {
	if (view.inputs[name]) {
		return view.inputs[name].value;
	}
}

//@public
function set(view, name, value) {
	if (view.inputs[name]) {
		return view.inputs[name].value = value;
	}
}
