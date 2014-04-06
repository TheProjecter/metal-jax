/**
 * @class
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@public
function init(view) {
	//empty
}

//@public
function afterInit(view) {
	//empty
}

//@public
function bind(view, node) {
	//empty
}

//@public
function refresh(view, content) {
	for (var name in content.nodes) {
		if (view.nodes[name]) {
			view.clear(name).appendChild(content.nodes[name]);
		}
	}
}
