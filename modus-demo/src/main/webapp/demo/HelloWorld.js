/**
 * @class demo.HelloWorld
 */

//@public
function say(message) {
    document.body.appendChild(document.createElement("h2")).innerHTML = message;
}