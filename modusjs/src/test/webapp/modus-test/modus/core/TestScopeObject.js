/**
 * @class
 * @extends TestScopeBase
 */

//@public
function subProtected(name1) {
	return templateProtected(name1);
}

//@public
function subStatic(name1) {
	return templateStatic(name1);
}

//@protected
function getProtectedScope() {
	return "protectedScope";
}

//@static
function getStaticScope() {
	return "staticScope";
}
