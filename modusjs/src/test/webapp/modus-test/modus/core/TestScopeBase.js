/**
 * @class
 */

//@public
function baseProtected(name1) {
	return templateProtected(name1);
}

//@public
function baseStatic(name1) {
	return templateStatic(name1);
}

//@protected
function templateProtected(name1) {
	switch (name1) {
	case "protectedBase": return this.$protected.getProtectedBase();
	case "staticBase": return this.$static.getStaticBase();
	case "protectedScope": return this.$protected.getProtectedScope();
	case "staticScope": return this.$static.getStaticScope();
	}
}

//@static
function templateStatic(name1) {
	switch (name1) {
	case "protectedBase": return this.$protected.getProtectedBase();
	case "staticBase": return this.$static.getStaticBase();
	case "protectedScope": return this.$protected.getProtectedScope();
	case "staticScope": return this.$static.getStaticScope();
	}
}

//@protected
function getProtectedBase() {
	return "protectedBase";
}

//@static
function getStaticBase() {
	return "staticBase";
}

//@protected
function getProtectedScope() {
	return "protectedBase";
}

//@static
function getStaticScope() {
	return "staticBase";
}
