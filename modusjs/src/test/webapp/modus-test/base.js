/**
 * @context
 * @imports modus.test.TestSuite
 * @source base=${baseURL} module=${baseModule} includes=[.]test[.]
 * @source base=${baseURL} module=${baseModule} includes=[.]Test
 * @source base=${baseURL} module=${baseModule} includes=Test$
 * @stereotype type=test base=modus.test.TestCase
 */

//@private
function initObject(object) {
	$context.testSuite = new TestSuite();
}

//@private
function initContext() {
	$context.testSuite.run($context);
}
