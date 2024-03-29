/**
 * @class
 * @imports System
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@private
var $levels = {};

//@private
function initClass(scope) {
	$levels.info = $("logger.info", "true");
	$levels.warn = $("logger.warn", "true");
	$levels.error = $("logger.error", "true");
	$levels.debug = $("logger.debug", "true");
}

//@static
function log(msg) {
	if (typeof console != "undefined") {
		var level = (msg in $levels) ? Array.prototype.shift.call(arguments) : "info";
		var enabled = this.$("logger."+level, $levels[level]) == "true";
		if (enabled) {
			msg = this.$(msg, msg);
			msg = System.formatText.apply(null, arguments);
			var line = formatLine.call(this, level.toUpperCase(), msg);
			if (typeof console[level] != "undefined") console[level](line);
			else console.log(line);
		}
	}
}

//@private
function formatLine(level, msg) {
	if (typeof msg != "string") return msg;
	var category = System.parseBaseName(this.getName());
	return System.formatText("${time} ${level} ${category} - ${msg}", {time:now(), level:level, category:category, msg:msg});
}

//@private
function now() {
	var d = new Date();
	return (d.toJSON||d.toString).call(d);
}
