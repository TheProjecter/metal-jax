/**
 * @class
 * @extends Mapper
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@static
function write(value) {
	return marshal(value);
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
		return "null";
	case "date":
		return "".concat(value.getTime());
	case "string":
		return "\"".concat(value, "\"");
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
	case "long":
	case "date":
		return wrap(type, result);
	default:
		return result;
	}
}

//@private
function wrap(name, value) {
	return "{\"".concat(name, "\":", value, "}");
}

//@private
function marshalList(list) {
	var result = "[", started;
	for (var i = 0; i < list.length; i++) {
		if (started) result = result.concat(",");
		else started = true;
		result = result.concat(marshalGeneric(list[i]));
	}
	return result.concat("]");
}

//@private
function marshalMap(map) {
	var result = "{", started;
	for (var name in map) {
		if (started) result = result.concat(",");
		else started = true;
		result = result.concat("\"", name, "\":", marshalGeneric(map[name]));
	}
	return result.concat("}");
}

//@private
function marshalObject(object) {
	var result = "{", started;
	for (var name in object.$) {
		var value = object.$[name];
		var generic = object.getClass().$("modelAttribute."+name) == "generic";
		value = generic ? marshalGeneric(value) : marshal(value);
		if (started) result = result.concat(",");
		else started = true;
		result = result.concat("\"", name, "\":", value);
	}
	return result.concat("}");
}
