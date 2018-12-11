package com.izhubo.web

import com.izhubo.rest.anno.RestWithSession;

@RestWithSession
class Demo2Controller extends BaseController {
	
	def con(){
		return ["code" : 1];
	}
	
}
