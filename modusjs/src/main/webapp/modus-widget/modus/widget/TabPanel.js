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
	view.toggleStyle(view.node, view.setting.style, "tabPanel");
}

//@public
function afterInit(view) {
	if (view.setting.style == "tabBottom") {
		view.nodes.tabHead.parentNode.insertBefore(view.nodes.tabBody, view.nodes.tabHead);
	}
}

//@public
function bind(view, node) {
	var style;
	switch (node.id) {
	case "tabHead":
	case "tabBody":
		style = view.filterStyle(node, _panelStyles_, true);
		view.toggleStyle(node, style, view.setting.style);
		return;
	}
	
	style = view.filterStyle(node, _partStyles_);
	switch (style) {
	case "tabLabel":
		if (!view.tabLabel) {
			view.tabLabel = node;
			view.toggleStyle(node, "selected");
		}
		break;
	case "tabContent":
		if (!view.tabContent) {
			view.tabContent = node;
			view.toggleStyle(node, "selected");
		}
		break;
	}
}

//@public
function refresh(view, content) {
	view.clear("tabHead");
	view.clear("tabBody");
	for (var name in content.nodes) {
		initContentNode(view, content.nodes[name], name);
	}
	if (view.setting["select"]) {
		toggleTabSelection(view, view.getChildById(view.nodes.tabHead, view.setting["select"]));
	}
	if (!view.selectedLabel) {
		toggleTabSelection(view, view.getChildByIndex(view.nodes.tabHead, 0));
	}
}

//@protected
function initContentNode(view, node, nodeId) {
	var tabLabel = view.tabLabel.cloneNode(false);
	tabLabel.id = node.id;
	tabLabel.innerHTML = node.title || node.id;
	view.nodes.tabHead.appendChild(tabLabel);
	initTabLabel(view, tabLabel);
	
	var tabContent = view.tabContent.cloneNode(false);
	tabContent.id = node.id;
	tabContent.appendChild(node);
	view.nodes.tabBody.appendChild(tabContent);
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
