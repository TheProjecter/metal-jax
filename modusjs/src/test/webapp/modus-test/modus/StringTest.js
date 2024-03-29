/**
 * @test
 */

//@private
var count = 100000;

//@private
var value = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";

//@private
var result0 = "";

//@public
function test_addition() {
	println("count: " + count);
	var start = new Date().getTime();
	for (var i = 0; i < count; i++) {
		result0 = result0 + value;
	}
	println("time: " + (new Date().getTime() - start));
}

//@public
function test_concatOne() {
	var result = "";
	var start = new Date().getTime();
	for (var i = 0; i < count; i++) {
		result = result.concat(value);
	}
	println("time: " + (new Date().getTime() - start));
	assertEquals(result0, result);
}
