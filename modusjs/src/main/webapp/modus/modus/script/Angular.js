/**
 * @script
 * @imports modus.core.System
 * @setting resource.url=${angular.js.url}
 * @setting resource.object=angular
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@private
var $modules = {};

//@private
var $controllers = {};

//@private
function initContext(scope) {
	var names = [];
	for (var i in $modules) {
		names.push(i);
	}
	angular.bootstrap(System.$document.body, names);
}

//@static
function addModule(name, requires) {
	$modules[name] = angular.module(name, requires||[]);
}

//@static
function addController(name, module, fn) {
	$controllers[module.concat(".", name)] = $modules[module].controller(name, fn);
}
