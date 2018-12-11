package com.izhubo.util.accscore;

import com.izhubo.model.AccScoreGainType;
import com.izhubo.model.AccScoreType;

public class ScoreAccumulateBase {
	
	//新增积分
		private void AddScore(double score,AccScoreGainType scoreGainType,Integer userid )
		{
			if(IsAllow(score,scoreGainType, userid ))
			{
				addScoreIntoDataBase(score,scoreGainType, userid );
			}
		}
		
		//是否允许新增积分：新增积分的时候，判断是否允许，基础类库，只能判断每天限制的积分，和只赠送首次的积分，对于其他附加条件则无法判断。
		private boolean IsAllow(double score,AccScoreGainType scoreGainType,Integer userid )
		{
		
			return false;
		}
		
		private void addScoreIntoDataBase(double score,AccScoreGainType scoreGainType,Integer userid )
		{
			
		}
		
		//将积分消息推送到服务端
		public void PushScoreMsg(double score,AccScoreGainType scoreGainType,Integer userid  )
		{
			//
		}

}
