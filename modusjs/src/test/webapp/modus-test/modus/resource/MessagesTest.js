/**
 * @test
 * @requires Messages hello
 */

//@public
function testMessage() {
	assertEquals("hello world !", $("hello.world"));
}
