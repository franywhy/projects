#!/usr/local/bin/ruby

=begin
require 'fileutils'

#$appid='1101168695'

$appid='1101815070'

$files = ['package.json', 'src/fis-conf.js']
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

require 'Builder'
$b = Builder::new
$b.appid = '1101168695'

$b.baseDomain.nocdnDomain = ''
# $b.adminDomain =     #��Ȼʹ��ԭ���Ŀռ� ����վ
# $b.appDomain  				#��Ȼʹ��ԭ���Ŀռ� ������
$b.wwwDomain.nocdnDomain = ''
$b.assetDomain.nocdnDomain = ''
$b.imgDomain.nocdnDomain = ''
$b.rootDomain.nocdnDomain = ''
#$b.chatDomain = 'asset.izhubo.com'   #ʹ�ÿռ�ȱʡֵ
#$b.usDomain = 'asset.izhubo.com'      #ʹ�ÿռ�ȱʡֵ
#$b.mDomain = ''												#ʹ�ÿռ�ȱʡֵ


$b.fisCmd = 'fis release --no-color -ltDud ../build'
$b.files = ['package.json.real', 'src/fis-conf.js.real']
$b.filePath = File.dirname(__FILE__)
$b.runFis = true
$b.run7z = false
$b.runTenSDN = false
$b.cdnContext = 'base'
$b.cdnFolder = 'build'
$b.filename7z = 'base.7z'
$b.build