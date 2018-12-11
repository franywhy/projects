fis.set('project.ignore', ['node_modules/**', 'output/**', 'fis-conf.js', 'package.json', 'prod/**', 'README.md']);

fis.match('*.less', {
  parser: fis.plugin('less-2.x'),
  rExt: '.css',
  preprocessor: fis.plugin('autoprefixer', {
    "browsers": ["Android >= 2.1", "iOS >= 4", "ie >= 8", "firefox >= 15"],
    "cascade": true
  })
});

fis.media('prod')
  .match('*.{less,js,jpg,png}', {
    url: '//img.hengqijiaoyu.cn$0'
    // url: '$0'
  })
  .match('*.js', {
    optimizer: fis.plugin('uglify-js')
  })
  .match('*.less', {
    optimizer: fis.plugin('clean-css')
  })
  .match('*.png', {
    optimizer: fis.plugin('png-compressor')
  });
