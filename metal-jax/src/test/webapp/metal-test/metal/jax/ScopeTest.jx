/**
 * @test
 * @imports TestScopeBase
 * @imports TestScopeObject
 */

//@public
function test_BasefromBase() {
	assertEquals("protectedBase", new TestScopeBase().baseProtected("protectedBase"));
	assertEquals("staticBase", new TestScopeBase().baseProtected("staticBase"));
	assertEquals("protectedBase", new TestScopeBase().baseProtected("protectedScope"));
	assertEquals("staticBase", new TestScopeBase().baseProtected("staticScope"));
	
	assertEquals("protectedBase", new TestScopeBase().baseStatic("protectedBase"));
	assertEquals("staticBase", new TestScopeBase().baseStatic("staticBase"));
	assertEquals("protectedBase", new TestScopeBase().baseStatic("protectedScope"));
	assertEquals("staticBase", new TestScopeBase().baseStatic("staticScope"));
}

//@public
function test_SubfromBase() {
	assertEquals("protectedBase", new TestScopeObject().baseProtected("protectedBase"));
	assertEquals("staticBase", new TestScopeObject().baseProtected("staticBase"));
	assertEquals("protectedBase", new TestScopeObject().baseProtected("protectedScope"));
	assertEquals("staticBase", new TestScopeObject().baseProtected("staticScope"));
	
	assertEquals("protectedBase", new TestScopeObject().baseStatic("protectedBase"));
	assertEquals("staticBase", new TestScopeObject().baseStatic("staticBase"));
	assertEquals("protectedBase", new TestScopeObject().baseStatic("protectedScope"));
	assertEquals("staticBase", new TestScopeObject().baseStatic("staticScope"));
}

//@public
function test_SubfromSub() {
	assertEquals("protectedBase", new TestScopeObject().subProtected("protectedBase"));
	assertEquals("staticBase", new TestScopeObject().subProtected("staticBase"));
	assertEquals("protectedScope", new TestScopeObject().subProtected("protectedScope"));
	assertEquals("staticScope", new TestScopeObject().subProtected("staticScope"));
	
	assertEquals("protectedBase", new TestScopeObject().subStatic("protectedBase"));
	assertEquals("staticBase", new TestScopeObject().subStatic("staticBase"));
	assertEquals("protectedScope", new TestScopeObject().subStatic("protectedScope"));
	assertEquals("staticScope", new TestScopeObject().subStatic("staticScope"));
}
