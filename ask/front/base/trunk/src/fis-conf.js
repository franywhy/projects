//fis.config.set('modules.lint.js', 'jshint');
//fis.config.set('modules.lint.css', 'csslint');
//fis.config.set('modules.lint.less', 'csslint');
// Hello


var pkg = require('../package.json');

fis.config.set('settings.preprocessor.replace.appid', pkg.appid);
fis.config.set('settings.preprocessor.replace.baseDomain', pkg.baseDomain);
fis.config.set('settings.preprocessor.replace.adminDomain', pkg.adminDomain);
fis.config.set('settings.preprocessor.replace.wwwDomain', pkg.wwwDomain);
fis.config.set('settings.preprocessor.replace.apiDomain', pkg.apiDomain);

fis.config.set('settings.preprocessor.replace.assetDomain', pkg.assetDomain);
fis.config.set('settings.preprocessor.replace.imgDomain', pkg.imgDomain);
fis.config.set('settings.preprocessor.replace.rootDomain', pkg.rootDomain);
fis.config.set('settings.preprocessor.replace.chatDomain', pkg.chatDomain);
fis.config.set('settings.preprocessor.replace.usDomain', pkg.usDomain);
fis.config.set('settings.preprocessor.replace.mDomain', pkg.mDomain);



fis.config.set('modules.preprocessor.js', 'replace');
fis.config.set('modules.preprocessor.css', 'replace');
fis.config.set('modules.preprocessor.html', 'replace');

fis.config.merge({
    roadmap: {
        domain: "lib.kjcity.com",
        path: [{
            reg: "map.json",
            release: false
        }, {
            reg: "**",
            url: "/base$&"
        }]
    },
    deploy: {
        online: {
            to: "../build"
        }
    }
});