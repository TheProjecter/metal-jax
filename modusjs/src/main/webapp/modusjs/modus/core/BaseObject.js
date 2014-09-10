/**
 * @class
 * @imports Context
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@private
function initClass(scope) {
	$class.$ = $static.$ = Context.$;
	$class.extendsClass = $static.extendsClass = Context.extendsClass;
}

//@private
function initObject(object) {
	object.$ = {};
}

//@static
function newObject() {
	return this.apply(new this(this), arguments);
}

//@public
function get(name) {
	return valueOf.call(this, name);
}

//@public
function set(name, value) {
	return valueOf.call(this, name, value);
}

//@public
function isSet(name) {
	return valueOf.call(this, name, null, null);
}

//@private
function valueOf() {
	switch (arguments.length) {
	case 0: return this.$;
	case 1: return this.$[arguments[0]];
	case 2: return this.$[arguments[0]] = arguments[1];
	case 3: return arguments[0] in this.$;
	}
}

//@protected
function forEach(/*items, callback*/) {
	var items = arguments[0];
	var callback = arguments[1];
	if (typeof items.length == "number") {
		for (var i = 0; i < items.length; i++) {
			iterate(i, items[i], arguments, callback, this);
		}
	} else {
		for (var i in items) {
			iterate(i, items[i], arguments, callback, this);
		}
	}
}

//@private
function iterate(key, value, args, callback, context) {
	args[0] = key;
	args[1] = value;
	callback.apply(context, args);
}
