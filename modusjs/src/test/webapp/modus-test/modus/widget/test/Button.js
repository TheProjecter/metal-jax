/**
 * @controller
 */

//@private
function initObject(object, node) {
	object.nodes.button;
	object.nodes.label.innerHTML += " - Button.js";
	object.bindEvent("click", hello, object.nodes.button);
}

//@private
function hello() {
	alert(this.nodes.label.innerHTML);
}
