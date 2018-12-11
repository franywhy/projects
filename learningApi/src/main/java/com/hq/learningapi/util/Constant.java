package com.hq.learningapi.util;

/**
 * 常量
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月15日 下午1:23:52
 */
public class Constant {
	
	/**
	 * 考试状态:0.未通过;1.通过
	 */
	public enum PassStatus{
		/** 未通过 */
		UNPASS(0),
		/** 通过 */
		PASS(1);
		
		private int value;
		
		private PassStatus(int value){
			this.value = value;
		}
		
	    public int getValue(){
	    	return value;
	    }
	}
	/**
	 * 学员规划状态 : 0.正常;1.毕业;2.休学
	 */
	public enum UserplanStatus{
		/** 正常 */
		NORMAL(0),
		/** 毕业 */
		GRADUATE(1),
		/** 休学 */
		SUSPEND(2);
		
		private int value;
		
		private UserplanStatus(int value){
			this.value = value;
		}
		
		public int getValue(){
			return value;
		}
	}
	/**
	 * 排课是否可冲突
	 * @class io.renren.utils.Constant.java
	 * @Description:
	 * @author shihongjie
	 * @dete 2017年3月22日
	 */
	public enum CourseEQ{
		NO(0),
		YES(1);
		
		private int value;
		
		private CourseEQ(int value){
			this.value = value;
		}
		
		public int getValue(){
			return value;
		}
	}
	
	/***
	 * 软删除
	 * */
	public enum DR{
		/** 正常 */
		NORMAL(0),
		/** 删除 */
        DEL(1);
		
		private int value;
		
		private DR(int value){
			this.value = value;
		}
		
	    public int getValue(){
	    	return value;
	    }
	}
	
	/**
	 * 状态
	 * @author Huxuehan
	 * @email  null
	 * @date 2017年03月20日 下午10:30
	 * */
	public enum Status{
		/** 停用 */
        PAUSE((byte)0),
        /** 正常 */
        RESUME((byte)1),
		/** 结束*/
		END((byte)2);
		private byte value;
		
		private Status(byte value){
			this.value = value;
		}
		
	    public byte getValue(){
	    	return value;
	    }
	}
	
	public enum Audit{
		PASS(1),
		REJECT(0);
		
		private Integer value;
		
		private Audit(Integer value){
			this.value = value;
		}
		
		public Integer getValue(){
			return value;
		}
		
	}
	/**
	 * 菜单类型
	 * 
	 * @author chenshun
	 * @email sunlightcs@gmail.com
	 * @date 2016年11月15日 下午1:24:29
	 */
    public enum MenuType {
        /**
         * 目录
         */
    	CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        private MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
    /**
     * 定时任务状态
     * 
     * @author chenshun
     * @email sunlightcs@gmail.com
     * @date 2016年12月3日 上午12:07:22
     */
    public enum ScheduleStatus {
    	/**
         * 暂停
         */
    	PAUSE(0),
        /**
         * 正常
         */
    	NORMAL(1);
        

        private int value;

        private ScheduleStatus(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
    
    /**
     * 定时任务的GROUP_NAME类型
     * @author baobao
     * @email 2881393222@qq.com
     * @date 2017年5月22日 上午12:07:22
     */
    public enum ScheduleGroup{
    	MATERIAL_GROUP_NAME("知识点资料推送任务");
    	ScheduleGroup(String text){
    		this.text = text;
    	}
    	private String text;

		public String getText() {
			return text;
		}

		

		
    }
    
    /**
     * 资料推送状态
     * @author baobao
     * @email 2881393222@qq.com
     * @date 2017年5月22日 上午12:07:22
     */
    public enum MaterialDetailPushType{
    	 /**
         * 一次推送
         */
    	ONE_PUSH(0),
        /**
         * 每天推送
         */
    	EVERY_PUSH(1);

        private Integer value;

        private MaterialDetailPushType(Integer value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }

		
    }
    
    //定时任务Bean属性名称
    public static String SCHEDULE_BEAN_NAME = "io.renren.task.PushMsgContentJob";
    //定时任务执行方法名称
    public static String SCHEDULE_METHOD_NAME = "execute";
   
    
    
    public static void main(String args[]){
    	System.out.println(Status.PAUSE.getValue());
    }
}
