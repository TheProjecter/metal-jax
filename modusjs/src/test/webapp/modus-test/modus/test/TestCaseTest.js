/**
 * @test
 */

var token = null;

//@public
function setUp() {
	token = "secret";
	println("--------setUp: ".concat(token));
}

//@public
function tearDown() {
	println("--------tearDown: ".concat(token));
}

//@public
function testAssertEquals() {
	assertEquals("hello", "hello", token);
}

//@public
function testAssertEqualsFail() {
	assertEquals("hello", "world", token);
}
