//@ sourceURL=modus.core.Context
/**
 * @copyright Jay Tang 2012. All rights reserved.
 */
$public.getClass = function getClass() { return Context; };
$static.getSuperClass = function getSuperClass() { return undefined; };
$static.getContext = function getContext() { return $context; };
$static.getName = function getName() { return "modus.core.Context"; };
$private.Context = function Context() { return $protected.call(this, arguments); };
$imports["BaseObject"] = "modus.core.BaseObject";
$imports["System"] = "modus.core.System";
$imports["Logger"] = "modus.core.Logger";
$imports["modus.resource.Messages"] = "modus.resource.Messages";
$imports["modus.resource.Properties"] = "modus.resource.Properties";
$requires["modus.modus-messages"] = "modus.resource.Messages";
$requires["modus.modus-conf"] = "modus.resource.Properties";
$imports["BaseContext"] = $setting["baseContext"];

$private.initClass_ = function initClass_(scope) {
	scope.$source.classes[scope.$source.name] = scope.$source;
	scope._callbacks = [];
	scope._busyCount = 0;
	scope._pendingCount = 0;
	scope._pendingClasses = [];
	scope._pendingClasses.push(scope.$source);
	flush();
};

$private.initContext = function initContext(scope) {
	log("info", "modus.init.bootContext", $setting);
	if ($setting["baseModule"] == ".") {
		log("warn", "modus.init.baseModule.default", $setting);
	}
	if (BaseContext) {
		log("info", "modus.init.baseContext", $setting);
	} else {
		log("warn", "modus.init.baseContext.fail", $setting);
	}
	log("info", "modus.init.baseResource", $setting);
	if (BaseContext && BaseContext.main) {
		BaseContext.main();
	}
};

$protected.call = function call(target, args) {
	if (args && args.length == 1 && args[0] == this.$class) {
	} else if (target instanceof String && target in this.$static) {
		return this.$static[target].apply(this.$static, args);
	} else if (target instanceof BaseObject) {
		Array.prototype.unshift.call(args, target);
		callSuper("initObject", this.$class, this.$protected, args);
	}
	return target;
};

$private.callSuper = function callSuper(action, clazz, scope, args) {
	if (clazz.getSuperClass()) {
		callSuper(action, clazz.getSuperClass(), scope, args);
	}
	var source = getClassHandle(clazz);
	if (action in source.scope) {
		if (args) source.scope[action].apply(scope||source.scope, args);
		else source.scope[action].apply(scope||source.scope);
	}
};

$static.extendsClass = function extendsClass(that) {
	var clazz = this;
	while (clazz && that) {
		if (clazz == that || clazz.getName() == that) return true;
		clazz = clazz.getSuperClass();
	}
};

$static.$ = function $(name, value, solid) {
	var clazz = this;
	while (clazz) {
		var source = getClassHandle(clazz);
		if (name in source.scope.$setting) {
			return source.scope.$setting[name] || solid && value || source.scope.$setting[name];
		}
		clazz = clazz.getSuperClass();
		if (clazz && this == this.getContext().getClass()) {
			clazz = clazz.getContext().getClass();
		}
	}
	if (this != this.getContext().getClass()) {
		return arguments.callee.call(this.getContext().getClass(), name, value, solid);
	}
	return value;
};

$protected.throwError = function throwError(msg) {
	msg = this.$class.$(msg, msg);
	throw Error(formatText.apply(null, arguments));
};

$protected.log = function log() {
	if (typeof Logger != "undefined" && Logger.log) {
		Logger.log.apply(this.$class, arguments);
	} else if (typeof console != "undefined") {
		var level = Array.prototype.shift.call(arguments);
		if (level != "debug") {
			var line = formatText.apply(null, arguments);
			if (typeof console[level] != "undefined") console[level](line);
			else console.log(line);
		}
	}
};

$private.getResourceHandle = function getResourceHandle(clazz) {
	var source = getClassHandle(clazz);
	while (source.stereotype != "resource") {
		clazz = clazz.getSuperClass();
		source = getClassHandle(clazz);
	}
	return source;
};

$private.getResourceHolder = function getResourceHolder(clazz) {
	var source = getResourceHandle(clazz);
	source.resources = source.resources || {};
	return source.resources;
};

$private.putResourceHandle = function putResourceHandle(source) {
	source = (arguments.length == 1) ? source : initResourceHandle.apply(this, arguments);
	getResourceHolder(source.$class)[source.name] = source;
	return source;
};

