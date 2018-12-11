function parseURL(url) {
    var a = document.createElement('a');
    a.href = url;
    
//        alert(url);
//    a.href = "http://localhost/job_list.html?gwzw=0&area=0&xzsp=0&gznx=0&zsyq=0&sshy=0&key=&page=1";
    return {
        source: url,
        protocol: a.protocol.replace(':', ''),
        host: a.hostname,
        port: a.port,
        query: a.search,
        params: (function () {
            var ret = {},
            seg = a.search.replace(/^\?/, '').split('&'),
            len = seg.length, i = 0, s;
            for (; i < len; i++) {
                if (!seg[i]) { continue; }
                s = seg[i].split('=');
                ret[s[0]] = s[1];
            }
            return ret;

        })(),
        file: (a.pathname.match(/\/([^\/?#]+)$/i) || [, ''])[1],
        hash: a.hash.replace('#', ''),
        path: a.pathname.replace(/^([^\/])/, '/$1'),
        relative: (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [, ''])[1],
        segments: a.pathname.replace(/^\//, '').split('/')
    };
}

//替换myUrl中的同名参数值
function replaceUrlParams(myUrl, newParams) {

    for (var x in newParams) {
        var hasInMyUrlParams = false;
        for (var y in myUrl.params) {
            if (x.toLowerCase() == y.toLowerCase()) {
                myUrl.params[y] = newParams[x];
                hasInMyUrlParams = true;
                break;
            }
        }
        //原来没有的参数则追加
        if (!hasInMyUrlParams) {
            myUrl.params[x] = newParams[x];
        }
    }
    var _result = myUrl.protocol + "://" + myUrl.host + ":" + myUrl.port + myUrl.path + "?";

    for (var p in myUrl.params) {
        _result += (p + "=" + myUrl.params[p] + "&");
    }

    if (_result.substr(_result.length - 1) == "&") {
        _result = _result.substr(0, _result.length - 1);
    }

    if (myUrl.hash != "") {
        _result += "#" + myUrl.hash;
    }
    return _result.replace(":80", "");
    //    return _result;
}

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}