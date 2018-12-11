package com.izhubo.web.score;

import javax.annotation.Resource;

import com.izhubo.model.AccScoreGainType;
import com.izhubo.model.AccScoreType;
import com.izhubo.web.CommoditysController;

public class ScoreFactory {

	@Resource
	private ScoreBase scoreBase;
	
	
	//根据积分枚举，构造积分帮助类
	public ScoreBase getScoreType(AccScoreGainType scoreGainType )
	{
		
	   switch(scoreGainType){// C
		 default:
		  return  scoreBase; break;// D
		}
		
		
	}
	
}