$private.initResourceHandle = function initResourceHandle(name, clazz, context) {
	var setting = findSourceSetting(name, context);
	var source = { "$class": clazz };
	source.name = name;
	source.url = formatText(clazz.$("resource.url"), clazz);
	source.base = clazz.$("resource.base") || setting && setting.base || $("baseURL");
	source.module = clazz.$("resource.module") || setting && setting.module || $("baseModule");
	source.resourceType = clazz.$("resource.type") || $("viewType");
	source.path = source.name.split(".").join("/").concat(".", source.resourceType);
	return source;
};

$private.getClassHandle = function getClassHandle(clazz) {
	var name = (arguments.length == 1) ? clazz.getName() : arguments[0];
	var context = (arguments.length == 1) ? clazz.getContext() : arguments[1];
	return $source.classes[context.getClass().getName()].classes[name];
};

$private.putClassHandle = function putClassHandle(source) {
	source = (arguments.length == 1) ? source : initClassHandle.apply(this, arguments);
	$source.classes[source.$context.getClass().getName()].classes[source.name] = source;
	if (source.stereotype == "context") {
		$source.classes[source.name] = source;
	}
	return source;
};

$private.initClassHandle = function initClassHandle(name, context) {
	name = parseSourceName(name);
	var setting = findSourceSetting(name, context);
	var source = { "$context": setting ? setting.$context : $context };
	source.name = name;
	source.base = setting && setting.base || $("baseURL");
	source.module = setting && setting.module || $("baseModule");
	source.resourceType = $("classType");
	source.path = source.name.split(".").join("/").concat(".", source.resourceType);
	return source;
};

$private.findClassHandle = function findClassHandle(name, context) {
	if (name) {
		name = parseSourceName(name);
		var setting = findSourceSetting(name, context);
		return getClassHandle(name, setting ? setting.$context : $context);
	}
};

$private.findSourceSetting = function findSourceSetting(name, context) {
	var clazz = context.getClass();
	while (clazz) {
		var settings = getClassHandle(clazz).setting["source"];
		for (var i = 0; i < settings.length; i++) {
			var setting = settings[i];
			if (setting["includes"]) {
				if (setting["includes"].test(name)) {
					if (!(setting["excludes"] && setting["excludes"].test(name))) {
						return setting;
					}
				}
			} else if (setting["excludes"]) {
				if (!setting["excludes"].test(name)) {
					return setting;
				}
			}
		}
		clazz = clazz.getSuperClass();
		if (clazz) clazz = clazz.getContext().getClass();
	}
};

$private.findStereotypeSetting = function findStereotypeSetting(type, context) {
	var clazz = context.getClass();
	while (clazz) {
		var settings = getClassHandle(clazz).setting["stereotype"];
		for (var i = 0; i < settings.length; i++) {
			var setting = settings[i];
			if (setting.type == type) {
				return setting;
			}
		}
		clazz = clazz.getSuperClass();
		if (clazz) clazz = clazz.getContext().getClass();
	}
};

$public.findResource = function findResource(name, clazz, positive) {
	if (!name) return;
	var source = getResourceHolder(clazz)[name];
	if (source) {
		if (!source.error) {
			return source.model;
		} else {
			var msg = "resource '${name}:${resourceType}' was not loaded: ${error}";
			log("error", msg, source);
			if (positive) {
				throwError(msg, source);
			}
		}
	}
};

$public.loadResource = function loadResource(name, clazz, callback) {
	try {
		if (!name || getResourceHolder(clazz)[name]) return;
		_pendingCount++;
		var source = putResourceHandle(name, clazz, this);
		log("debug", "request to load resource '${0}:${1}' from context '${2}'", name, source.resourceType, this.getClass().getName());
		System.loadback(source, handleResponse);
	} catch (ex) {
		_pendingCount--;
		source.error = ex.message;
		log("error", "failed to load resource '${name}:${resourceType}': ${error}", source);
	} finally {
		callLater(callback);
	}
};

$public.findClass = function findClass(name, positive) {
	var source = findClassHandle(name, this);
	if (source) {
		if (!source.error) {
			return source.$class;
		} else {
			var msg = "class '${name}' was not loaded: ${error}";
			log("error", msg, source);
			if (positive) {
				throwError(msg, source);
			}
		}
	}
};

