/**
 * @resource
 * @imports modus.core.System
 * @setting resource.url
 * @setting resource.type=css
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@static
function initResourceHandle(source) {
	source.node = System.$document.createElement("link");
	source.node.href = System.formatText(this.$("resource.url"), this);
	source.node.rel = "stylesheet";
}

//@static
function clearResourceHandle(source) {
	delete source.node;
}

//@static
function checkResource() {
	return true;
}
