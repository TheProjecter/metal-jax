/**
 * @test
 * @imports XmlMapper
 * @imports TestAnnotatedObject
 * @imports TestBaseObject
 */

//@public
function testMarshal() {
	assertEquals("4", XmlMapper.marshal(4));
	assertEquals("2200000000", XmlMapper.marshal(2200000000));
	assertEquals("7.123", XmlMapper.marshal(7.123));
	assertEquals("false", XmlMapper.marshal(false));
	assertEquals("ABC", XmlMapper.marshal("ABC"));
	assertEquals("2013-12-31T05:00:00.000Z", XmlMapper.marshal(new Date(1388466000000)));
	
	assertEquals("", XmlMapper.marshal(""));
	assertEquals("", XmlMapper.marshal(null));
	assertEquals("", XmlMapper.marshal(undefined));
	assertEquals("", XmlMapper.marshal([]));
	assertEquals("", XmlMapper.marshal({}));
}

//@public
function testMarshalGeneric() {
	assertEquals("<int>4</int>", XmlMapper.marshalGeneric(4));
	assertEquals("<long>2200000000</long>", XmlMapper.marshalGeneric(2200000000));
	assertEquals("<double>7.123</double>", XmlMapper.marshalGeneric(7.123));
	assertEquals("<boolean>true</boolean>", XmlMapper.marshalGeneric(true));
	assertEquals("<string>ABC</string>", XmlMapper.marshalGeneric("ABC"));
	assertEquals("<date>2013-12-31T05:00:00.000Z</date>", XmlMapper.marshalGeneric(new Date(1388466000000)));
	
	assertEquals("<string/>", XmlMapper.marshalGeneric(""));
	assertEquals("<null/>", XmlMapper.marshalGeneric(null));
	assertEquals("<null/>", XmlMapper.marshalGeneric(undefined));
	assertEquals("<list/>", XmlMapper.marshalGeneric([]));
	assertEquals("<map/>", XmlMapper.marshalGeneric({}));
}

//@public
function testMarshalMap() {
	var actual = {
		name1: "value1",
		name2: "value2",
		name3: "value3"
	};
	var expected = "<name1><string>value1</string></name1><name2><string>value2</string></name2><name3><string>value3</string></name3>";
	assertEquals(expected, XmlMapper.marshal(actual));
	
	var expected2 = "<map>"+expected+"</map>";
	assertEquals(expected2, XmlMapper.marshalGeneric(actual));
}

//@public
function testMarshalObject() {
	var actual = new TestBaseObject({
		name1: "value1",
		name2: "value2",
		name3: "value3"
	});
	
	var expected = "<name1>value1</name1><name2>value2</name2><name3>value3</name3>";
	assertEquals(expected, XmlMapper.marshal(actual));
	
	var expected2 = "<testBaseObject>"+expected+"</testBaseObject>";
	assertEquals(expected2, XmlMapper.marshalGeneric(actual));
}

//@public
function testMarshalAnnotatedObject() {
	var actual = new TestAnnotatedObject({
		name1: "value1",
		name2: "value2",
		name3: "value3"
	});
	
	var expected = "<name1>value1</name1><name2>value2</name2><name3>value3</name3>";
	assertEquals(expected, XmlMapper.marshal(actual));
	
	var expected2 = "<annotatedObject>"+expected+"</annotatedObject>";
	assertEquals(expected2, XmlMapper.marshalGeneric(actual));
}

//@public
function testBaseObject() {
	var actual = new TestBaseObject({
		intValue:		4,
		longValue:		2200000000,
		doubleValue:	7.123,
		booleanValue:	true,
		intObject:		4,
		longObject:		2200000000,
		doubleObject:	7.123,
		booleanObject:	true,
		stringObject:	"ABC",
		dateObject:		new Date(1388466000000),
		defaultObject:	""
	});
	var expected =
		"<testBaseObject>" +
			"<intValue>4</intValue>" +
			"<longValue>2200000000</longValue>" +
			"<doubleValue>7.123</doubleValue>" +
			"<booleanValue>true</booleanValue>" +
			"<intObject>4</intObject>" +
			"<longObject>2200000000</longObject>" +
			"<doubleObject>7.123</doubleObject>" +
			"<booleanObject>true</booleanObject>" +
			"<stringObject>ABC</stringObject>" +
			"<dateObject>2013-12-31T05:00:00.000Z</dateObject>" +
			"<defaultObject></defaultObject>" +
		"</testBaseObject>";
	assertEquals(expected, XmlMapper.marshalGeneric(actual));
}
