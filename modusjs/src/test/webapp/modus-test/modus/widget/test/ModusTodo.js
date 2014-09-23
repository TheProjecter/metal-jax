/**
 * @controller
 * @requires modus.stylesheet.Angular
 */

//@public
function addTodo() {
	this.view.get("todos").push({ text: this.view.get("todoText"), done: false });
	this.view.set("todoText", "");
	return true;
}

//@public
function remaining() {
	var count = 0;
	forEach(this.view.get("todos"), function(index, todo) {
		count += todo.done ? 0 : 1;
	});
	return count;
}

//@public
function archive() {
	var todos = [];
	forEach(this.view.get("todos"), function(index, todo) {
		if (!todo.done) todos.push(todo);
	});
	if (this.view.get("todos").length != todos.length) {
		this.view.set("todos", todos);
		return true;
	}
}
