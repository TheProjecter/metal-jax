/**
 * @controller
 * @imports modus.face.Node
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@private
var _panelStyles_ = [ "top", "bottom" ];

//@static
function initView(view) {
	view.setting.style = Node.filterStyle(view.node, _panelStyles_, true);
}

//@static
function initModel(view) {
	view.tabLabel = view.nodes.head.removeChild(Node.getChildByIndex(view.nodes.head));
	view.nodes["head"].innerHTML = "";
}

//@static
function initNode(view, node) {
	switch (node.id) {
	case "head":
	case "body":
		var style = Node.filterStyle(node, _panelStyles_, true);
		Node.toggleStyle(node, style, view.setting.style);
		break;
	}
}

//@static
function initPlaceholder(placeholder, node) {
	node.parentNode.id = node.id;
	var selected = Node.filterStyle(node, ["selected"]);
	var tabLabel = placeholder.view.tabLabel.cloneNode(false);
	tabLabel.id = node.id;
	tabLabel.title = node.title || node.id;
	tabLabel.innerHTML = node.title || node.id;
	placeholder.view.nodes.head.appendChild(tabLabel);
	initTabLabel(placeholder.view, tabLabel);
	if (selected) {
		toggleTabSelection(placeholder.view, tabLabel);
	}
}

//@private
function initTabLabel(view, tabLabel) {
	view.bindEvent("mouseover", toggleHighlight, tabLabel);
	view.bindEvent("mouseout", toggleHighlight, tabLabel);
	view.bindEvent("click", toggleTabSelection, tabLabel);
}

//@private
function toggleHighlight(view, tabLabel) {
	Node.toggleStyle(tabLabel, "highlight");
	return true;
}

//@private
function toggleTabSelection(view, tabLabel) {
	if (view.selectedLabel != tabLabel) {
		if (view.selectedLabel) {
			Node.toggleStyle(view.selectedLabel, "selected");
			Node.toggleStyle(getTabContent(view, view.selectedLabel), "selected");
		}
		Node.toggleStyle(tabLabel, "selected");
		Node.toggleStyle(getTabContent(view, tabLabel), "selected");
		view.selectedLabel = tabLabel;
	}
}

//@private
function getTabContent(view, tabLabel) {
	var index = Node.indexOfChild(view.nodes.head, tabLabel);
	return Node.getChildByIndex(view.nodes.body, index);
}
