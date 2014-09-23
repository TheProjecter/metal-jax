/**
 * @class
 * @imports Internal
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
		var nodes = Internal.toArray(part.node);
		if (nodes.length) {
			var placemark = findPlacemark(placeholder.node);
			for (var i = 0; i < nodes.length; i++) {
				placemark.appendChild(nodes[i]);
				placeholder.view.controller.getClass().initPlaceholder(placeholder, nodes[i]);
				if (placeholder.repeatText && i < nodes.length-1) {
					var holderNode = Internal.toDocFrag(placeholder.repeatText);
					if (placemark != placeholder.node) {
						placemark = findPlacemark(holderNode);
					}
					placeholder.node.appendChild(holderNode);
				}
			}
		}
	}
}

//@private
function findPlacemark(placemark) {
	return Internal.findNodeByStyles(placemark, ["placemark"]) || placemark;
}

//@private
function clearPart(id, part) {
	part.node.parentNode.removeChild(part.node);
}
