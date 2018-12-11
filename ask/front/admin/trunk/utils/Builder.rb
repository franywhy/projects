#!/usr/local/bin/ruby

require 'fileutils'


class Domain
	
	@cdnDomain = nil
	@nocdnDomain = nil
	@appid
	@cdnContext
	@prefix
	
	
	attr_accessor :cdnDomain, :nocdnDomain, :cdnContext, :appid
	
	def initialize(appid, cdnContext, nocdnDomain, cdnDomain, prefix)
		@appid, @cdnContext = appid, cdnContext
		@nocdnDomain, @cdnDomain = nocdnDomain, cdnDomain
		@prefix = prefix
	end
	
	def reset
		@cdnContext = @cdnContext == nil ? "app#{@appid}.imgcache.qzoneapp.com/app#{@appid}/#{@cdnContext}" : @cdnContext
		@nocdnDomain = @nocdnDomain == nil ? "#{@prefix}.app#{@appid}.twsapp.com" : @nocdnDomain
	end
		

	def domain(cdn)
		cdn ? cdnDomain : nocdnDomain
	end
	
end



class Builder
	@appid = '1101168695'
	
	@fisCmd = 'fis release -ltDuopd ../build'
	@files = ['package.json.real', 'src/fis-conf.js.real']
	@runFis = true
	@runTenSDN = false
	@run7z = true
	@filename7z = 'base.7z'
	@cmd7z = "7z a base.7z build"
	
	@cdnFolder = ''
	
	# @runTenSDN == TRUE 将启用cdn路径，否则使用domain
	
	@baseDomain
	@adminDomain
	@apiDomain
	@wwwDomain
	
	@assetDomain
	@imgDomain
	@rootDomain
	@chatDomain
	@usDomain
	@mDomain
	
	@domains
	
	@cdnUploadPath
	@cdnUrl
	@cdnContext
	@cdnUsername
	@cdnPw
	
	# 编译脚本所在目录
	@filePath = ''
	
	@isAlone = false
	
	@runSomeReplace = false
	
	attr_accessor :appid, :fisCmd, :files, :filePath
	attr_accessor :runFis, :runTenSDN, :run7z, :filename7z
	attr_accessor :cdnContext, :cdnFolder
	
	attr_accessor :baseDomain, :adminDomain, :apiDomain, :wwwDomain, :assetDomain
	attr_accessor :imgDomain, :rootDomain, :chatDomain, :usDomain, :mDomain
	
	attr_accessor :isAlone, :runSomeReplace
	
	def initialize
	
		#puts "@appid: #{@appid}"
		@baseDomain = Domain.new(@appid, 'base', nil, nil, 'ttlib')
		@adminDomain = Domain.new(@appid, 'admin', nil, nil, 'ttadmin')
		@apiDomain = Domain.new(@appid, 'api', nil, nil, 'ttapi')
		@wwwDomain = Domain.new(@appid, 'www', nil, nil, 'ttwww')
		
		@assetDomain = Domain.new(@appid, 'asset', nil, nil, 'ttasset')
		@imgDomain = Domain.new(@appid, 'img', nil, nil, 'ttimg')
		@rootDomain = Domain.new(@appid, 'root', nil, nil, 'ttroot')
		@chatDomain = Domain.new(@appid, 'chat', nil, nil, 'ttwsshowd')
		@usDomain = Domain.new(@appid, 'us', nil, nil, 'ttus')
		@mDomain = Domain.new(@appid, 'm', nil, nil, 'ttm')
	
		@domains = [@baseDomain, @adminDomain, @apiDomain, @wwwDomain, @assetDomain, 
		@imgDomain, @rootDomain, @chatDomain, @usDomain, @mDomain]
	end
	
	def resetAll
		@cmd7z = "7z a #{@filename7z} build"
		@cdnUploadPath = "https://cdn.yun.qq.com/#{@appid}"
		@cdnUrl = "app#{@appid}.imgcache.qzoneapp.com/app#{@appid}/#{@cdnContext}/"
		@domainReplace = @runTenSDN ? @cdnUrl : @domain
		
		#print "domains #{@domains}\n"
		
		@domains.each do |x|
			x.appid = @appid
			x.reset
		end
		
	end
	
	
	#在执行完fis命令之后，直接修改build目录里面的某一些文件
	def changeSomeFile
		$data=''
		
		File.open('../build/website/scripts/common.js', 'r+:utf-8') do |f|
			$data = f.read
			$data.gsub!(/@isAlone@/, @isAlone ? 'true' : 'false')
			#f << data	
		end
		
		if($data != '')
			File.open('../build/website/scripts/common.js', 'w+:utf-8') do |f|
				f << $data
			end
			FileUtils.cp('../build/website/scripts/common.js', 
				'../build/assets/scripts/common.js')
		end
		
		
	end
	
	def build
		resetAll
		
		Dir.chdir @filePath
		
		@files.each do |f|
			replaceInFile f
		end
		
		if(Dir.exist? 'build')
			FileUtils.rm_rf 'build'
		end
		
		Dir.chdir @filePath + '/src'
		

	
		if(@runFis)
			puts 'ready to run fis'
			msg = `#{@fisCmd}`
			puts "run fis: #{msg}"
		end
		
		if(@runSomeReplace)
			changeSomeFile
		end
		
		
		Dir.chdir @filePath
		
		
		if(@runTenSDN)
			

			
			cdnFolder = "cdn_build/#{@appid}/#{@cdnContext}"
			
			if(File.exist? cdnFolder)
				#puts "cdnFolder #{cdnFolder}"
				FileUtils.rm_rf(Dir.glob(cdnFolder + '/*'))
			end
			
			FileUtils.mkdir_p cdnFolder
			FileUtils.cp_r(Dir.glob("#{@cdnFolder}/*"), cdnFolder)
		end
		
		
		if(@run7z)
			if(File.exist? @filename7z)
				File.delete @filename7z
			end
			puts "ready to run #{@cmd7z}"
			msg = `#{@cmd7z}`
			puts "#{msg}"
		end
		
		

		
		puts 'finish'
		STDIN.readline		
	end
	
	def replaceInFile(filename)
		wfilename = filename[0, filename.length - 5]
		File.open(filename, 'r+:utf-8') do |f|
			data = f.read
			#puts "data: #{data}"
			data.gsub!(/@appid@/, @appid)
			
			@domains.each do |x|
				#puts "x: #{x}, x.cdnContext: #{x.cdnContext}, x.domain(@runTenSDN): #{x.domain(@runTenSDN)}"
				regstr = "@#{x.cdnContext}Domain@"
				#puts regstr
				data.gsub!(/#{regstr}/, x.domain(@runTenSDN))
			end
			#puts "data: #{data}"
			#f.rewind
			File.open(wfilename, 'w+:utf-8') do |wf|
				wf << data
			end
			puts "write to #{filename} OK"
		end

	end
end