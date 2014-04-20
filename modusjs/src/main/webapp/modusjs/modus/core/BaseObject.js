/**
 * @class
 * @imports modus.core.Context
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@private
function initClass(scope) {
	$class.$ = $static.$ = Context.$;
	$class.extendsClass = $static.extendsClass = Context.extendsClass;
}

//@public
function get(name) {
	return valueOf.call(this, name);
}

//@public
function set(name, value) {
	return valueOf.call(this, name, value);
}

//@private
function valueOf() {
	this.$ = this.$ || {};
	switch (arguments.length) {
	case 0: return this.$;
	case 1: return this.$[arguments[0]];
	case 2: return this.$[arguments[0]] = arguments[1];
	}
}