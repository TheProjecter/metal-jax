/**
 * @test
 * @imports JsonMapper
 * @imports TestAnnotatedObject
 * @imports TestBaseObject
 */

//@public
function testMarshal() {
	assertEquals("4", JsonMapper.marshal(4));
	assertEquals("2200000000", JsonMapper.marshal(2200000000));
	assertEquals("7.123", JsonMapper.marshal(7.123));
	assertEquals("false", JsonMapper.marshal(false));
	assertEquals("\"ABC\"", JsonMapper.marshal("ABC"));
	assertEquals("1388466000000", JsonMapper.marshal(new Date(1388466000000)));
	
	assertEquals("\"\"", JsonMapper.marshal(""));
	assertEquals("null", JsonMapper.marshal(null));
	assertEquals("null", JsonMapper.marshal(undefined));
	assertEquals("[]", JsonMapper.marshal([]));
	assertEquals("{}", JsonMapper.marshal({}));
}

//@public
function testMarshalGeneric() {
	assertEquals("4", JsonMapper.marshalGeneric(4));
	assertEquals("{\"long\":2200000000}", JsonMapper.marshalGeneric(2200000000));
	assertEquals("7.123", JsonMapper.marshalGeneric(7.123));
	assertEquals("true", JsonMapper.marshalGeneric(true));
	assertEquals("\"ABC\"", JsonMapper.marshalGeneric("ABC"));
	assertEquals("{\"date\":1388466000000}", JsonMapper.marshalGeneric(new Date(1388466000000)));
	
	assertEquals("\"\"", JsonMapper.marshalGeneric(""));
	assertEquals("null", JsonMapper.marshalGeneric(null));
	assertEquals("null", JsonMapper.marshalGeneric(undefined));
	assertEquals("[]", JsonMapper.marshalGeneric([]));
	assertEquals("{}", JsonMapper.marshalGeneric({}));
}

//@public
function testMarshalMap() {
	var actual = {
		name1: "value1",
		name2: "value2",
		name3: "value3"
	};
	var expected = "{\"name1\":\"value1\",\"name2\":\"value2\",\"name3\":\"value3\"}";
	assertEquals(expected, JsonMapper.marshal(actual));
	assertEquals(expected, JsonMapper.marshalGeneric(actual));
}

//@public
function testMarshalObject() {
	var actual = new TestBaseObject({
		name1: "value1",
		name2: "value2",
		name3: "value3"
	});
	
	var expected = "{\"name1\":\"value1\",\"name2\":\"value2\",\"name3\":\"value3\"}";
	assertEquals(expected, JsonMapper.marshal(actual));
	
	var expected2 = "{\"testBaseObject\":"+expected+"}";
	assertEquals(expected2, JsonMapper.marshalGeneric(actual));
}

//@public
function testMarshalAnnotatedObject() {
	var actual = new TestAnnotatedObject({
		name1: "value1",
		name2: "value2",
		name3: "value3"
	});
	
	var expected = "{\"name1\":\"value1\",\"name2\":\"value2\",\"name3\":\"value3\"}";
	assertEquals(expected, JsonMapper.marshal(actual));
	
	var expected2 = "{\"annotatedObject\":"+expected+"}";
	assertEquals(expected2, JsonMapper.marshalGeneric(actual));
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
		"{\"testBaseObject\":{" +
			"\"intValue\":4," +
			"\"longValue\":2200000000," +
			"\"doubleValue\":7.123," +
			"\"booleanValue\":true," +
			"\"intObject\":4," +
			"\"longObject\":2200000000," +
			"\"doubleObject\":7.123," +
			"\"booleanObject\":true," +
			"\"stringObject\":\"ABC\"," +
			"\"dateObject\":1388466000000," +
			"\"defaultObject\":\"\"" +
		"}}";
	assertEquals(expected, JsonMapper.marshalGeneric(actual));
}
