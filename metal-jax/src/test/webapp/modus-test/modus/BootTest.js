/**
 * @test
 */

//@public
function test_bootSetting() {
	var names = [
		"baseScheme",
		"baseURL",
		"baseOrigin",
		"baseModule",
		"baseContext",
		"baseResource",
		"bootURL",
		"bootOrigin",
		"bootModule",
		"bootContext",
		"crossDomain",
		"requestTime",
		"requestTimeout",
		"classType",
		"viewType"
	];
	for (var i = 0; i < names.length; i++) {
		println("| ".concat(names[i], ": ", $(names[i])));
	}
}
