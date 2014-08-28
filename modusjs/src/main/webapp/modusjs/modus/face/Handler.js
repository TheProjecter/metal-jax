/**
 * @class
 * @imports View
 * @imports modus.core.System
 * 
 * @copyright Jay Tang 2014. All rights reserved.
 */

//@static
function newView(view, node, setting) {
	var controllerClass = view.getClass().getContext().findClass(setting.controller);
	var controller = controllerClass ? new controllerClass() : view.controller;
	if (setting.view) {
		var resource = view.getClass().findResource(setting.view, true).cloneNode(true);
		if (resource) {
			var part = toDocFrag(node, System.$document.createElement("div"));
			part.className = "part";
			node.appendChild(resource);
			node.appendChild(part);
		}
	}
	return view.views[node.id] = new View(node, controller, setting);
}

//@static
function newPlaceholder(view, node, setting) {
	var placeholder = { name:setting.placeholder, node:node };
	if ("repeat" in setting) placeholder.repeat = node.innerHTML;
	view.placeholders[placeholder.name] = placeholder;
	return placeholder;
}

//@static
function newPart(view, node, setting) {
	var part = { name:setting.part, node:node };
	view.parts[part.name] = part;
	return part;
}

//@static
function newScope(view, node, setting) {
	var scope = { name:setting.scope, node:node, beans:[] };
	if ("repeat" in setting) scope.repeat = node.innerHTML;
	newBean(scope);
	view.scopes.push(scope);
	return scope;
}

//@static
function newBean(scope) {
	var bean = { templates:[], inputs:[], bindings:[] };
	scope.beans.push(bean);
	return bean;
}

//@static
function newTemplate(scope, node, bean) {
	var template = { node:node, text:node.nodeValue };
	(bean||scope.beans[0]).templates.push(template);
	return template;
}

//@static
function newInput(bean, node) {
	var input = { name:node.name, type:node.type, node:node };
	bean.inputs.push(input);
	return input;
}

//@static
function newBinding(scope, node, event, action) {
	var binding = { event:event, action:action, node:node };
	scope.beans[0].bindings.push(binding);
	return binding;
}

//@static
function newJson(view, node, setting) {
	var json = System.parseJSONFromText(node.text);
	if (setting.scope) {
		view.set(setting.scope, json);
	} else {
		for (var key in json) {
			view.set(key, json[key]);
		}
	}
	return json;
}

//@static
function toDocFrag(source, doc) {
	var node = source;
	if (typeof source == "string") {
		node = System.$document.createElement("div");
		node.innerHTML = source;
	}
	doc = doc || System.$document.createDocumentFragment();
	while (node.firstChild) {
		if (node.firstChild.nodeType === 1) {
			doc.appendChild(node.firstChild);
		} else {
			node.removeChild(node.firstChild);
		}
	}
	return doc;
}
