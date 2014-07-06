/**
 * @controller
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@private
var _panelStyles_ = [ "tabTop", "tabBottom" ];

//@private
var _partStyles_ = [ "tabLabel", "tabContent" ];

//@public
function init(view) {
	view.setting.style = view.filterSetting(view.setting, _panelStyles_, true);
}

//@public
function afterInit(view) {
	view.toggleStyle(view.node, view.setting.style, "tabPanel");
	view.tabLabel = view.nodes.tabHead.removeChild(view.getChildByIndex(view.nodes.tabHead));
	view.clear("tabHead");
	if (view.setting.style == "tabBottom") {
		view.nodes.tabHead.parentNode.insertBefore(view.nodes.tabBody, view.nodes.tabHead);
	}
}

//@public
function bind(view, node) {
	switch (node.id) {
	case "tabHead":
	case "tabBody":
		var style = view.filterStyle(node, _panelStyles_, true);
		view.toggleStyle(node, style, view.setting.style);
		break;
	}
}

//@public
function initPart(view, placeholder, part) {
	part.parentNode.id = part.id;
	var selected = view.filterStyle(part, ["selected"]);
	var tabLabel = view.tabLabel.cloneNode(false);
	tabLabel.id = part.id;
	tabLabel.title = part.title || part.id;
	tabLabel.innerHTML = part.title || part.id;
	view.nodes.tabHead.appendChild(tabLabel);
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
	var index = view.indexOfChild(view.nodes.tabHead, tabLabel);
	return view.getChildByIndex(view.nodes.tabBody, index);
}
