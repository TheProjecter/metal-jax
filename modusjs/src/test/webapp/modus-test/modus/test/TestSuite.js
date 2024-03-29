/**
 * @class
 * @imports TestCase
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@public
function run(context) {
	var runner = this, testName = $("baseResource");
	if (!testName) {
		runAll.call(runner, context);
	} else {
		context.loadClass(testName, function() {
			var testClass = context.findClass(testName);
			if (testClass && testClass.extendsClass(TestCase)) {
				runner.runTest(new testClass(context));
			} else {
				var test = new testClass(context);
				runAll.call(runner, context);
			}
		});
	}
}

//@private
function runAll(context) {
	for (var name in context.$) {
		this.runTest(context.$[name]);
	}
}

//@public
function runTest(test) {
	if (test instanceof TestCase) {
		test.run();
	}
}
