/**
 * @controller
 * @imports modus.core.System
 * 
 * @copyright Jay Tang 2012. All rights reserved.
 */

//@private
function initClass(scope) {
	scope._selected_ = $("style.selected", "selected");
	scope._sortable_ = $("style.sortable", "sortable");
	scope._sorted_ascending_ = $("style.sorted_ascending", "sorted_ascending");
	scope._sorted_descending_ = $("style.sorted_descending", "sorted_descending");
}

//@private
function initObject(object) {
	object.scroll = object.bindEvent(null, scroll);
	object.compare = System.callback.call(object, compare);
	object.rows = System.copyArray(object.nodes["contentRows"].rows);
	for (var i = 0; i < object.rows.length; i++) {
		object.bindEvent("click", toggleRow, object.rows[i]);
	}
	for (var i = 0; i < object.nodes["headerRow"].cells.length; i++) {
		var headerCell = object.nodes["headerRow"].cells[i];
		if (headerCell.className) {
			headerCell.firstChild.className = _sortable_;
			object.bindEvent("click", sortColumn, headerCell);
		}
	}
}

//@private
function sortColumn(headerCell) {
	if (this.selectedCell == headerCell) {
		this.sortOrder = 1 - this.sortOrder;
	} else {
		if (this.selectedCell) {
			this.selectedCell.firstChild.className = _sortable_;
		}
		this.selectedCell = headerCell;
		this.sortOrder = 0;
	}
	switch (this.sortOrder) {
	case 0:
		this.rows.sort(this.compare);
		this.selectedCell.firstChild.className = _sorted_ascending_;
		break;
	case 1:
		this.rows.reverse();
		this.selectedCell.firstChild.className = _sorted_descending_;
		break;
	}
	for (var i = 0; i < this.rows.length; i++) {
		this.nodes["contentRows"].appendChild(this.rows[i]);
	}
}

//@private
function toggleRow(row) {
	if (this.selectedRow == row) {
		this.selectedRow.className = "";
		delete this.selectedRow;
		toggleEvent("keydown", this.scroll, false);
	} else {
		if (this.selectedRow) {
			this.selectedRow.className = "";
		} else {
			toggleEvent("keydown", this.scroll);
		}
		this.selectedRow = row;
		this.selectedRow.className = _selected_;
	}
}

//@private
function scroll(node, event) {
	var keyCode = event.keyCode;
	if (keyCode == 38 || keyCode == 40) {
		var index = this.selectedRow.sectionRowIndex;
		index = index + ((keyCode == 38) ? -1 : 1);
		if (index < 0) index = this.rows.length-1;
		else if (index == this.rows.length) index = 0;
		toggleRow.call(this, this.nodes["contentRows"].rows[index]);
	} else {
		return true;
	}
}

//@private
function compare(row1, row2) {
	var value1 = getCellValue(row1.cells[this.selectedCell.cellIndex], this.selectedCell);
	var value2 = getCellValue(row2.cells[this.selectedCell.cellIndex], this.selectedCell);
	if (value1 < value2) return -1;
	if (value1 > value2) return 1;
	return 0;
}

//@private
function getCellValue(cell, headerCell) {
	return System.parseValue(cell.abbr || cell.innerHTML, headerCell.className);
}
