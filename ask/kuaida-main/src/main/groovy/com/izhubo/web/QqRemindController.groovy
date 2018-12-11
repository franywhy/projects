package com.izhubo.web

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.izhubo.service.QQRemindService
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.web.BaseController
import com.izhubo.web.api.Web;


@RestWithSession
class QqRemindController extends BaseController {

	@Autowired
	def QQRemindService service;
	
	def sync(){
		def userid = Web.currentUserId
		this.service.synanchorAnchorInfor(userid)
		this.service.anchorOnLineOrOffLineServive(userid, true)
		this.service.anchorAuditInformation(userid)
		[code:1]
	}
	
	//ͬ������������Ϣ
	def  syncAll(){
		this.service.synanchorAnchorInforAll()
		[code:1]
	}
	// ��ȡ�������������Ϣ
	def  anchorAll(){
		this.service.anchorAuditInformationAll()
		[code:1]
	}
}
