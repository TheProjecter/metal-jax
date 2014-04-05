/**
 * @controller
 * @requires metal.jax.script.Angular
 * @requires metal.jax.stylesheet.Angular AngularStyleSheet
 */

//@private
var $scope = null;

//@private
function initResource(scope) {
	Angular.addModule('todo');
	Angular.addController('TodoCtrl', "todo", TodoCtrl);
}

//@private
function TodoCtrl($scope) {
	$private.$scope = $scope;
	$scope.todos = [ {text:'learn angular', done:true}, {text:'build an angular app', done:false}];
	$scope.todoText = "";
	$scope.addTodo = addTodo;
	$scope.remaining = remaining;
	$scope.archive = archive;
}

//@private
function addTodo() {
	$scope.todos.push({ text: $scope.todoText, done: false });
	$scope.todoText = '';
}

//@private
function remaining() {
	var count = 0;
	angular.forEach($scope.todos, function(todo) {
		count += todo.done ? 0 : 1;
	});
	return count;
}

//@private
function archive() {
	var oldTodos = $scope.todos;
	$scope.todos = [];
	angular.forEach(oldTodos, function(todo) {
		if (!todo.done) $scope.todos.push(todo);
	});
}
