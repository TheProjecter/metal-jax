/**
 * @resource
 * @imports modus.core.System
 * 
 * @setting resource.module=conf
 * @setting resource.type=properties
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@static
function parseSource(source) {
	source.model = System.parseProperties(source.content);
}

//@protected
function initResource(scope, source) {
	for (var name in source.model) {
		if (!(name in scope.$setting)) {
			scope.$setting[name] = this.$protected.formatText(source.model[name], scope.$class);
		}
	}
}

//@protected
function formatText(text, clazz) {
	return System.formatText(text, clazz);
}
