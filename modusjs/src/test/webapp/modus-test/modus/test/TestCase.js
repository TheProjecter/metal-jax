/**
 * @class
 * @imports modus.core.System
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@public
function run() {
	info(this.getClass().getName());
	for (var name in this.getClass().prototype) {
		if (name.indexOf("test") == 0 && name.length > 4) {
			this.setUp && this.setUp();
			runTest.call(this, name);
			this.tearDown && this.tearDown();
		}
	}
}

//@private
function runTest(name) {
	try {
		this[name]();
		passTest(name);
	} catch (ex) {
		failTest(name, ex.message);
	}
}

//@private
function passTest(name) {
	info("+-------".concat(name, ": pass"));
}

//@private
function failTest(name, message) {
	error("+-------".concat(name, ": fail; ", message));
}

//@private
function info(message) {
	println(message, "green");
}

//@private
function error(message) {
	println(message, "red");
}

//@static
function fail(message, name) {
	if (name) {
		failTest(name, message);
	} else {
		log("error", message);
		throwError(message);
	}
}

//@static
function assertEquals(expected, actual, name) {
	if (expected === actual) return;
	fail(System.formatText("expected \"${0}\", but was \"${1}\"", expected, actual), name);
}

//@protected
function println(message, color) {
	var line = System.$document.createElement("pre");
	line.style.margin = "0px";
	if (color) line.style.color = color;
	line.innerHTML = formatHTML(message);
	System.$document.body.appendChild(line);
}

//@private
var _tagRE_ = /[<>]/g;

//@private
function formatHTML(message) {
	return message.toString().replace(_tagRE_, function(match) {
		switch (match) {
		case "<":
			return "&lt;";
		case ">":
			return "&gt;";
		default:
			return match;
		}
	});
}
