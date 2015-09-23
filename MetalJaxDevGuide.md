<h1><b>metal-jax</b> Developer Guide</h1>

---



# Introduction #
**metal-jax** is a JavaScript OOP framework with a simple and intuitive programming model. The framework promotes class-based OOP practices in JavaScript such as encapsulation and inheritance, which result in more understandable and reusable code than other JavaScript frameworks. The framework provides a small set of base API and annotations to aid creation of modular JavaScript code, and even modular HTML code in a webapp.

# Getting Started #
Let's begin with an example to see how the framework works. The simple program below displays the universal tech greeting "Hello World". The two annotations in the program are self explanatory. The framework will load the _class_ `HelloWorld` and call its _static_ method `main`, which will render the greeting text in the web page.

<wiki:gadget url="http://metal-jax.appspot.com/metal.xml" up\_path="HelloWorld.jx" up\_contentType="text" width="850" height="135" border="0"/>

Let's assume `C:\work` is our work folder, and save the class file as `C:\work\HelloWorld.jx`, note the extension `.jx` as required by the framework.

Then create the HTML file to load the framework and the class, and save it as `C:\work\HelloWorld.html`.

<wiki:gadget url="http://metal-jax.appspot.com/metal.xml" up\_path="HelloWorld.html" up\_contentType="text" width="850" height="22" border="0"/>

Now open the HTML file in a browser. If everything is in place the browser will display the following message.

<wiki:gadget url="http://metal-jax.appspot.com/metal.xml" up\_path="HelloWorld-sd.html" width="850" height="60"/>

# Overview #
To use **metal-jax** in a web page, simply include a `script` tag in the HTML file, its `src` attribute specifies the boot URL with three parts identifying the following components:
  1. The framework startup file `boot.jx`, hosted either on a web server or in the local file system. Here we choose Google App Engine where the framework is already deployed.
  1. An optional startup module, the top level folder where a startup class could be found.
  1. An optional startup class, for custom startup behavior.

Putting it all together, the boot URL has the following general format:
```
http://metal-jax.appspot.com/metal-jax/boot.jx?module#class
```

As the HTML file is opened in a browser, the framework startup file will be loaded, as well as other framework files and webapp files.

The rest of the guide will go over the framework features, and demonstrate how to create modular and reusable JavaScript and HTML code.

# Modular JavaScript #
With **metal-jax**, JavaScript code is organized into classes, one per source file. Each class is a group of named members, either methods or variables, with controlled access via `private`, `protected`, or `public` scope modifiers, which provide better _encapsulation_. In addition, the framework supports declarative class-based _inheritance_ and import. Together they are the essential building blocks for a modular JavaScript program.

## Source Organization ##
Class sources are grouped into hierarchical packages, which are directories containing source files. Packages can be further grouped into modules, which are top level folders in a webapp. Source files are named with extension `.jx`. Module `metal-jax` and the top level package `metal` are reserved for the framework itself.

The framework derives source file location from module, package and class names.

## Class Members ##
Class defines members with JavaScript constructs and **metal-jax** annotations. The framework loads and evaluates classes in a JavaScript closure. All top level declared members or identifiers in a class are created in the closure scope instead of the global scope. Besides the declared members, the framework may inject additional members into the class based on its annotations. Some of the injected members are:
| _Member_ | _Comment_ |
|:---------|:----------|
| `getName()` | Class method to get class name. |
| `getSuperClass()` | Class method to get superclass. |
| `getClass()` | Instance method to get instance class. |

Unlike other JavaScript frameworks, **metal-jax** does not declare any named functions or variables in the global scope, hence free from any library compatibility issue.

## Class Annotations ##
Class annotations associate metadata to a class, such as class name and dependencies on other classes. The annotations are declared in a comment block at the beginning of a source file, delimited by "`/** ... */`". Some of the annotations are:
| _Annotation_ | _Comment_ |
|:-------------|:----------|
| `@class some.package.SomeClass` | Declares class name. |
| `@extends some.package.ParentClass` | Declares parent class to extend from. |
| `@imports some.package.OtherClass` | Declares and imports a dependent class. |

The framework loads and initializes class inheritance hierarchies and dependencies based on class annotations. Again the effect is entirely encapsulated inside each class. There is zero impact to the global scope.

## Member Annotations ##
Member annotations specify the visibility scope of class members. The annotations are declared in a comment line right before each top level member declaration, delimited by "`//`" . The annotations are:
| _Annotation_ |  _Comment_ |
|:-------------|:-----------|
| `@private`   | Declares members accessed only in the declaring class. |
| `@protected` | Declares members accessed only in inheritance hierarchy. |
| `@public`    | Declares members accessed through instance reference. |
| `@static`    | Declares members accessed through class reference. |

Let's create a second greeting program `HelloAgain` with these annotations.

<wiki:gadget url="http://metal-jax.appspot.com/metal.xml" up\_path="HelloAgain.jx" up\_contentType="text" width="850" height="190" border="0"/>

The annotations in this example are pretty self explanatory. Note however, since the two imported classes have the same base name, an alias `Hello` is specified in the second import. In method `main`, we call the static method of the first `HelloWorld`. Then we construct an instance of `demo.HelloWorld` with the aliased constructor and call the instance method. Let's save the file as `C:\work\HelloAgain.jx`.

Below is `demo.HelloWorld`, which differs only slightly from the first `HelloWorld`. Let's save the file as `C:\work\demo\HelloWorld.jx`, note the package folder.

