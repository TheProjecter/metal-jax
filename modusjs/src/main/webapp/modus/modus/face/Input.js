/**
 * @class
 * @imports Bean
 * @imports Node
 * 
 * @copyright Jay Tang 2014. All rights reserved.
 */

//@private
var _inputTypes_ = {
	"text": { event: "change", status: false },
	"textarea": { event: "change", status: false },
	"password": { event: "change", status: false },
	"checkbox": { event: "click", status: false },
	"radio": { event: "change", status: false },
	"submit": { event: "change", status: false }
};

//@static
function scanInput(bean, node) {
	if (node.type in _inputTypes_) {
		newInput(bean, node, _inputTypes_[node.type]);
	}
}

//@private
function newInput(bean, node, type) {
	if (node.name) {
		var input = { bean:bean, node:node, view:bean.view, name:node.name, type:node.type, event:type.event, status:type.status };
		bean.inputs.push(input);
		return input;
	}
}

//@static
function normalizeInput(index, input, model) {
	valueOf(input, format(input, model));
}

//@private
function format(input, model) {
	var result = null;
	if (input.name in model) {
		result = model[input.name];
	} else if (input.view.isSet(input.name)) {
		result = input.view.get(input.name);
	}
	return result;
}

//@static
function bindInput(index, input) {
	if (input.event) {
		input.callback = input.view.bindEvent(input.event, dispatch, input.node, input);
	}
}

//@static
function unbindInput(index, input) {
	if (input.callback) {
		Node.toggleEvent(input.event, input.callback, false, input.node);
		delete input.callback;
	}
}

//@private
function dispatch(view, node, event, input) {
	var value = valueOf(input);
	var model = Bean.getModel(input.bean, input.name);
	if (model && model[input.name] != value) {
		model[input.name] = value;
		Bean.notifyBean(input.bean);
	}
}

//@private
function valueOf(input, value) {
	var old = null, set = arguments.length == 2;
	switch (input.type) {
	case "checkbox":
		old = input.node.checked;
		if (set) input.node.checked = value;
		break;
	case "text":
	case "textarea":
	case "password":
		old = input.node.value;
		if (set) input.node.value = value;
		break;
	case "submit":
		old = input.node.value;
		break;
	default:
		log("warn", "Input type '${0}' not supported yet", input.type);
		break;
	}
	return old;
}