$public.loadClass = function loadClass(name, callback) {
	try {
		if (!name || findClassHandle(name, this)) return;
		_pendingCount++;
		var source = putClassHandle(name, this);
		log("debug", "request to load class '${0}' from context '${1}'", name, this.getClass().getName());
		loadback(source, handleResponse);
	} catch (ex) {
		_pendingCount--;
		source.error = ex.message;
		log("error", "failed to load class '${name}': ${error}", source);
	} finally {
		callLater(callback);
	}
};

$protected.extendClass = function extendClass(clazz) {
	var source = { "$context":this.$class.getContext() };
	source.name = clazz.getName().concat(new Date().getTime());
	source.superBase = parseBaseName(clazz.getName());
	parseModel(putClassHandle(source));
	source.scope.$imports[source.superBase] = clazz.getName();
	source.scope[source.superBase] = clazz;
	initClassModel(source);
	return source.$class;
};

$private.callLater = function callLater(callback) {
	if (!callback) return;
	if (_pendingCount) {
		_callbacks.push(callback);
	} else {
		callback();
	}
};

$private.handleResponse = function handleResponse(source) {
	log("debug", "response for '${name}:${resourceType}' from ${url}", source);
	try {
		if (source.$class) {
			if (source.$class.checkResource()) {
				//nothing
			} else if (source.content) {
				source.$class.call("parseResource", source);
			} else {
				if (!source.error) source.error = "empty response content";
				log("error", "failed to load '${name}:${resourceType}' from ${url}: ${error}", source);
			}
		} else if (source.content) {
			parseSource(source);
		} else {
			if (!source.error) source.error = "empty response content";
			log("error", "failed to load '${name}:${resourceType}' from ${url}: ${error}", source);
		}
	} catch (ex) {
		source.error = ex.message;
		log("error", "failed to parse '${name}:${resourceType}' from ${url}: ${error}", source);
		}
	if (source.status < 100) {
		//error
	} else {
		_pendingCount--;
		flush();
	}
};

$private.parseSource = function parseSource(source) {
	parseModel(source);
	_pendingClasses.push(source);
	source.$context.loadClass(source.scope.$imports[source.superBase]);
	if (source.stereotype != "context") {
		for (var name in source.scope.$imports) {
			source.$context.loadClass(source.scope.$imports[name]);
		}
	}
};

$private._classHeader_   = "//@ sourceURL=${0}\n";
$private._getClass_      = "$public.getClass = function getClass() { return ${0}; };\n";
$private._getSuperClass_ = "$static.getSuperClass = function getSuperClass() { return ${0}; };\n";
$private._getContext_    = "$static.getContext = function getContext() { return $context; };\n";
$private._getName_       = "$static.getName = function getName() { return \"${0}\"; };\n";
$private._constructor_   = "$private.${0} = function ${0}() { return $protected.call(this, arguments); };\n";
$private._memberRE_      = /^\/\/@(\S+)\s+(function|var)\s+([A-Za-z0-9_$]+)/gm;
$private._classFooter_   = "\n//@ sourceURL=${0}\n";
$private._classHeaderRE_ = /^\s*\/\*\*([\S\s]*?)\*\//;
$private._classSettingRE_ = /@(setting|source|stereotype)[\t ]+([\S\t ]*\S+)[\t ]*$|@(extends|imports|requires)[\t ]+(\S+)[\t ]*(\S+)?[\t ]*$|@([a-zA-Z]+)[\t ]*(\S+)?[\t ]*$/gm;
$private.parseModel = function parseModel(source) {
	source.scope = { $setting:{}, $imports:{}, $requires:{}, $static:{}, $protected:{}, $public:{} };
	source.scope.$private = source.scope;
	source.scope.$context = source.$context;
	var match = _classHeaderRE_.exec(source.content);
	if (match) {
		source.content = source.content.substring(match[0].length);
		var header = match[1];
		while (match = _classSettingRE_.exec(header)) {
			var settingTag = match[1]||match[3]||match[6], name = match[2]||match[4]||match[7], value = match[5];
			if (!settingTag) continue;
			switch (settingTag) {
			case "extends":
				name = formatText(name, source.$context.getClass());
				name = parseSourceName(name, source.name);
				value = parseBaseName(name, value);
				if (value) {
					source.scope.$imports[value] = name;
					source.superBase = source.superBase || value;
				} else {
					log("warn", "incorrect @${0} format in class source: ${1}", settingTag, source.name);
				}
				break;
			case "imports":
				name = formatText(name, source.$context.getClass());
				name = parseSourceName(name, source.name);
				value = parseBaseName(name, value);
				if (value) {
					source.scope.$imports[value] = name;
				}
				break;
			case "requires":
				name = formatText(name, source.$context.getClass());
				name = parseSourceName(name, source.name);
				value = formatText(value, source.$context.getClass());
				if (name) {
					source.scope.$imports[parseBaseName(name, value)] = name;
					source.scope.$imports[name] = name;
					source.scope.$requires[value||name] = name;
				} else {
					log("warn", "incorrect @${0} format in class source: ${1}", settingTag, source.name);
				}
				break;
			case "setting":
				parseSetting(source.scope.$setting, match);
				break;
			case "source":
			case "stereotype":
				if (source.stereotype == "context") {
					source.setting[settingTag].push(parseSetting({}, match));
				}
				break;
			case "class":
			case "context":
			case "resource":
			default:
				if (source.stereotype) break;
				source.stereotype = settingTag;
				name = name || source.name;
				if (source.name != parseSourceName(name, source.name)) {
					throwError("incorrect ${stereotype} name declared: ${name}", source);
				}
				if (source.stereotype == "context") {
					source.classes = {};
					source.setting = { source:[], stereotype:[] };
				}
				break;
			}
		}
	}
	source.stereotype = source.stereotype || "class";
	source.baseName = parseBaseName(source.name);
	if (!source.superBase && source.name != $imports.BaseObject) {
		var setting = findStereotypeSetting(source.stereotype, source.$context);
		var superName = setting && setting["base"] || $imports.BaseObject;
		source.superBase = parseBaseName(superName);
		source.scope.$imports[source.superBase] = superName;
	}
	source.content = formatText(_classHeader_, source.name).concat(
		formatText(_getClass_, source.baseName),
		formatText(_getSuperClass_, source.superBase),
		_getContext_,
		formatText(_getName_, source.name),
		formatText(_constructor_, source.baseName),
		source.content ? source.content.replace(_memberRE_, formatMember) : "",
		formatText(_classFooter_, source.name)
	);
	compile.call(source);
};

