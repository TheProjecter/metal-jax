/**
 * @controller
 * @imports modus.face.View
 * 
 * @requires modus.script.Angular
 * @requires modus.stylesheet.Angular AngularStyleSheet
 */

//@private
var $scope = null;

//@private
function initResource(scope) {
	Angular.addModule('todo');
	Angular.addController('TodoCtrl', "todo", TodoCtrl);
}

//@public
function initScope(view) {
	TodoCtrl(view.$);
}

//@private
function TodoCtrl($scope) {
	$private.$scope = $scope;
	$scope.addTodo = $public.addTodo;
	$scope.remaining = $public.remaining;
	$scope.archive = $public.archive;
}

//@public
function addTodo() {
	$scope.todos.push({ text: $scope.todoText, done: false });
	$scope.todoText = '';
	return true;
}

//@public
function remaining() {
	var count = 0;
	angular.forEach($scope.todos, function(todo) {
		count += todo.done ? 0 : 1;
	});
	return count;
}

//@public
function archive() {
	var oldTodos = $scope.todos;
	$scope.todos = [];
	angular.forEach(oldTodos, function(todo) {
		if (!todo.done) $scope.todos.push(todo);
	});
	return true;
}
