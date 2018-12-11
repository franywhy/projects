/*
* $ history plugin
* 
* The MIT License
* 
* Copyright (c) 2006-2009 Taku Sano (Mikage Sawatari)
* Copyright (c) 2010 Takayuki Miwa
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/

(function($) {
    var locationWrapper = {
        put: function(hash, win) {
            (win || window).location.hash = this.encoder(hash);
        },
        get: function(win) {
            var hash = ((win || window).location.hash).replace(/^#*/, '');
            try {
                return $.browser.mozilla ? hash : decodeURIComponent(hash);
            }
            catch (error) {
                return hash;
            }
        },
        encoder: encodeURIComponent
    };

    var iframeWrapper = {
        id: "__history",
        init: function() {
            var html = '<iframe id="' + this.id + '" style="display:none" src="javascript:false;" />';
            $("body").prepend(html);
            return this;
        },
        _document: function() {
            return $("#" + this.id)[0].contentWindow.document;
        },
        put: function(hash) {
            var doc = this._document();
            doc.open();
            doc.close();
            locationWrapper.put(hash, doc);
        },
        get: function() {
            return locationWrapper.get(this._document());
        }
    };

    function initObjects(options) {
        options = $.extend({
            unescape: false
        }, options || {});

        locationWrapper.encoder = encoder(options.unescape);

        function encoder(unescape_) {
            if (unescape_ === true) {
                return function(hash) { return hash; };
            }
            if (typeof unescape_ == "string" &&
               (unescape_ = partialDecoder(unescape_.split("")))
               || typeof unescape_ == "function") {
                return function(hash) { return unescape_(encodeURIComponent(hash)); };
            }
            return encodeURIComponent;
        }

        function partialDecoder(chars) {
            var re = new RegExp($.map(chars, encodeURIComponent).join("|"), "ig");
            return function(enc) { return enc.replace(re, decodeURIComponent); };
        }
    }

    var implementations = {};

    implementations.base = {
        callback: undefined,
        type: undefined,

        check: function() { },
        load: function(hash) { },
        init: function(callback, options) {
            initObjects(options);
            self.callback = callback;
            self._options = options;
            self._init();
        },

        _init: function() { },
        _options: {}
    };

    implementations.timer = {
        _appState: undefined,
        _init: function() {
            var current_hash = locationWrapper.get();
            self._appState = current_hash;
            self.callback(current_hash);
            setInterval(self.check, 100);
        },
        check: function() {
            var current_hash = locationWrapper.get();
            if (current_hash != self._appState) {
                self._appState = current_hash;
                self.callback(current_hash);
            }
        },
        load: function(hash) {
            if (hash != self._appState) {
                locationWrapper.put(hash);
                self._appState = hash;
                self.callback(hash);
            }
        }
    };

    implementations.iframeTimer = {
        _appState: undefined,
        _init: function() {
            var current_hash = locationWrapper.get();
            self._appState = current_hash;
            iframeWrapper.init().put(current_hash);
            self.callback(current_hash);
            setInterval(self.check, 100);
        },
        check: function() {
            var iframe_hash = iframeWrapper.get(),
                location_hash = locationWrapper.get();

            if (location_hash != iframe_hash) {
                if (location_hash == self._appState) {    // user used Back or Forward button
                    self._appState = iframe_hash;
                    locationWrapper.put(iframe_hash);
                    self.callback(iframe_hash);
                } else {                              // user loaded new bookmark
                    self._appState = location_hash;
                    iframeWrapper.put(location_hash);
                    self.callback(location_hash);
                }
            }
        },
        load: function(hash) {
            if (hash != self._appState) {
                locationWrapper.put(hash);
                iframeWrapper.put(hash);
                self._appState = hash;
                self.callback(hash);
            }

        }
    };

    implementations.hashchangeEvent = {
        _init: function() {
            self.callback(locationWrapper.get());
            $(window).bind('hashchange', self.check);
        },
        check: function() {
            self.callback(locationWrapper.get());
        },
        load: function(hash) {
            locationWrapper.put(hash);
        }
    };
	
	// 把 implementations.base 导入到 self 新对象中
    var self = $.extend({}, implementations.base);
	
	// 判断是否为 IE 浏览器，如果 IE 版本小于 8 的版本
    if ($.browser.msie && ($.browser.version < 8 || document.documentMode < 8)) {
        self.type = 'iframeTimer';
	// 判断 window 是否有 onhashchange 事件
    } else if ("onhashchange" in window) {
        self.type = 'hashchangeEvent';
	// 非IE浏览器，又不支持 onhashchange 事件，那么采用定时器方式
    } else {
        self.type = 'timer';
    }
	
	// 给 self 对象导入新的属性与方法
    $.extend(self, implementations[self.type]);
	
	// 把 self 对象引用添加到 $.history 属性中
    $.history = self;
})($);

/**
* 扩展框架管理器 
*/
(function($) {
	
    var _history = $.history;
    var _actionList = [];
    var _isInit = false;
	
	$.extend($.history, {
		/**
		* 初始化框架管理器,最好是再页面最后的一个方法中初始化
		* eg: $.hsitory.init();
		*/
		// 在 Izb.ui.startRouter 中调用
		initActionManager : function() {
			// 是否已经初始化？
			if (!_isInit) {
				// 初始化 
				_history.init(function(hash) {
					for (var i = 0; i < _actionList.length; i++) {
						_actionList[i].call(this, hash);
					}
				}, { unescape: true });
				_isInit = true;
			}
		},
		/**
		* 注册Action
		* eg: $.history.registerAction(callback);
		*/
		registerAction : function(callback) {
			// 判断路由是否已经注册
			var found = false;
			for (var i = 0; i < _actionList.length; i++) {
				if (_actionList[i] == callback) {
					found = true;
					break;
				}
			}
			if (!found) {
				_actionList.push(callback);
			}
	
		},
		loadAction : function(url) {
			_history.load(url);
		},
		refreshAction : function() {
			$(window).trigger('hashchange');
		},
		//设置过滤的条件
		//eg:$.history.setAction("pageIndex",1);    
		setAction : function(key, value) {
			var url = $.query.setHash(key, value);
			this.loadAction(url);
		},
		//设置过滤的条件
		//eg:$.historysetActions({"pageIndex":1,"pageSize":12});
		setActions : function(o) {
			var url = self.location.hash;
			$.each(o, function(key, val) {
				if (val != null || val != undefined) {
					url = $.query.setHash(key, val, url);
				}
			});
			this.loadAction(url);
		}
	}); 
}($));