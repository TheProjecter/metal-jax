/**
 * @test
 * @imports metal.jax.core.System
 */

//@public
function test_callback() {
	var object = new Object();
	var callback = System.callback(function() {
		return this;
	});
	assertEquals(System.$window, callback());
	assertEquals(object, callback.call(object));
	
	callback = System.callback.call(object, function() {
		return this;
	});
	assertEquals(object, callback());
	assertEquals(object, callback.call(object));
}

//@public
function test_loadback() {
	var source = { base:$("bootURL"), module:$("bootModule"), path:"boot.js" };
	System.loadback(source, function(source) {
		assertEquals(true, source.content.length>0, "test_loadback");
	});
}

//@public
function test_formatText() {
	assertEquals(undefined, System.formatText());
	assertEquals("", System.formatText(""));
	assertEquals("hello", System.formatText("hello"));
	
	assertEquals("hello undefined world", System.formatText("hello ${0} ${1}", undefined, "world"));
	assertEquals("hello null world",      System.formatText("hello ${0} ${1}", null, "world"));
	assertEquals("hello  world",          System.formatText("hello ${0} ${1}", "", "world"));
	assertEquals("hello 0 world",         System.formatText("hello ${0} ${1}", 0,  "world"));
	assertEquals("hello 0 ${2}",          System.formatText("hello ${0} ${2}", 0,  "world"));
	assertEquals("hello 0 hello",         System.formatText("hello ${0} ${2:hello}", 0, "world"));
	
	assertEquals("hello undefined world", System.formatText("hello ${0} ${1}", {"0":undefined, "1":"world"}));
	assertEquals("hello null world",      System.formatText("hello ${0} ${1}", {"0":null, "1":"world"}));
	assertEquals("hello  world",          System.formatText("hello ${0} ${1}", {"0":"", "1":"world"}));
	assertEquals("hello 0 world",         System.formatText("hello ${0} ${1}", {"0":0,  "1":"world"}));
	assertEquals("hello 0 ${2}",          System.formatText("hello ${0} ${2}", {"0":0,  "1":"world"}));
	assertEquals("hello 0 hello",         System.formatText("hello ${0} ${2:hello}", {"0":0, "1":"world"}));
}

//@public
function test_resolvePath() {
	var base = "http://host/base/root";
	assertEquals("http://host/hello/world", System.resolvePath("http://host/hello/world", base));
	assertEquals("http://host/hello/world", System.resolvePath("/hello/world", base));
	assertEquals("http://host/base/hello/world", System.resolvePath("hello/world", base));
	assertEquals("http://host/base", System.resolvePath("", base));
}

//@public
function test_splitPath() {
	var path = "a/b/c/d/e";
	
	assertEquals("a/b/c/d/e", System.splitPath(path, -2, 0));
	assertEquals("a/b/c/d/e", System.splitPath(path, -1, 0));
	assertEquals("a/b/c/d/e", System.splitPath(path, 0, 0));
	assertEquals("a/b/c/d/e", System.splitPath(path, 1, 0));
	assertEquals("a/b/c/d/e", System.splitPath(path, 2, 0));
	
	assertEquals("a/b/c", System.splitPath(path, -2, -2));
	assertEquals("a/b/c/d", System.splitPath(path, -1, -2));
	assertEquals("c/d/e", System.splitPath(path, 0, -2));
	assertEquals("a/d/e", System.splitPath(path, 1, -2));
	assertEquals("a/b/e", System.splitPath(path, 2, -2));
	
	assertEquals("a/b/c/e", System.splitPath(path, -2, -1));
	assertEquals("a/b/c/d", System.splitPath(path, -1, -1));
	assertEquals("b/c/d/e", System.splitPath(path, 0, -1));
	assertEquals("a/c/d/e", System.splitPath(path, 1, -1));
	assertEquals("a/b/d/e", System.splitPath(path, 2, -1));
	
	assertEquals("d", System.splitPath(path, -2, 1));
	assertEquals("e", System.splitPath(path, -1, 1));
	assertEquals("a", System.splitPath(path, 0, 1));
	assertEquals("b", System.splitPath(path, 1, 1));
	assertEquals("c", System.splitPath(path, 2, 1));
	
	assertEquals("d/e", System.splitPath(path, -2, 2));
	assertEquals("e", System.splitPath(path, -1, 2));
	assertEquals("a/b", System.splitPath(path, 0, 2));
	assertEquals("b/c", System.splitPath(path, 1, 2));
	assertEquals("c/d", System.splitPath(path, 2, 2));
}