$static.parseSourceName = function parseSourceName(name, base) {
	var parts; base = base || "";
	if (name && name.indexOf(".") < 0 && base.indexOf(".") > 0) {
		parts = base.split(".");
		parts[parts.length-1] = name;
		name = parts.join(".");
	}
	if (name && name.charAt(0) == ".") name = name.substring(1);
	return name;
};

$static.parseBaseName = function parseBaseName(name, value) {
	if (value && value.indexOf(".") < 0) return value;
	return splitPath(name, -1, 1, ".") || "";
};

$private._settingRE_ = /([^=\t ]+)(?:=|\s*)(.*)/;
$private._settingsRE_ = /([^=\t ]+)=(\S+)/g;
$private.parseSetting = function parseSetting(setting, match) {
	var input = match[0], settingTag = match[1], content = match[2], found;
	switch (settingTag) {
	case "setting":
		if (match = _settingRE_.exec(content)) {
			setting[match[1]] = match[2];
			found = true;
		}
		break;
	case "source":
	case "stereotype":
		while (match = _settingsRE_.exec(content)) {
			setting[match[1]] = match[2];
			found = true;
		}
		break;
	}
	if (!found) {
		throwError("incorrect ${0} declared: ${1}", settingTag, input);
	}
	return setting;
};

$private._members_ = {
	"var": "$${0}.${2}",
	"function": "$${0}.${2} = ${1} ${2}"
};
$private.formatMember = function formatMember(match, memberTag, memberType, name) {
	return (memberType in _members_) ? formatText(_members_[memberType], memberTag, memberType, name) : match;
};

$private._NULL_ = {};
$private._formatRE_ = /\$\{([^\s\}:|]+)([:|][^\}]*)?\}/g;
$static.formatText = function formatText() {
	var args = arguments;
	var text = Array.prototype.shift.call(arguments);
	if (typeof text != "string" || text.indexOf("${") < 0) return text;
	var argType = typeof args[0];
	return text.replace(_formatRE_, function(match, name, value) {
		var result = _NULL_;
		if (argType != "object" && argType != "function" || args.length > 1) {
			var idx = parseInt(name);
			if (idx in args) return ensureValue(args[idx], value);
		} else if (argType == "object") {
			if (name in args[0]) return ensureValue(args[0][name], value);
		} else if (argType == "function") {
			result = args[0].$(name, _NULL_);
		}
		return result != _NULL_ ? ensureValue(result, value) : value ? value.substring(1) : match;
	});
};

