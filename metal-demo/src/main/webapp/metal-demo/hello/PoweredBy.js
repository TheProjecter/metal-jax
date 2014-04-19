/**
 * @controller PoweredBy
 * @imports metal.jax.core.System
 * 
 * @setting label.value=Powered by MetalJax
 */

//@private
function initObject(object) {
	object.nodes.label.style.color = "red";
	setInterval(System.callback.call(object, changeLabel), 1000);
}

//@private
function changeLabel() {
	if (this.nodes.label.style.color == "red") {
		this.nodes.label.style.color = "blue";
	} else {
		this.nodes.label.style.color = "red";
	}
}
