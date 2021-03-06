#!/usr/local/bin/ruby

=begin
require 'fileutils'

#$appid='1101168695'

$appid='1101815070'

$files = ['package.json', 'src/fis-conf.js.real']
$fisCmd = 'fis release -ltDuopd ../build'

def replaceInFile(filename)
	File.open(filename, 'r+:utf-8') do |f|
		data = f.read
		data.gsub!(/@appid@/, $appid)
		f.rewind
		f << data
		puts "write to #{filename} OK"
	end

end

$curFolder = File.dirname(__FILE__)
Dir.chdir $curFolder


# replace appid

$files.each do |f|
	replaceInFile f
end

if(Dir.exist? 'build')
	FileUtils.rm_rf 'build'
end

Dir.chdir $curFolder + '/src'

$msg = `#{$fisCmd}`
puts $msg
puts 'finish'
STDIN.readline
=end

$LOAD_PATH.unshift(File.dirname(__FILE__) + '/../../../utils/trunk') 
puts File.expand_path(File.dirname(__FILE__) + '/../../../utils/trunk')

require 'Builder'
$b = Builder::new

$b.baseDomain.nocdnDomain = 'http://lib.kjcity.com'
# $b.adminDomain =     #依然使用原来的空间 管理站
# $b.apiDomain  				#依然使用原来的空间 服务器
$b.wwwDomain.nocdnDomain = 'www.kjcity.com'
$b.assetDomain.nocdnDomain = 'asset.kjcity.com'
$b.imgDomain.nocdnDomain = 'img.kjcity.com'
$b.rootDomain.nocdnDomain = 'www.kjcity.com'
#$b.chatDomain = 'asset.izhubo.com'   #使用空间缺省值
#$b.usDomain = 'asset.izhubo.com'      #使用空间缺省值
#$b.mDomain = ''												#使用空间缺省值										#使用空间缺省值


$b.appid = '1101168695'
$b.fisCmd = 'fis release --no-color -umd online'
$b.files = ['package.json.real', 'src/fis-conf.js']
$b.filePath = File.dirname(__FILE__)
$b.runFis = true
$b.run7z = false
$b.filename7z = 'admin-build.7z'
$b.runTenSDN = FALSE
$b.cdnContext = 'admin'
$b.cdnFolder = 'build/assets'
$b.build