$private.ensureValue = function ensureValue(value, fallback) {
	var solid = fallback && fallback.charAt(0) == "|";
	return value || (solid && fallback.substring(1)) || value;
};

$private.flush = function flush() {
	for (var i = 0; i < _pendingClasses.length; i++) {
		if (_pendingCount) return;
		flush_initClass(_pendingClasses[i]);
	}
	for (var i = 0; i < _pendingClasses.length; i++) {
		if (_pendingCount) return;
		flush_notifyClass(_pendingClasses[i]);
	}
	for (var i = 0; i < _pendingClasses.length; i++) {
		if (_pendingCount) return;
		flush_loadResource(_pendingClasses[i]);
	}
	for (var i = 0; i < _pendingClasses.length; i++) {
		if (_pendingCount) return;
		flush_notifyResource(_pendingClasses[i]);
	}
	for (var i = 0; i < _pendingClasses.length; i++) {
		if (_pendingCount) return;
		flush_initContext(_pendingClasses[i]);
	}
	if (_busyCount) return;
	while (_callbacks.length) {
		if (_pendingCount) return;
		_callbacks.shift()();
	}
	for (var i = 0; i < _pendingClasses.length; i++) {
		if (_pendingCount) return;
		flush_notifyContext(_pendingClasses[i]);
	}
	for (var i = 0; i < _pendingClasses.length; ) {
		if (_pendingClasses[i].flushState == "endNotifyContext") {
			_pendingClasses.splice(i, 1);
		} else i++;
	}
};

$private.flush_initClass = function flush_initClass(source) {
	if (!source.flushState) {
		source.flushState = "beginInitClass";
		_busyCount++;

		if (source.superBase) {
			source.scope[source.superBase] = source.$context.findClass(source.scope.$imports[source.superBase], true);
			flush_initClass(findClassHandle(source.scope.$imports[source.superBase], source.$context));
		}
		if (source.stereotype != "context") {
			for (var name in source.scope.$imports) {
				source.scope[name] = source.$context.findClass(source.scope.$imports[name], true);
				flush_initClass(findClassHandle(source.scope.$imports[name], source.$context));
			}
		}
		if (source.name == $imports.BaseObject) {
			source.scope.$protected.throwError = $protected.throwError;
			source.scope.$protected.call = $protected.call;
			source.scope.$protected.log = $protected.log;
			source.scope.$protected.copy = $protected.copy;
		}
		initClassModel(source);

		if (source.stereotype == "context") {
			for (var name in source.scope.$imports) {
				source.$context.loadClass(source.scope.$imports[name]);
			}
		}

		_busyCount--;
		source.flushState = "endInitClass";

		if (source.stereotype != "context") {
			flush_notifyClass(source);
		}
	}
};

$private.flush_notifyClass = function flush_notifyClass(source) {
	if (source.flushState == "endInitClass") {
		source.flushState = "beginNotifyClass";
		_busyCount++;

		for (var name in source.scope.$imports) {
			source.scope[name] = source.$context.findClass(source.scope.$imports[name], true);
			flush_notifyClass(findClassHandle(source.scope.$imports[name], source.$context));
		}

		if (source.scope.initClass) {
			source.scope.initClass(source.scope);
		} else if (source.scope.$protected.initClass) {
			source.scope.$protected.initClass(source.scope);
		}

		_busyCount--;
		source.flushState = "endNotifyClass";
	}
};

$private.flush_loadResource = function flush_loadResource(source) {
	if (_busyCount) return;

	source.resourceState = source.resourceState || {};
	for (var name in source.scope.$requires) {
		if (source.resourceState[name] == "pendingInit") {
			source.resourceState[name] = "doneInit";
			var clazz = source.scope[source.scope.$requires[name]];
			if (source.$context.findResource(name, clazz)) {
				var handle = getResourceHandle(clazz);
				handle.scope.$protected.initResource(source.scope, handle.resources[name]);
			}
		}
	}
	for (var name in source.scope.$requires) {
		if (!source.resourceState[name]) {
			source.resourceState[name] = "pendingInit";
			source.scope[source.scope.$requires[name]].loadResource(name, source.$context);
		}
	}
	if (_pendingCount) return;
	for (var name in source.scope.$imports) {
		if (name == source.scope.$imports[name] && !source.scope[name]) {
			source.scope[name] = source.$context.findClass(source.scope.$imports[name], true);
			if (!source.scope[name]) {
				source.$context.loadClass(source.scope.$imports[name]);
			}
		}
	}
};

