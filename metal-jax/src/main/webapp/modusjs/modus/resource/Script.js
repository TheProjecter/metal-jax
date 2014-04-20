/**
 * @resource
 * @imports modus.core.System
 * @setting resource.url
 * @setting resource.object
 * @setting resource.type=js
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@static
function checkResource() {
	var name = this.$("resource.object");
	if (name) {
		return typeof System.$window[name] != "undefined";
	}
}
