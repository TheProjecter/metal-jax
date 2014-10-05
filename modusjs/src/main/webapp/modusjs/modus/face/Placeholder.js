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
			for (var i = 0; i < nodes.length; i++) {
				placemark.appendChild(nodes[i]);
				placeholder.view.controller.getClass().initPlaceholder(placeholder, nodes[i]);
				if (placeholder.repeatText && i < nodes.length-1) {
					var frag = Node.toDocFrag(placeholder.repeatText);
					if (placemark != placeholder.node) {
						placemark = findPlacemark(frag);
					}
					placeholder.node.appendChild(frag);
				}
			}
		}
	}
}

//@private
function findPlacemark(placemark) {
	return Node.findNodeByStyles(placemark, ["placemark"]) || placemark;
}

//@private
function clearPart(id, part) {
	part.node.parentNode.removeChild(part.node);
}
