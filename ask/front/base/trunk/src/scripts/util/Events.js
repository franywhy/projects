/**
 * @author     LiuZhaoHui <hi.liuzhaoxin@gmail.com>
 * @link       http://www.eatbean.com
**/

;(function (ns, undefined) {
"use strict";

// api
// -------------------------------------------
// Events.on(type, callback)
// Events.once(type, callback)
// Events.emit(type [, arg1 [, arg2 [, ...]]])
// Events.removeListener(type, callback)
// Events.removeAllListeners([type])
// Events.listeners(type)
// Events.pipe()
// Events.removePipe()
// -------------------------------------------

function Events ()
{
	this._listeners = {};
}

Events.prototype = 
{
	constructor : Events, 
	__name__ : "Events", 
	
	_pipeExists : false, 
	_pipeEvents : null, 
	
	on : function (type, callback)
	{
		if (!(type in this._listeners))
		{
			this._listeners[type] = [];
		}
		
		this._listeners[type].push(callback);
		return this.emit("newListener", type, callback);
	}, 
	
	once : function (type, callback)
	{
		callback.__once__ = true;
		this.on(type, callback);
	}, 
	
	emit : function (type)
	{
		if (this._pipeExists)
		{
			return this._pipeEvents.emit.apply(this._pipeEvents, arguments);
		}
		
		if (!(type in this._listeners))
		{
			return this;
		}
		
		var args = Array.prototype.slice.call(arguments, 1);
		var list = this._listeners[type], callback;
		var i = 0, len = list.length;
		
		while (i < len)
		{
			callback = list[i];
			callback.apply(this, args);
			
			if (callback.__once__)
			{
				delete callback.__once__;
				list.splice(i, 1);
				len--;
			}
			else
			{
				i++;
			}
		}
		
		return this;
	}, 
	
	removeListener : function (type, callback)
	{
		if (!(type in this._listeners))
		{
			return this;
		}
		
		var list = this._listeners[type];
		var len = list.length;
		
		while (~--len)
		{
			if (list[len] === callback)
			{
				list.splice(len--, 1);
			}
		}
		
		return this;
	}, 
	
	removeAllListeners : function (type)
	{
		if ("string" == typeof type)
		{
			delete this._listeners[type];
		}
		else
		{
			this._listeners = {};
		}
	}, 
	
	listeners : function (type)
	{
		return undefined === type ? this._listeners : this._listeners[type];
	}, 
	
	pipe : function (events)
	{
		if (!events instanceof Events)
		{
			throw new Error("Cannot create pipe. The first argument must be an event emitter.");
		}
		
		this._pipeExists = true;
		this._pipeEvents = events;
		
		var func = new Function ();
		func.prototype = events;
		
		events = new func();
		events._pipeExists = false;
		events._pipeEvents = null;
		
		return events;
	}, 
	
	removePipe : function ()
	{
		if (this._pipeExists)
		{
			this._pipeExists = false;
			this._pipeEvents = null;
		}
		
		return this;
	}
};

ns.Events = Events;
})($.fn);