/*
 * @(#) PropertiesUtil.java 2016-4-20 下午1:16:20
 *
 * Copyright 2016 CIMIP, Inc. All rights reserved.
 * CIMIP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package io.renren.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import io.renren.constant.IInterfaceConfig;
import io.renren.entity.InterfaceConfig;



public class PropertiesUtil
{

	private static final String INTERFACE_CONFIG_PROP_FILE = "/properites/dev/config.properties";

	public static InterfaceConfig loadInterfaceConfigProperties()
		throws Exception
	{
		InterfaceConfig interConfig = new InterfaceConfig();

		Properties props = new Properties();
		/*this.Class.getClassLoader().getResource("")  */
		/*String propFilePath = "src"+ File.separator+"main"+File.separator+"resources" + File.separator +"properties"+ File.separator+"dev" + File.separator
			+ INTERFACE_CONFIG_PROP_FILE;*/
		/*String propFilePath = "properites"+ File.separator+"dev" + File.separator
				+ INTERFACE_CONFIG_PROP_FILE;*/

		try
		{
			InputStream inStream = PropertiesUtil.class.getResourceAsStream(INTERFACE_CONFIG_PROP_FILE);
			/*FileInputStream fis = new FileInputStream(propFilePath);*/
			props.load(inStream);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
//		/*interConfig.setHome(home);
//		interConfig.setBrokerURL(props.getProperty(IInterfaceConfig.BROKER_URL));*/
		interConfig.setMaterialDetailUrl(props.getProperty(IInterfaceConfig.MATERIAL_DATAIL_URL));
		return interConfig;
	}

}
