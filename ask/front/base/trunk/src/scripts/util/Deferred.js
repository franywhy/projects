
;(function (ns, undefined) {
"use strict";

/*
Deferred.chain
([
	function ()
	{
		var defer = Deferred.promise();
		setTimeout(function () {defer.done(null, "load")}, 100);
		return defer;
	}, 
	function (err, data)
	{
		var defer = Deferred.promise();
		setTimeout(function () {defer.done(null, data + "-ok")}, 100);
		return defer;
	}
]).then(function (err, data)
{
	console.log(data); // load-ok
});
*/

function Promise ()
{
	this._callbacks = [];
}

Promise.prototype = 
{
	__name__ : "Promise", 
	constructor : Promise, 
	
	_error : null, 
	_isDone : false, 
	
	then : function (func)
	{
		if (this._isDone)
		{
			func(this._error, this._result);
		}
		else
		{
			this._callbacks.push(func);
		}
		
		return this;
	}, 
	
	done : function (error, result)
	{
		if (error) this._error = error;
		this._isDone = true;
		this._result = result;
		
		var item, callbacks = this._callbacks;
		while (item = callbacks.shift())
		{
			item(this._error, this._result);
		}
		
		return this;
	}
};

function Join (callbacks)
{
	this._errors = [];
	this._results = [];
	this._total = callbacks.length;
	this._defer = new Promise();
	
	for (var i = 0, len = this._total; i < len; i++)
	{
		callbacks[i]().then(this.notifier_(i));
	}
}

Join.prototype = 
{
	__name__ : "Join", 
	constructor : Join, 
	_current : 0, 
	
	getPromise : function ()
	{
		return this._defer;
	}, 
	
	notifier_ : function (index)
	{
		var self = this;
		
		return function (error, result)
		{
			self._errors[index] = error;
			self._results[index] = result;
			
			if (++self._current === self._total)
			{
				self._defer.done(self._errors, self._results);
			}
		};
	}
};

function Chain (callbacks)
{
	this._defer = new Promise();
	this._callbacks = callbacks;
	this._total = callbacks.length;
	this.handle_();
}

Chain.prototype = 
{
	constructor : Chain, 
	__name__ : "Chain", 
	_current : 0, 
	
	getPromise : function ()
	{
		return this._defer;
	}, 
	
	handle_ : function (error, result)
	{
		if (this._current === this._total)
		{
			this._defer.done(error, result);
		}
		else
		{
			this._callbacks[this._current++](error, result).then(this.next_());
		}
	}, 
	
	next_ : function ()
	{
		var self = this;
		
		return function (error, result)
		{
			self.handle_(error, result);
		};
	}
};

var Deferred = 
{
	__name__ : "Deferred", 
	
	promise : function () {return new Promise()}, 
	join : function (callbacks){return new Join(callbacks).getPromise()}, 
	chain : function (callbacks){return new Chain(callbacks).getPromise()}
};

ns.Deferred = Deferred;
})($.fn);