/*
 * @(#) InterfaceConfig.java 2016-4-20 下午1:20:54
 *
 * Copyright 2016 CIMIP, Inc. All rights reserved.
 * CIMIP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */package io.renren.entity;

import java.io.Serializable;

public class InterfaceConfig implements Serializable
{
	private static final long serialVersionUID = 7612562644613097018L;
	private String materialDetailUrl;
	public String getMaterialDetailUrl() {
		return materialDetailUrl;
	}
	public void setMaterialDetailUrl(String materialDetailUrl) {
		this.materialDetailUrl = materialDetailUrl;
	}

	
	}
