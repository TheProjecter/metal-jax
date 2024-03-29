/**
 * @controller HelloTable
 * @extends modus.widget.Table
 * @imports Table TableModel
 * @imports modus.core.System
 * 
 * @setting table.header.count=4
 * @setting table.header.0=Header 0
 * @setting table.header.1=Header 1
 * @setting table.header.2=Header 2
 * @setting table.header.3=Header 3
 */

//@private
function initObject(object) {
	TableModel.load("hello", System.callback.call(object, initTable));
}

//@private
function initTable() {
	var table = TableModel.find("hello");
	var headerRow = this.nodes.headerRow;
	var contentRows = this.nodes.contentRows.rows;
	var header = table.value[0];
	for (var i = 0; i < headerRow.cells.length; i++) {
		headerRow.cells[i].firstChild.innerHTML = header[i];
	}
	for (var i = 1; i < table.value.length; i++) {
		var row = contentRows[i-1];
		var value = table.value[i];
		for (var j = 0; j < row.cells.length; j++) {
			row.cells[j].innerHTML = value[j];
		}
	}
}
