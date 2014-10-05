/**
 * @resource
 * @imports Node
 * @imports Internal
 * @imports Node
 * @imports Scope
 * @imports Placeholder
 * @imports modus.core.System
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@static
function parseSource(source) {
	source.model = Node.toDocFrag(source.content);
}

//@protected
function initResource(scope, source) {
	copy(scope.$imports, source.$imports);
	copy(scope.$requires, source.$requires);
}

//@static
function parseModel(source, node) {
	node = node || source.model;
	source.$imports = source.$imports || {};
	source.$requires = source.$requires || {};
	for (var i = 0; i < node.childNodes.length; i++) {
		var childNode = node.childNodes[i];
		if (childNode.nodeType == 1) {
			var done = parseNode(source, childNode);
			if (!done) {
				parseModel(source, childNode);
			}
		}
	}
}

//@private
function parseNode(source, node) {
	if (parseResourceNode(source, node)) {
		return true;
	}
	var setting = Internal.parseNodeSetting(node, source.name);
	if (setting.controller) {
		source.$imports[setting.controller] = setting.controller;
	}
	if (setting.view == source.name) {
		log("warn", "modus.resource.cycle", setting.view);
	} else if (setting.view) {
		source.$imports[$class.getName()] = $class.getName();
		source.$requires[setting.view] = $class.getName();
	}
}

//@private
function parseResourceNode(source, node) {
	if (node.parentNode.nodeName.toLowerCase() != "body" && !node.parentNode.offsetParent) {
		if (node.nodeName.toLowerCase() == "img") {
			node.src = Internal.resolvePath(node.attributes["alt"].value||node.attributes["src"].value, source);
			return true;
		} else if (node.nodeName.toLowerCase() == "link") {
			System.$head.appendChild(node);
			node.href = Internal.resolvePath(node.attributes["href"].value, source);
			return true;
		}
	}
}

//@private
function initObject(view, node, setting, controller) {
	Internal.initView(view, node, setting, controller);
	
	view.controller.getClass().initView(view);
	forEach(view.node.childNodes, Scope.scanScope, view.scope);
	
	view.controller.getClass().initModel(view);
	Placeholder.normalizePlaceholder(view);

	view.controller.getClass().initScope(view);
	forEach(view.scopes, Scope.initScope);
	
	Internal.afterInit(view);
}

//@public
function bindEvent(event, callback, node, bean) {
	bean = bean || this.scope.bean;
	var binding = System.callback.call(this, dispatch, callback, node, bean);
	if (event && node) Node.toggleEvent(event, binding, true, node);
	return binding;
}

//@private
function dispatch(callback, node, bean, event) {
	var status = callback(this, node, event, bean);
	if (status) return true;
	
	/*stop propagation*/
	if (event.stopPropagation) event.stopPropagation();
	event.cancelBubble = true;
	
	/*prevent default action in FF*/
	if (event.preventDefault) event.preventDefault();
	
	/*false to prevent default action in IE*/
	return false;
}
