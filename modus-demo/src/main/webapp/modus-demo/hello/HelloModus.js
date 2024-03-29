/**
 * @controller
 * @imports PoweredBy
 * @imports modus.core.System
 * 
 * @setting label.title=Hello, Modus
 * @setting label.value=Hello, Modus
 * @setting poweredBy.title=Powered By ModusJS
 */

//@private
var $fontSizes = [ "10px", "13px", "16px", "18px", "24px", "32px" ];

//@private
function initObject(object) {
	object.next = 0;
	object.flag = false;
	object.nodes.label.style.fontSize = $fontSizes[object.next++];
	setInterval(System.callback.call(object, changeLabel), 200);
}

//@private
function changeLabel() {
	this.nodes.label.style.fontSize = $fontSizes[this.next];
	if (!this.flag && this.next < $fontSizes.length-1) this.next++;
	else if (this.flag && this.next > 0) this.next--;
	else this.flag = !this.flag;
}
