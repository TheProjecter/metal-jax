/**
 * @copyright Jay Tang 2012. All rights reserved.
 */
(function() {
	
	(function() {
		
		var $setting = {
			baseModule: ".",
			baseContext: ".base",
			baseClass: "",
			baseResource: "",
			crossDomain: "xd",
			requestTime: "100",
			requestTimeout: "3000",
			classType: "js",
			viewType: "html"
		};
		var $source = {
			stereotype: "context",
			name: "modus.core.Context",
			baseName: "Context",
			scope: { $setting:$setting, $imports:{}, $requires:{}, $static:{}, $protected:{}, $public:{} },
			classes: {},
			setting: {
				source: [
					{ base:"${bootURL}", module:"modus-widget", includes:"^modus[.]widget[.]" },
					{ base:"${bootURL}", module:"${bootModule}", includes:"^modus[.]" }
				],
				stereotype: [
					{ type:"class", base:"modus.core.BaseObject" },
					{ type:"context", base:"modus.core.Context" },
					{ type:"resource", base:"modus.core.Resource" },
					{ type:"model", base:"modus.core.Model" },
					{ type:"service", base:"modus.core.Service" },
					{ type:"view", base:"modus.face.View" },
					{ type:"controller", base:"modus.face.Controller" },
					{ type:"script", base:"modus.resource.Script" },
					{ type:"stylesheet", base:"modus.resource.StyleSheet" }
				]
			}
		};
		var _URL_ = /(?:([^\/:]+:\/\/[^\/]*)?([^?&#]+)?)(?:\?([^&#]+)?(?:&([^#]+))?)?(?:#(.+))?/;
		
		function main() {
			parseSetting($setting);
			if (!this[$setting["bootModule"]]) {
				this[$setting["bootModule"]] = {};
				callLater(loadSource, "load");
			}
		}
		
		function parseSetting(setting) {
			var parts = _URL_.exec(document.URLUnencoded||document.URL);
			setting["baseOrigin"] = parts[1];
			setting["baseScheme"] = parts[1].split(":")[0];
			setting["baseURL"] = splitPath(parts[1] + parts[2], -1, -1);
			var module = parts[3]||"", query = parts[4]||"", base = parts[5]||"";
			var nodes = document.getElementsByTagName("script");
			parts = _URL_.exec(resolvePath(nodes[nodes.length-1].src, setting["baseURL"] + "/"));
			setting["bootOrigin"] = parts[1];
			setting["bootURL"] = splitPath(parts[1] + parts[2], -2, -2);
			setting["bootModule"] = splitPath(parts[2], -2, 1);
			setting["bootContext"] = splitPath(parts[2], -1, 1).split(".")[0];
			if (parts[3] || parts[4] || parts[5]) {
				module = parts[3]||module; query = parts[4]||query; base = parts[5]||base;
			}
			parts = base.split(":");
			setting["baseResource"] = parts[0] || setting["baseResource"];
			setting["baseClass"] = parts.length > 1 ? parts[1]||setting["baseResource"] : setting["baseClass"];
			parts = module.split(":");
			setting["baseModule"] = parts[0] || setting["baseModule"];
			setting["baseContext"] = module ? parts[1]||setting["baseContext"] : setting["baseResource"]||setting["baseContext"];
			parts = query.split("&");
			for (var i = 0; i < parts.length; i++) {
				var part = parts[i].split("=");
				setting[part[0]] = setting[part[0]] || part[1];
			}
		}
		
		function loadSource() {
			$source.base = $setting["bootURL"];
			$source.module = $setting["bootModule"];
			$source.resourceType = $setting["classType"];
			$source.path = $source.name.split(".").join("/").concat(".", $source.resourceType);
			loadback($source, parseSource);
		}
		
		function parseSource(source) {
			if (source.content) {
				source.scope.$private = source.scope;
				$.call(source);
				source.scope.compile = $;
				source.scope.$source = source;
				source.scope.loadback = loadback;
				source.scope.$static.splitPath = splitPath;
				source.scope.$static.resolvePath = resolvePath;
				source.scope.$static.isSameDomain = isSameDomain;
				source.scope.initClass_(source.scope);
			} else {
				throw Error("failed to load URL: " + source.url + ", error: " + source.error);
			}
		}
		
		function callLater(callback, event) {
			if (this.addEventListener) {
				this.addEventListener(event, callback, false);
			} else if (this.attachEvent) {
				this.attachEvent("on"+event, callback);
			}
		}
		
		function isSameDomain(url) {
			return $setting["baseOrigin"] == _URL_.exec(url)[1];
		}
		
		function loadback(source, callback) {
			var action = isSameDomain(source.base) ? loadSameDomain : loadCrossDomain;
			action.call(this, source, callback);
		}
		
		function loadSameDomain(source, callback) {
			var caller = this;
			var request = requestSameDomain(source, function() {
				if (!request || request.readyState != 4) return;
				if (request.status == 200 || request.status == 0) {
					source.content = request.responseText;
				} else {
					source.status = request.status;
					source.error = request.statusText;
				}
				callback.call(caller, source);
			});
			try {
				request.send(null);
			} catch (ex) {
				throw Error("failed to load URL: " + source.url + ", error: " + ex.message);
			}
		}
		
		function requestSameDomain(source, callback) {
			var request;
			if (typeof XMLHttpRequest != "undefined" && typeof ActiveXObject == "undefined") {
				request = new XMLHttpRequest();
			} else if (typeof XMLHttpRequest != "undefined" && typeof ActiveXObject != "undefined" && $setting["baseScheme"] != "file") {
				request = new XMLHttpRequest();
			} else if (typeof ActiveXObject != "undefined") {
				request = new ActiveXObject("Microsoft.XMLHTTP");
			} else {
				throw Error("XHR not supported");
			}
			source.url = [source.base, source.module, source.path].join("/");
			request.open("GET", source.url, true);
			request.onreadystatechange = callback;
			return request;
		}
		
		function loadCrossDomain(source, callback) {
			var caller = this;
			var request = requestCrossDomain(source, function(response) {
				var timeout = request.time > request.timeout;
				if (!timeout && typeof response != "object") {
					request.time += request.time;
					request.timeoutId = setTimeout(request.callback, request.time);
					return;
				}
				clearRequest(request, source);
				if (timeout) {
					source.error = "request timeout";
				} else if (response.error) {
					source.error = response.error;
				} else {
					source.content = response.content;
				}
				callback.call(caller, source);
			});
			document.firstChild.firstChild.appendChild(source.node);
		}
		
		function requestCrossDomain(source, callback) {
			var request = {};
			source.url = [source.base, $setting["crossDomain"], source.module, source.path].join("/");
			source.node = document.createElement("script");
			source.node.src = source.url;
			request.callback = callback;
			request.time = parseInt($setting["requestTime"]);
			request.timeout = parseInt($setting["requestTimeout"]);
			request.timeoutId = setTimeout(request.callback, request.time);
			this[$setting["bootModule"]][source.url] = request.callback;
			return request;
		}
		
		function clearRequest(request, source) {
			clearTimeout(request.timeoutId);
			delete this[$setting["bootModule"]][source.url];
			document.firstChild.firstChild.removeChild(source.node);
			delete source.node;
		}
		
		function resolvePath(path, base) {
			if (!_URL_.exec(path)[1]) {
				if (path.charAt(0) == "/") {
					path = _URL_.exec(base)[1].concat(path);
				} else {
					base = splitPath(base, -1, -1);
					path = base ? path ? base.concat("/", path) : base : path;
				}
			}
			return path;
		}
		
		function splitPath(path, index, count, delim) {
			if (!path) return path;
			delim = delim || "/";
			var parts = path.split(delim);
			index = index>=0 ? index : parts.length+index;
			var keep = count <= 0;
			count = keep ? -count : count;
			var parts2 = parts.splice(index, count);
			return (keep ? parts : parts2).join(delim);
		}
		
		main();
		
	})();
	
	function $() {
		with (this.scope.$static) {
			with (this.scope.$protected) {
				with (this.scope.$private) {
					try { eval(this.content); }
					finally { delete this.content; }
				}
			}
		}
		this.$class = this.scope[this.baseName];
	}
	
})();
