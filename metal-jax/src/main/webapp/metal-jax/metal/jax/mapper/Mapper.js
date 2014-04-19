/**
 * @class
 * @imports metal.jax.core.System
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@private
var _MIN_INT_ = -Math.pow(2, 31);
//@private
var _MAX_INT_ = Math.pow(2, 31)-1;

//@protected
function typeOf(value) {
	// undefined, boolean, number, string, object
	var type = typeof value;
	if (value === null) {
		return "null";
	} else if (value instanceof Date) {
		return "date";
	} else if (value instanceof Array) {
		return "list";
	} else if (value instanceof BaseObject) {
		return "object";
	} else if (type === "undefined") {
		return "null";
	} else if (type === "object") {
		return "map";
	} else if (type === "number" && value % 1) {
		return "double";
	} else if (type === "number" && value >= _MIN_INT_ && value <= _MAX_INT_) {
		return "int";
	} else if (type === "number") {
		return "long";
	} else {
		return type;
	}
}

//@protected
function typeName(value) {
	var name = System.parseBaseName(value.getClass().getName());
	name = name.charAt(0).toLowerCase().concat(name.substring(1));
	return value.getClass().$("modelRootElement", name);
}
