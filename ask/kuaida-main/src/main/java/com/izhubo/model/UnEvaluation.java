package com.izhubo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 不满意
 * @author Administrator
 *
 */
public enum UnEvaluation {
	
	tip1(1 , "没听明白") , tip2(2,"回复太慢") , tip3(3,"讲解错误") ;
	
	private int id;
	private String name;
	private UnEvaluation(int id , String name){
		this.id = id;
		this.name = name;
	}
	

	public int getId() {
		return id;
	}


	public String getName() {
		return name;
	}

	public static List<Map> getUnEvaluationList(){
		List list = new ArrayList();
		for(UnEvaluation item :UnEvaluation.values()){
			Map map = new HashMap();
			map.put("id", item.getId());
			map.put("name", item.getName());
			list.add(map);
		}
		return list;
	}
	
	private static final String NULL = ""; 
	
	private static final String EVAL_HEARD = "此次评价为：";
	/** 满意*/
	private static final String EVAL_SATISFACTION_MSG0 = EVAL_HEARD + "满意"; 
	/** 非常满意 */
	private static final String EVAL_SATISFACTION_MSG1 = EVAL_HEARD + "很满意"; 
	/** 不满意（选了不满意后，不选择原因）*/
	private static final String UNEVAL_MSG0 = EVAL_HEARD + "不满意"; 
	/**不满意——没听懂 */
	private static final String UNEVAL_MSG1 = EVAL_HEARD + "没听懂，不满意"; 
	/**不满意——好久没回复 */
	private static final String UNEVAL_MSG2 = EVAL_HEARD + "回复太慢了，不满意"; 
	/**不满意——感觉不专业 */
	private static final String UNEVAL_MSG3 = EVAL_HEARD + "感觉不太专业，不满意"; 
	/**不满意——没听懂+好久没回复 */
	private static final String UNEVAL_MSG12 = EVAL_HEARD + "没听懂，回复太慢了，不满意"; 
	/** 不满意——没听懂+感觉不专业*/
	private static final String UNEVAL_MSG13 = EVAL_HEARD + "没听懂，感觉不太专业，不满意"; 
	/** 不满意——感觉不专业+好久没回复*/
	private static final String UNEVAL_MSG23 = EVAL_HEARD + "回复太慢了，感觉不太专业，不满意"; 
	/** 不满意——感觉不专业+好久没回复+没听懂*/
	private static final String UNEVAL_MSG123 = EVAL_HEARD + "回复太慢了，没听懂，感觉不专业，不满意"; 
	
	public static String getMsg(int score , String eval_contents){
		String result = NULL;
		if(score == ScoreType.不满意.ordinal()){
			result = UNEVAL_MSG0;
			if(StringUtils.isNotBlank(eval_contents)){
				if(eval_contents.equals("1")){
					result = UNEVAL_MSG1;
				}else if(eval_contents.equals("2")){
					result = UNEVAL_MSG2;
				}else if(eval_contents.equals("3")){
					result = UNEVAL_MSG3;
				}else if(eval_contents.equals("1,2")){
					result = UNEVAL_MSG12;
				}else if(eval_contents.equals("1,3")){
					result = UNEVAL_MSG13;
				}else if(eval_contents.equals("2,3")){
					result = UNEVAL_MSG23;
				}else if(eval_contents.equals("1,2,3")){
					result = UNEVAL_MSG123;
				}
			}
		}else if(score == ScoreType.满意.ordinal()){
			result = EVAL_SATISFACTION_MSG0;
		}else if(score == ScoreType.很满意.ordinal()){
			result = EVAL_SATISFACTION_MSG1;
		}
		
		
		return result;
	}
}
