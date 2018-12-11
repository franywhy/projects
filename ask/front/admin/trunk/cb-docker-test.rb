#!/usr/local/bin/ruby

=begin
require 'fileutils'

#$appid='1101168695'

$appid='1101815070'

$files = ['package.json.test', 'src/fis-conf.js']
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
=end


$LOAD_PATH.unshift(File.dirname(__FILE__) + '/utils') 
puts File.expand_path(File.dirname(__FILE__) + '/utils')


require 'Builder'
$b = Builder::new

$b.baseDomain.nocdnDomain = ''
$b.adminDomain.nocdnDomain =''
# $b.appDomain  				#��Ȼʹ��ԭ���Ŀռ� ������
$b.wwwDomain.nocdnDomain = ''
$b.assetDomain.nocdnDomain = ''
$b.imgDomain.nocdnDomain = ''
$b.rootDomain.nocdnDomain = ''
#$b.chatDomain = 'asset.izhubo.com'   #ʹ�ÿռ�ȱʡֵ
#$b.usDomain = 'asset.izhubo.com'      #ʹ�ÿռ�ȱʡֵ
#$b.mDomain = ''												#ʹ�ÿռ�ȱʡֵ


$b.appid = '1101168695'
$b.fisCmd = 'fis release --no-color -umd online'
$b.files = ['package.json.test', 'src/fis-conf.js']
$b.filePath = File.dirname(__FILE__)
$b.runFis = true
$b.run7z = false
$b.filename7z = 'admin-build.7z'
$b.runTenSDN = FALSE
$b.cdnContext = 'admin'
$b.cdnFolder = 'build/assets'
$b.build