/**
 * @class
 * @extends Mapper
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@private
var _PROLOG_ = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>';

//@static
function write(value) {
	return _PROLOG_.concat(marshalGeneric(value));
}

//@static
function marshal(value) {
	var type = typeOf(value);
	switch (type) {
	case "object":
		return marshalObject(value);
	case "list":
		return marshalList(value);
	case "map":
		return marshalMap(value);
	case "null":
		return "";
	case "date":
		return value.toISOString();
	default:
		return "".concat(value);
	}
}

//@static
function marshalGeneric(value) {
	var type = typeOf(value);
	var result = marshal(value);
	switch (type) {
	case "object":
		return wrap(typeName(value), result);
	default:
		return result ? wrap(type, result) : empty(type);
	}
}

//@private
function wrap(name, value) {
	return "<".concat(name, ">", value, "</", name, ">");
}

//@private
function empty(name) {
	return "<".concat(name, "/>");
}

//@private
function marshalList(list) {
	var result = "";
	for (var i = 0; i < list.length; i++) {
		result = result.concat(marshalGeneric(list[i]));
	}
	return result;
}

//@private
function marshalMap(map) {
	var result = "";
	for (var name in map) {
		result = result.concat(wrap(name, marshalGeneric(map[name])));
	}
	return result;
}

//@private
function marshalObject(object) {
	var result = "";
	for (var name in object.$) {
		var value = object.$[name];
		var generic = object.getClass().$("modelAttribute."+name) == "generic";
		value = generic ? marshalGeneric(value) : marshal(value);
		result = result.concat(wrap(name, value));
	}
	return result;
}
