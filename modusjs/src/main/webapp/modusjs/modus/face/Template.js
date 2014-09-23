/**
 * @class
 * @copyright Jay Tang 2014. All rights reserved.
 */

//@private
var _macroRE_ = /\$\{([^\s\}:|]+)([:|][^\}]*)?\}/;

//@private
var _macrosRE_ = /\$\{([^\s\}:|]+)([:|][^\}]*)?\}/g;

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
	if (text && _macroRE_.test(text)) {
		newTemplate(bean, node, text);
	}
}

//@private
function newTemplate(bean, node, text) {
	var template = { bean:bean, node:node, view:bean.view, text:text };
	bean.templates.push(template);
	return template;
}

//@static
function normalizeTemplate(index, template, model) {
	var text = format(template, model);
	switch (template.node.nodeType) {
	case 2: // attr
		template.node.value = text;
		break;
	case 3: // text
		template.node.data = text;
		break;
	}
}

//@private
function format(template, model) {
	return template.text.replace(_macrosRE_, function(match, name, value) {
		return resolve(name, model, template.view, value ? value.substring(1) : match);
	});
}

//@private
function resolve(name, model, view, value) {
	var result = null, exists = false;
	if (name in model) {
		result = model[name]; exists = true;
	} else if (view.isSet(name)) {
		result = view.get(name); exists = true;
	} else if (typeof view.controller[name] == "function") {
		result = view.controller[name](); exists = true;
	}
	return exists ? result : value;
}
