/**
 * @class
 * @imports System
 * 
 * @setting resource.base
 * @setting resource.module
 * @setting resource.type
 * @setting resource.content.delimiter
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@private
function initClass(scope) {
	$class.checkResource = $static.checkResource = System.checkResource;
	$class.initResourceHandle = $static.initResourceHandle = System.initResourceHandle;
	$class.clearResourceHandle = $static.clearResourceHandle = System.clearResourceHandle;
}

//@protected
function initResource(scope, source) {
}

//@static
function loadResource(name, context, callback) {
	context.loadResource(name, this, callback);
}

//@static
function findResource(name, positive) {
	return this.getContext().findResource(name, this, positive);
}

//@static
function parseResource(source) {
	switch (source.resourceType) {
	case "json":
		source.model = System.parseJSONFromText(source.content);
		break;
	case "xml":
		source.model = System.parseJSONFromXML(source.content.documentElement);
		break;
	case "csv":
		source.model = System.parseCSV(source.content, source.$class.$("resource.content.delimiter", "\t"));
		break;
	default:
		this.$static.parseSource(source);
	}
	this.$static.parseModel(source);
	delete source.content;
}

//@static
function parseSource(source) {
	source.model = source.content;
}

//@static
function parseModel(source) {
}
