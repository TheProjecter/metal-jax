/**
 * @class HelloAgain
 * @imports HelloWorld
 * @imports demo.HelloWorld Hello
 */

//@static
function main() {
    HelloWorld.main();
    new Hello().say("Hello Again!");
}