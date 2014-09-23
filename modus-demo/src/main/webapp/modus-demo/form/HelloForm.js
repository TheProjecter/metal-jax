/**
 * @controller
 * @extends modus.widget.Form
 * @imports service.Echo
 * @imports modus.core.System
 */

//@private
function initObject(form) {
	form.echo = new Echo(form);
	form.echo.bind("hello", update);
	form.echo.bind("error", error);
}

//@static
function initNode(view, node) {
	form.bindEvent("click", submit, form.nodes["submit"]);
}

//@private
function submit() {
	this.nodes.result.innerHTML = "submitting...";
	this.echo.hello(this);
}

//@private
function update(model) {
	this.nodes.result.innerHTML = "updated";
	for (var name in this.inputs) {
		this.set(name, model.get(name));
	}
}

//@private
function error(messages) {
	var message = "<ul>";
	for (var i = 0; i < messages.length; i++) {
		message = message.concat("<li>", System.formatText("Code: ${code}; Detail: ${detail}", messages[i]), "</li>");
	}
	message = message.concat("</ul>");
	this.nodes.result.innerHTML = "error: " + message;
}
