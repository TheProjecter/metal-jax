/**
 * @test
 * @imports metal.jax.BaseObject
 * @setting hello=hello
 */

//@private
function initObject(object) {
	object.hello = "hello";
}

//@public
function test_initObject() {
	assertEquals("hello", this.hello);
}

//@public
function test_initObjectCall() {
	assertEquals(getName(), BaseObjectTest.call("getName"));
}

//@public
function test_getClass() {
	assertEquals(BaseObjectTest, this.getClass());
}

//@public
function test_getSuperClass() {
	assertEquals(TestCase, this.getClass().getSuperClass());
}

//@public
function test_getName() {
	assertEquals("metal.jax.BaseObjectTest", this.getClass().getName());
}

//@public
function test_$() {
	assertEquals("hello", $("hello"));
	assertEquals("hello", BaseObjectTest.$("hello"));
}

//@public
function test_newObject() {
	assertEquals(BaseObject, new BaseObject().getClass());
}

//@public
function test_get() {
	var object = new BaseObject();
	assertEquals(undefined, object.get());
	assertEquals(undefined, object.get("hello"));
}

//@public
function test_set() {
	var object = new BaseObject();
	assertEquals(undefined, object.set());
	assertEquals(undefined, object.set("hello"));
	assertEquals(undefined, object.get("hello"));
	assertEquals("world", object.set("hello", "world"));
	assertEquals("world", object.get("hello"));
	assertEquals(undefined, object.set("hello"));
	assertEquals(undefined, object.get("hello"));
}
