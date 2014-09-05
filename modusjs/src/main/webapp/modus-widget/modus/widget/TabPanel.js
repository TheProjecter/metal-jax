/**
 * @controller
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@private
var _panelStyles_ = [ "top", "bottom" ];

//@public
function initView(view) {
	view.setting.style = view.filterStyle(view.node, _panelStyles_, true);
}

//@public
function initModel(view) {
	view.tabLabel = view.nodes.head.removeChild(view.getChildByIndex(view.nodes.head));
	view.clear("head");
}

//@public
function initNode(view, node) {
	switch (node.id) {
	case "head":
	case "body":
		var style = view.filterStyle(node, _panelStyles_, true);
		view.toggleStyle(node, style, view.setting.style);
		break;
	}
}

//@public
function initPlaceholder(view, placeholder, part) {
	part.parentNode.id = part.id;
	var selected = view.filterStyle(part, ["selected"]);
	var tabLabel = view.tabLabel.cloneNode(false);
	tabLabel.id = part.id;
	tabLabel.title = part.title || part.id;
	tabLabel.innerHTML = part.title || part.id;
	view.nodes.head.appendChild(tabLabel);
	initTabLabel(view, tabLabel);
	if (selected) {
		toggleTabSelection(view, tabLabel);
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
	view.toggleStyle(tabLabel, "highlight");
	return true;
}

//@private
function toggleTabSelection(view, tabLabel) {
	if (view.selectedLabel != tabLabel) {
		if (view.selectedLabel) {
			view.toggleStyle(view.selectedLabel, "selected");
			view.toggleStyle(getTabContent(view, view.selectedLabel), "selected");
		}
		view.toggleStyle(tabLabel, "selected");
		view.toggleStyle(getTabContent(view, tabLabel), "selected");
		view.selectedLabel = tabLabel;
	}
}

//@private
function getTabContent(view, tabLabel) {
	var index = view.indexOfChild(view.nodes.head, tabLabel);
	return view.getChildByIndex(view.nodes.body, index);
}
