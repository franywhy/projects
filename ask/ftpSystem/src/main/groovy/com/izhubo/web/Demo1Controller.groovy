package com.izhubo.web

import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import com.izhubo.rest.anno.Rest

@Rest
@TypeChecked(TypeCheckingMode.SKIP)
class Demo1Controller extends BaseController{
	
	def con(){
		return ["code" : 1];
	}
	
}