<wiki:gadget url="http://metal-jax.appspot.com/metal.xml" up\_path="demo/HelloWorld.jx" up\_contentType="text" width="850" height="135" border="0"/>

Let's also create the HTML file to load `HelloAgain` and save it as `C:\work\HelloAgain.html`.

<wiki:gadget url="http://metal-jax.appspot.com/metal.xml" up\_path="HelloAgain.html" up\_contentType="text" width="850" height="22" border="0"/>

Now open the HTML file in a browser. The following messages should be displayed:

<wiki:gadget url="http://metal-jax.appspot.com/metal.xml" up\_path="HelloAgain-sd.html" width="850" height="105"/>

## Class Dependencies ##
The simple annotations "`@extends`" and "`@imports`" minimize effort to declare class inheritance and dependency in JavaScript. When one class extends another, the child class inherits all `public`/`static`/`protected` members from the parent class, unless there are child members with the same name. Overridden members from the parent class can still be accessed through a framework reserved variable `$super`, such as:
| _Overridden Member_ | _Example_ |
|:--------------------|:----------|
| `protected`         | `$super.$protected.someMember` |
| `public`            | `$super.$public.getClass()` |
| `static`            | `$super.$static.getName()` |

When one class imports another, only `public` and `static` members from the imported class are accessible.

## Framework Classes ##
The framework base API consists of the following classes:
| _Class_ | _Comment_ |
|:--------|:----------|
| `metal.jax.BaseObject` | Base class for all classes. |
| `metal.jax.core.Context` | Class to load other classes. |
| `metal.jax.resource.Face` | Base class for user interface elements. |
| `metal.jax.Model` | Base class for data elements. |
| `metal.jax.Resource` | Base class for resources. |
| `metal.jax.Service` | Base class for services. |
| `metal.jax.core.System` | Class with utility methods. |

# Modular HTML #
With **metal-jax**, HTML code of a webapp is organized into HTML fragments or faces, one per source file, each contains a group of well-formed HTML tags representing UI elements. HTML tags in a face file may have references to classes, other faces, style-sheets and images, which the framework interprets to inject and combine behavior and additional UI elements, and renders the eventual look and feel of the webapp. Face file serves as template to render HTML code, and does not contain inline script and style sheet constructs.

With this approach UI pieces can be built and verified easily and incrementally with a standalone browser, and are guaranteed to work when combined.

## Source Organization ##
Face files are grouped into similar modules and packages to those of classes. Face files are named with extension `.html`.

## Face Template ##
Face is a template and may have annotated elements with references to other face templates. These annotated elements are place holders which content will be replaced by that of the referenced face templates. When a page is loaded, the framework will load all the face templates and perform content substitution to render the page.

## Face Annotation ##
The annotated elements in a face template are normal HTML elements. The annotation is specified in the element `id` attribute in a colon-delimited format:
```
<div id="someId:some.package.SomeFace:some.package.SomeClass">...</div>
```

The annotation declares three components:
  1. Name of the face instance in the template.
  1. Name of the face template to load.
  1. Name of the face class to load, optional.

Time to work out an example to see how everything fits. Let's create a simple wiki page with four sections: header, footer, navigation and content. First the below page template and save as `C:\work\demo\WikiPage.html`.

<wiki:gadget url="http://metal-jax.appspot.com/metal.xml" up\_path="demo/WikiPage.html" up\_contentType="text" width="850" height="166" border="0"/>

The template is a normal HTML fragment with some very common elements. The only uncommon detail is the annotated `id` attributes, each identifies additional template to be loaded. Note the referenced template names are unqualified, the framework will assume they are in the same package as the referencing template.

Now open the template in a browser. The visual of the template looks like the following:

<wiki:gadget url="http://metal-jax.appspot.com/metal.xml" up\_path="demo/WikiPage.html" width="850" height="97"/>

Let's create the dependent templates, start with `demo.WikiBanner` and save as `C:\work\demo\WikiBanner.html`.

<wiki:gadget url="http://metal-jax.appspot.com/metal.xml" up\_path="demo/WikiBanner.html" up\_contentType="text" width="850" height="22" border="0"/>

Then `demo.WikiNavigation` and save as `C:\work\demo\WikiNavigation.html`.

<wiki:gadget url="http://metal-jax.appspot.com/metal.xml" up\_path="demo/WikiNavigation.html" up\_contentType="text" width="850" height="22" border="0"/>

And last, `demo.WikiContent` and save as `C:\work\demo\WikiContent.html`.

<wiki:gadget url="http://metal-jax.appspot.com/metal.xml" up\_path="demo/WikiContent.html" up\_contentType="text" width="850" height="166" border="0"/>

Finally create the HTML file to load `demo.WikiPage` and save it as `C:\work\WikiPage.html`.

<wiki:gadget url="http://metal-jax.appspot.com/metal.xml" up\_path="WikiPage.html" up\_contentType="text" width="850" height="22" border="0"/>

Now open the HTML file in a browser. The simple wiki page with all pieces together will look like the following:

<wiki:gadget url="http://metal-jax.appspot.com/metal.xml" up\_path="WikiPage-sd.html" width="850" height="220"/>

## Face Class ##
The third component declared in a face annotation references a face class. It binds event handling to the annotated element to process UI events.

# Modular Data #
Features:
  * model definition
  * model loading

# Setting Up Everything #
Features:
  * webapp structure
  * context config
  * same domain
  * cross domain