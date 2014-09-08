/**
 * @class
 * @imports Internal
 * @imports View
 * 
 * @copyright Jay Tang 2014. All rights reserved.
 */

//@private
var _templateRE_ = /\$\{([^\s\}:|]+)([:|][^\}]*)?\}/;

//init: element, attr, text
//@static
function initTemplate(index, node, bean) {
	switch (node.nodeType) {
	case 1: // element node
		forEach(node.attributes, initTemplate, bean);
		break;
	case 2: // attr node
	case 3: // text node
		if (_templateRE_.test(node.nodeValue)) {
			Internal.newTemplate(bean, node);
		}
		break;
	}
}

//@static
function formatTemplate(index, template, model) {
	template.node.nodeValue = format(template.text, model, template.view);
}

//@private
var _formatRE_ = /\$\{([^\s\}:|]+)([:|][^\}]*)?\}/g;

//@private
function format(text, model, view) {
	return text.replace(_formatRE_, function(match, name, value) {
		var result = null, exists = false;
		if (name in model) {
			result = model[name]; exists = true;
		} else if (name in view.$) {
			result = view.get(name); exists = true;
		}
		result = (typeof result == "function") ? result() : result;
		return exists ? result : value ? value.substring(1) : match;
	});
}
