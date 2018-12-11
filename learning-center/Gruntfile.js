module.exports = function(grunt) {
	grunt.initConfig({
		//复制文件
		copy: {
			dist: {
			    files: [{
			    	expand: true,
			    	cwd: 'src/main/webapp/resources/images',
			    	src: ['**'],
			    	dest: 'src/main/webapp/resources/compress/images'
			    }]
			  }
		},
		//图片压缩
		imagemin: {
			/* 压缩图片大小 */  
            dist: {
            	options: {
            		optimizationLevel: 3 //定义 PNG 图片优化水平  
                },  
                files: [{  
                    expand: true,  
                    cwd: 'src/main/webapp/resources/compress/images',  
                    src: ['**/*.{png,jpg,jpeg,gif}'],					// 优化 img 目录下所有 png/jpg/jpeg 图片  
                    dest: 'src/main/webapp/resources/compress/images'	// 优化后的图片保存位置，覆盖旧图片，并且不作提示  
                }]  
            }  
		},  
		//js压缩
		uglify: {
			dist: {
                options: {
                    relative: true
                },
                files: [{
                    	expand: true,		
                        cwd: 'src/main/webapp/resources/js',
                        src: '**/*.js',
                        dest: 'src/main/webapp/resources/compress/js'
                    },{
                        expand: true,		
                        cwd: 'src/main/webapp/resources/lib',
                        src: '**/*.js',
                        dest: 'src/main/webapp/resources/compress/lib'
                    }]
            }
		},
		//css压缩
		cssmin: {
			dist: {
                options: {
                    relative: true
                },
                files: [{
                    expand: true,
                    cwd: 'src/main/webapp/resources/css',
                    src: '**/*.css',
                    dest: 'src/main/webapp/resources/compress/css'
                },
                {
                    expand: true,
                    cwd: 'src/main/webapp/resources/lib',
                    src: '**/*.css',
                    dest: 'src/main/webapp/resources/compress/lib'
                }]
            },
		},
		//文件合并
		concat: {
			baseJs: {
				options: {
					separator: ';'
				},
           		src: [
           		      'src/main/webapp/resources/compress/lib/jquery/jquery-1.11.0.min.js',
           		      'src/main/webapp/resources/compress/js/common/common.js',
           		      'src/main/webapp/resources/compress/lib/jquery/jquery.blockUI.js'
           		      ],
                dest: 'src/main/webapp/resources/compress/concat/hqBase.js'
            },
            baseCss:{
				options: {
					separator: ''
				},
				src: [
				      'src/main/webapp/resources/compress/css/reset.css',
				      'src/main/webapp/resources/compress/css/common.css'
				      ],
                dest: 'src/main/webapp/resources/compress/concat/hqBase.css'
			}
		}
	});
	
	grunt.loadNpmTasks('grunt-contrib-copy');
	grunt.loadNpmTasks('grunt-contrib-cssmin');
	grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-imagemin');
    grunt.registerTask('default', [
                                   'copy:dist',
                                   'uglify:dist',
                                   'cssmin:dist',
                                   'concat:baseCss',
                                   'concat:baseJs',
                                   'imagemin:dist'
                                   ]
    );
    
}