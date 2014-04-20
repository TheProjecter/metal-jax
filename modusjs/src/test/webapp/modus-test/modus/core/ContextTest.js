/**
 * @test
 * 
 * @imports modus.core.Context
 * @imports modus.core.BaseObject
 * @imports modus.core.BaseObject Object2
 * @imports .base baseContext
 * 
 * @setting classLoader=modus.core.Context
 * @setting classLoader1=${classLoader}
 * @setting classLoader2=  ${classLoader}  ${classLoader}
 */

$imports.Object3 = "modus.core.BaseObject";
$setting["classLoader3"] = "${classLoader}";

//@private
function initClass(scope) {
	scope.initClassTested = true;
}

//@private
function initObject(object) {
	$private.initObjectTested = true;
}

//@public
function test_initClass() {
	assertEquals(true, initClassTested);
}

//@public
function test_initObject() {
	assertEquals(true, initObjectTested);
}

//@public
function test_classSetting() {
	assertEquals($context, baseContext.getContext());
	assertEquals($imports, $private.$imports);
	assertEquals($private, $private.$private);
	assertEquals($protected, $private.$protected);
	assertEquals($public, $private.$public);
	assertEquals($setting, $private.$setting);
	assertEquals($static, $private.$static);
	assertEquals($super, $private.$super);
	assertEquals($super.$protected, $private.$super.$protected);
	assertEquals($super.$static, $private.$super.$static);
	assertEquals($super.$public, $private.$super.$public);
	
	assertEquals($("classLoader"), Context.getName());
	assertEquals($("classLoader1"), Context.getName());
	assertEquals($("classLoader2"), "  ".concat(Context.getName(), "  ", Context.getName()));
	assertEquals($("classLoader3"), Context.getName());
	
	assertEquals(BaseObject, Object2);
	assertEquals(BaseObject, Object3);
}

//@public
function test_getSuperClass() {
	assertEquals(null, Context.getSuperClass());
}

//@public
function test_newObject() {
	assertEquals(Context, new Context().getClass());
	assertEquals(ContextTest, new ContextTest().getClass());
}

//@public
function test_loadClass() {
	$context.loadClass("NoSuchClass", function() {
		try {
			$context.findClass("NoSuchClass", true);
			fail("should fail");
		} catch (ex) {}
	});
}

//@public
function test_findClass() {
	assertEquals(Context, new Context().findClass(Context.getName()));
}
