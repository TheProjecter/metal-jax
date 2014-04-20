/**
 * @service Echo
 * @imports model.Hello
 * 
 * @setting service.path=/metal/demo/Echo
 */

//@public
function hello(model) {
	callService(this, "hello", {model: new Hello(model)}, Hello);
}