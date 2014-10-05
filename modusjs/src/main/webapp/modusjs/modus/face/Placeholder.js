/**
 * @class
 * @imports Node
 * 
 * @copyright Jay Tang 2014. All rights reserved.
 */

//@static
function normalizePlaceholder(view) {
	forEach(view.placeholders, initPlaceholder);
	forEach(view.parts, clearPart);
}

//@private
function initPlaceholder(name, placeholder) {
	var part = placeholder.view.parts[name];
	if (part) {
		var nodes = Node.toArray(part.node);
		if (nodes.length) {
			var placemark = findPlacemark(placeholder.node);
			forEach(nodes, initPlacemark, placeholder, placemark);
		}
	}
}

//@private
function initPlacemark(index, node, placeholder, placemark) {
	switch (placeholder.type) {
	case "list":
		var frag = placeholder.frag.cloneNode(true);
		placemark = findPlacemark(frag);
		if (placemark == frag) {
			placemark = placeholder.node;
		}
		placeholder.node.appendChild(frag);
		break;
	}
	placemark.appendChild(node);
	placeholder.view.controller.getClass().initPlaceholder(placeholder, node);
}

//@private
function findPlacemark(placemark) {
	return Node.findNodeByStyles(placemark, ["placemark"]) || placemark;
}

//@private
function clearPart(id, part) {
	part.node.parentNode.removeChild(part.node);
}
