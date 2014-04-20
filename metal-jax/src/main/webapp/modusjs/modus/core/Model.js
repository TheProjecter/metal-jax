/**
 * @resource
 * @imports BaseObject
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@protected
function initClass(scope) {
	scope.$modelAttributes = scope.$class.$("modelAttributes", "").split(",");
}

//@private
function initObject(object, model) {
	if (this.$modelAttributes[0] && model instanceof BaseObject) {
		for (var i = 0; i < this.$modelAttributes.length; i++) {
			var name = this.$modelAttributes[i];
			object.set(name, model.get(name));
		}
	} else if (this.$modelAttributes[0] && model instanceof BaseObject) {
		for (var i = 0; i < this.$modelAttributes.length; i++) {
			var name = this.$modelAttributes[i];
			object.set(name, model[name]);
		}
	} else {
		for (var name in model) {
			object.set(name, model[name]);
		}
	}
}
