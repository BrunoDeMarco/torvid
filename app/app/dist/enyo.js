(function (scope, bundled) {
	
	var   enyo     = scope.enyo || (scope.enyo = {})
		, manifest = enyo.__manifest__ || (defineProperty(enyo, '__manifest__', {value: {}}) && enyo.__manifest__)
		, exported = enyo.__exported__ || (defineProperty(enyo, '__exported__', {value: {}}) && enyo.__exported__)
		, require  = enyo.require || (defineProperty(enyo, 'require', {value: enyoRequire}) && enyo.require)
		, local    = bundled()
		, entries;

	// below is where the generated entries list will be assigned if there is one
	entries = null;


	if (local) {
		Object.keys(local).forEach(function (name) {
			var value = local[name];
			if (manifest.hasOwnProperty(name)) {
				if (!value || !(value instanceof Array)) return;
			}
			manifest[name] = value;
		});
	}

	function defineProperty (o, p, d) {
		if (Object.defineProperty) return Object.defineProperty(o, p, d);
		o[p] = d.value;
		return o;
	}
	
	function enyoRequire (target) {
		if (!target || typeof target != 'string') return undefined;
		if (exported.hasOwnProperty(target))      return exported[target];
		var   request = enyo.request
			, entry   = manifest[target]
			, exec
			, map
			, ctx
			, reqs
			, reqr;
		if (!entry) throw new Error('Could not find module "' + target + '"');
		if (!(entry instanceof Array)) {
			if (typeof entry == 'object' && (entry.source || entry.style)) {
				throw new Error('Attempt to require an asynchronous module "' + target + '"');
			} else if (typeof entry == 'string') {
				throw new Error('Attempt to require a bundle entry "' + target + '"');
			} else {
				throw new Error('The shared module manifest has been corrupted, the module is invalid "' + target + '"');
			}
		}
		exec = entry[0];
		map  = entry[1];
		if (typeof exec != 'function') throw new Error('The shared module manifest has been corrupted, the module is invalid "' + target + '"');
		ctx  = {exports: {}};
		if (request) {
			if (map) {
				reqs = function (name) {
					return request(map.hasOwnProperty(name) ? map[name] : name);
				};
				defineProperty(reqs, 'isRequest', {value: request.isRequest});
			} else reqs = request;
		}
		reqr = !map ? require : function (name) {
			return require(map.hasOwnProperty(name) ? map[name] : name);
		};
		exec(
			ctx,
			ctx.exports,
			scope,
			reqr,
			reqs
		);
		return exported[target] = ctx.exports;
	}

	// in occassions where requests api are being used, below this comment that implementation will
	// be injected
	

	// if there are entries go ahead and execute them
	if (entries && entries.forEach) entries.forEach(function (name) { require(name); });
})(this, function () {
	// this allows us to protect the scope of the modules from the wrapper/env code
	return {'enyo/options':[function (module,exports,global,require,request){
/*jshint node:true */
'use strict';

/**
* Returns the global enyo options hash
* @module enyo/options
*/

module.exports = (global.enyo && global.enyo.options) || {};

}],'enyo':[function (module,exports,global,require,request){
'use strict';

exports = module.exports = require('./src/options');
exports.version = '2.7.0';

},{'./src/options':'enyo/options'}],'enyo/ready':[function (module,exports,global,require,request){
require('enyo');

// we need to register appropriately to know when
// the document is officially ready, to ensure that
// client code is only going to execute at the
// appropriate time

var doc = global.document;
var queue = [];
var ready = ("complete" === doc.readyState);
var run;
var init;
var remove;
var add;
var flush;
var flushScheduled = false;

/**
* Registers a callback (and optional `this` context) to run after all the Enyo and library code
* has loaded and the `DOMContentLoaded` event (or equivalent on older browsers) has been sent.
* 
* If called after the system is in a ready state, runs the supplied code asynchronously at the
* earliest opportunity.
*
* @module enyo/ready
* @param {Function} fn - The method to execute when the DOM is ready.
* @param {Object} [context] - The optional context (`this`) under which to execute the
*	callback method.
* @public
*/
module.exports = function (fn, context) {
	queue.push([fn, context]);
	// schedule another queue flush if needed to run new ready calls
	if (ready && !flushScheduled) {
		setTimeout(flush, 0);
		flushScheduled = true;
	}
};

/**
* @private
*/
run = function (fn, context) {
	fn.call(context || global);
};

/**
* @private
*/
init = function (event) {
	// if we're interactive, it should be safe to move
	// forward because the content has been parsed
	if ((ready = ("interactive" === doc.readyState))) {
		if ("DOMContentLoaded" !== event.type && "readystatechange" !== event.type) {
			remove(event.type, init);
			flush();
		}
	}
	// for legacy WebKit (including webOS 3.x and less) and assurance
	if ((ready = ("complete" === doc.readyState || "loaded" === doc.readyState))) {
		remove(event.type, init);
		flush();
	}
};

/**
* @private
*/
add = function (event, fn) {
	doc.addEventListener(event, fn, false);
};

/**
* @private
*/
remove = function (event, fn) {
	doc.removeEventListener(event, fn, false);
};

/**
* @private
*/
flush = function () {
	if (ready && queue.length) {
		while (queue.length) {
			run.apply(global, queue.shift());
		}
	}
	flushScheduled = false;
};

// ok, let's hook this up
add("DOMContentLoaded", init);
add("readystatechange", init);

}]
	};

});
//# sourceMappingURL=enyo.js.map