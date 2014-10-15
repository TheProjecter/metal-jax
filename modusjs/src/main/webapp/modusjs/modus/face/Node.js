/**
 * @class
 * @imports modus.core.System
 * 
 * @copyright Jay Tang 2014. All rights reserved.
 */

//@private
var _settingRE_ = /([^:]+)(?::|\s*)(.*)/;

//@static
function parseSetting(node) {
	var setting = {}, match;
	var text = node.getAttribute("data-modus-face") || node.className;
	var tokens = text && text.split(" ") || [];
	for (var i = 0; i < tokens.length; i++) {
		if (match = _settingRE_.exec(tokens[i])) {
			setting[match[1]] = match[2];
		}
	}
	return setting;
}

//@static
function toggleEvent(event, callback, positive, target) {
	positive = positive || arguments.length == 2;
	target = target || System.$document;
	if (callback) {
		if (target.addEventListener) {
			if (positive) {
				target.addEventListener(event, callback, false);
			} else {
				target.removeEventListener(event, callback, false);
			}
		} else if (target.attachEvent) {
			event = "on"+event;
			if (positive) {
				target.attachEvent(event, callback);
			} else {
				target.detachEvent(event, callback);
			}
		} else {
			throwError("event not supported: ${0}", event);
		}
	}
	return callback;
}

//@static
function findNodeByStyles(node, styles) {
	if (Node.filterStyle(node, styles)) return node;
	for (var i = 0; i < node.childNodes.length; i++) {
		var found = findNodeByStyles(node.childNodes[i], styles);
		if (found) return found;
	}
}

//@static
function filterStyle(node, styles, defaultToFirst) {
	var tokens = node.className && node.className.split(" ") || [];
	for (var i = 0; i < styles.length; i++) {
		if (tokens.indexOf(styles[i]) >= 0) {
			return styles[i];
		}
	}
	return defaultToFirst && styles[0] || "";
}

//@static
function toggleStyle(node, a, b) {
	a = a || ""; b = b || "";
	var tokens = node.className && node.className.split(" ") || [];
	var indexa = tokens.indexOf(a);
	var indexb = tokens.indexOf(b);
	if (indexa >= 0) {
		if (b && indexb < 0) {
			tokens.splice(indexa, 1, b);
		} else if (indexa != indexb) {
			tokens.splice(indexa, 1);
		}
	} else if (a) {
		if (indexb >= 0) {
			tokens.splice(indexb, 1, a);
		} else if (b) {
			tokens.push(b);
		} else {
			tokens.push(a);
		}
	}
	if (a) {
		node.className = tokens.join(" ");
	}
}

//@static
function indexOfChild(node, child) {
	for (var i = 0, c = 0; i < node.childNodes.length; i++) {
		var childNode = node.childNodes[i];
		if (childNode.nodeType == 1) {
			if (childNode == child) return c;
			else c++;
		}
	}
}

//@static
function getChildByIndex(node, index) {
	index = index || 0;
	for (var i = 0, c = 0; i < node.childNodes.length; i++) {
		var childNode = node.childNodes[i];
		if (childNode.nodeType == 1) {
			if (c == index) return childNode;
			else c++;
		}
	}
}

//@static
function moveContent(target, source, elementOnly) {
	while (source.firstChild) {
		if (elementOnly && source.firstChild.nodeType != 1) {
			source.removeChild(source.firstChild);
		} else {
			target.appendChild(source.firstChild);
		}
	}
	return target;
}

//@static
function clearContent(node) {
	while (node.firstChild) {
		node.removeChild(node.firstChild);
	}
}

//@static
function toArray(node) {
	var result = [];
	for (var i = 0; i < node.childNodes.length; i++) {
		if (node.childNodes[i].nodeType === 1) {
			result.push(node.childNodes[i]);
		}
	}
	return result;
}

//@static
function toFrag(html) {
	var node = (typeof html == "string") ? toNode("div", html) : html;
	return moveContent(System.$document.createDocumentFragment(), node, true);
}

//@static
function toNode(tagName, html) {
	var node = System.$document.createElement(tagName);
	if (typeof html == "string") {
		node.innerHTML = html;
	} else {
		moveContent(node, html);
	}
	return node;
}

//@static
function split(node, name) {
	var nodes = [];
	forEach(node.childNodes, filter, name, nodes);
	for (var i = 0; i < nodes.length; i++) {
		node.removeChild(nodes[i]);
	}
	return nodes;
}

//@private
function filter(index, node, name, nodes) {
	var setting = parseSetting(node);
	if (name in setting) nodes.push(node);
}
