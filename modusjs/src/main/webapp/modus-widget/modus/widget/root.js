/**
 * @context
 * @extends ${baseContext}
 * @imports modus.core.System
 * @requires modus.face.View ${baseResource}
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@private
var $root = null;

//@private
function initResource(scope) {
	View.parseModel(scope, System.$document.body);
}

//@private
function initContext(scope) {
	var View0 = scope.$protected.extendClass(View);
	var baseResource = $("baseResource");
	var node = View0.findResource(baseResource);
	if (node) {
		System.$document.body.appendChild(node.cloneNode(true));
	} else if (baseResource) {
		log("warn", "modus.init.baseResource.fail", baseResource);
	}
	$root = new View0(System.$document.body, {view:baseResource, controller:$("baseClass", "modus.face.Controller", true)});
}
