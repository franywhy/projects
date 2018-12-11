package com.izhubo.web.drawReward;

import com.izhubo.web.ServerTypeEnum;

public enum SystemAdministratorEnum {
	systemid(10012743, 10036855, 232), 
	;
	
	private int localId;
	private int betaTestId;
	private int productId;
	
	private SystemAdministratorEnum(int localId, int betaTestId, int productId){
		this.localId = localId;
		this.betaTestId = betaTestId;
		this.productId = productId;
	}

	public int getId(ServerTypeEnum serverTypeEnum){
		int id;
		switch(serverTypeEnum){
			default:
			case product:
				id = productId;
				break;
			case betaTest:
				id = betaTestId;
				break;
			case testRemote:
				id = localId;
				break;
		}
		
		return id;
	}
}
