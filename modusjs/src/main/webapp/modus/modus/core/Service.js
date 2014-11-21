/**
 * @class
 * @imports System
 * @imports Request
 * @imports modus.mapper.XmlMapper
 * @imports modus.mapper.JsonMapper
 * 
 * @setting service.base=${baseURL}
 * @setting service.module=service
 * @setting service.resourceType=xml
 * @setting service.method=post
 * @setting contentType.form=application/x-www-form-urlencoded
 * @setting contentType.xml=application/xml
 * @setting contentType.json=application/json
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@private
function initClass(scope) {
	scope.$mappers = {
		xml: XmlMapper,
		json: JsonMapper
	};
}

//@private
function initObject(service, caller) {
	service.caller = caller;
	service.callbacks = {};
}

//@public
function bind(action, callback) {
	this.callbacks[action] = callback;
}

//@protected
function callService(service, action, model, clazz) {
	try {
		var source = initServiceHandle(service, action, model, clazz);
		log("debug", "request to call service '${name}:${resourceType}'", source);
		System.loadback(source, handleResponse);
	} catch (ex) {
		source.error = ex.message;
		log("error", "failed to call service '${name}:${resourceType}': ${error}", source);
		service.callbacks.error.call(service.caller, source.error);
	}
}

//@private
function initServiceHandle(service, action, model, clazz) {
	var source = { "$class": clazz };
	source.service = service;
	source.action = action;
	source.name = service.getClass().getName();
	source.base = service.getClass().$("service.base");
	source.module = service.getClass().$("service.module");
	source.resourceType = service.getClass().$("service.resourceType");
	source.path = service.getClass().$("service.path") || source.name.split(".").join("/");
	source.path = source.path.concat("/", source.action, ".", source.resourceType);
	source.method = service.getClass().$("service.method").toUpperCase();
	marshalRequest(source, model);
	return source;
}

//@private
function marshalRequest(source, model) {
	var request = new Request(model);
	var format = (source.method == "GET" || !System.isSameDomain(source.base)) ? "form" : source.resourceType;
	source.contentType = $("contentType."+format);
	source.input = $mappers[format].write(request);
}

//@private
function handleResponse(source) {
	if (source.content) {
		log("debug", "response for '${name}:${resourceType}' from ${url}", source);
		try {
			if (source.$class) {
				source.$class.call("parseResource", source);
			}
		} catch (ex) {
			source.error = ex.message;
			log("error", "failed to parse '${name}:${resourceType}' from ${url}: ${error}", source);
		}
	} else {
		if (!source.error) source.error = "empty response content";
		log("error", "failed to load '${name}:${resourceType}' from ${url}: ${error}", source);
	}
	if (source.status < 100) {
		source.error = source.error || "bad response status: ".concat(source.status);
	}
	if (source.error || source.model.messages) {
		source.service.callbacks.error.call(source.service.caller, source.error || source.model.messages);
	} else {
		var result = source.$class ? new source.$class(source.model.result) : source.model;
		source.service.callbacks[source.action].call(source.service.caller, result);
	}
}
