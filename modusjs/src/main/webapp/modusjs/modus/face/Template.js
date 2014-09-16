/**
 * @class
 * @imports Internal
 * 
 * @copyright Jay Tang 2014. All rights reserved.
 */

//@private
var _templateRE_ = /\$\{([^\s\}:|]+)([:|][^\}]*)?\}/;

//@static
function scanTemplate(index, node, bean) {
	var text = null;
	switch (node.nodeType) {
	case 1: // element
		forEach(node.attributes, scanTemplate, bean);
		return;
	case 2: // attr
		text = node.value;
		break;
	case 3: // text
		text = node.data;
		break;
	default:
		return;
	}
	if (_templateRE_.test(text)) {
		Internal.newTemplate(bean, node, text);
	}
}

//@static
function normalize(index, template, model) {
	var text = format(template.text, model, template.view);
	switch (template.node.nodeType) {
	case 2:
		template.node.value = text;
		break;
	case 3:
		template.node.data = text;
		break;
	}
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