$private.flush_notifyResource = function flush_notifyResource(source) {
	if (source.flushState == "endNotifyClass" || source.flushState == "endNotifyResource") {
		source.flushState = "beginNotifyResource";
		_busyCount++;

		if (source.scope.initResource) {
			source.scope.initResource(source.scope);
		}

		_busyCount--;
		source.flushState = "endNotifyResource";
	}
};

$private.flush_initContext = function flush_initContext(source) {
	if (source.flushState == "endNotifyResource") {
		source.flushState = "beginInitContext";
		_busyCount++;

		for (var name in source.scope.$imports) {
			if (source.scope[name]) {
				flush_initContext(getClassHandle(source.scope[name]));
			}
		}
		if (source.stereotype == "context") {
			if (source.scope.initObject) {
				source.scope.initObject(source.$context);
			}
		}

		_busyCount--;
		source.flushState = "endInitContext";
	}
};

$private.flush_notifyContext = function flush_notifyContext(source) {
	if (source.flushState == "endInitContext") {
		source.flushState = "beginNotifyContext";
		_busyCount++;

		if (source.scope.initContext) {
			source.scope.initContext(source.scope);
		}

		_busyCount--;
		source.flushState = "endNotifyContext";
	}
};

$private.initClassModel = function initClassModel(source) {
	source.scope.$private.$class = source.$class;
	source.scope.$protected.$class = source.$class;
	source.scope.$static.$class = source.$class;
	source.scope.$public.$class = source.$class;
	source.scope.$protected.$protected = source.scope.$protected;
	source.scope.$protected.$static = source.scope.$static;
	source.scope.$protected.$public = source.scope.$public;
	source.scope.$static.$protected = source.scope.$protected;
	source.scope.$static.$static = source.scope.$static;
	source.scope.$static.$public = source.scope.$public;
	source.scope.$super = {};

	var superSource = findClassHandle(source.scope.$imports[source.superBase], source.$context);
	if (superSource) {
		copy(source.scope.$super, superSource.scope.$protected);
		source.scope.$super.$protected = source.scope.$super;
		source.scope.$super.$static = copy({}, superSource.scope.$static);
		source.scope.$super.$public = copy({}, superSource.scope.$public);
		copy(source.scope.$protected, source.scope.$super.$protected);
		copy(source.scope.$static, source.scope.$super.$static);
		source.$class.prototype = new superSource.$class(superSource.$class);
		copy(source.$class.prototype, source.scope.$super.$public);
		source.scope.$protected.$super = source.scope.$super.$protected;
		source.scope.$static.$super = source.scope.$super.$static;
		source.scope.$public.$super = source.scope.$super.$public;
	}
	copy(source.$class, source.scope.$static);
	copy(source.$class.prototype, source.scope.$public, true);
	copy(source.scope.$public, source.scope.$super.$public);
	if (source.stereotype == "context") {
		source.$context = source.scope.$context = new source.$class(source.$class);
		putClassHandle(source);
	}
	initClassSettings(source);
};

$private.initClassSettings = function initClassSettings(source) {
	initSetting(source.scope.$setting, source.$class);
	if (source.stereotype == "context") {
		var settings = source.setting["source"];
		for (var i = 0; i < settings.length; i++) {
			var setting = settings[i];
			initSetting(setting, source.$class);
			if (setting["includes"]) {
				setting["includes"] = new RegExp(setting["includes"]);
			}
			if (setting["excludes"]) {
				setting["excludes"] = new RegExp(setting["excludes"]);
			}
			setting.$context = source.$context;
		}
		var settings = source.setting["stereotype"];
		for (var i = 0; i < settings.length; i++) {
			var setting = settings[i];
			initSetting(setting, source.$class);
			setting.$context = source.$context;
		}
	}
};

$protected.copy = function copy(target, source, override) {
	if (source && target) {
		for (var name in source) {
			if (name.length > 1 && name.charAt(0) == "$") {
				continue;
			}
			target[name] = !override && target[name] || source[name];
		}
	}
	return target;
};

$private.initSetting = function initSetting(setting, clazz) {
	if (setting) {
		for (var name in setting) {
			setting[name] = formatText(setting[name], clazz);
		}
	}
};

//@ sourceURL=modus.core.Context
