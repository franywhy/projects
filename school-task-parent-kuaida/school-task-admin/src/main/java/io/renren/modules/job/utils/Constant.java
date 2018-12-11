package io.renren.modules.job.utils;

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
	 * @class io.renren.modules.job.utils.Constant.java
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

    /***
     * 学员状态
     * */
    public enum UserStatus{
        /** 其他 */
        OTHERS(-1),
        /** 正常 */
        LEARNING(0),
        /** 休学 */
        PAUSE(1),
        /** 失联 */
        NON_CONTACT(2),
        /** 弃考 */
        ABANDON(3),
        /** 免考 */
        EXEMPT(4);

        private int value;

        private UserStatus(int value){
            this.value = value;
        }

        public int getValue(){
            return value;
        }
    }
    
    //定时任务Bean属性名称
    public static String SCHEDULE_BEAN_NAME = "io.renren.modules.job.task.PushMsgContentJob";
    //定时任务执行方法名称
    public static String SCHEDULE_METHOD_NAME = "execute";

    public final static int THREAD_POOL_CORE_SIZE = 20; //线程池的实际个数
    public final static int THREAD_POOL_MAX_SIZE = 1100;//线程池的最大个数
    public final static int THREAD_POOL_ALIVE_TIME = 60;//线程池的生命时间
    public final static int THREAD_POOL_QUEUE = 100000; //等待线程池的队列

    //弃考免考单-excel模板-课程模板
    public static final String COURSE_ABNORMAL_CLASSPLAN_EXCEL_TEMPAL_COURSE = "0,0,0,0,学员手机号码&0,1,0,0,课程名称&0,2,0,0,类型-【弃考】或【免考】&0,3,0,0,原因&0,4,0,0,备注&1,0,0,0,13811223344&1,1,0,0,会计基础&1,2,0,0,弃考&1,3,0,0,工作出差&1,4,0,0,学员工作出差2018年下学期补考&2,0,0,0,13511223344&2,1,0,0,数学&2,2,0,0,免考&2,3,0,0,学员报考修改省份新省份北京不需要考试&2,4,0,0,转省政策原因免考";
//	//弃考免考单-excel模板-排课模板
//    public static final String COURSE_ABNORMAL_CLASSPLAN_EXCEL_TEMPAL_CLASSPLAN = "0,0,0,0,学员手机号码&0,1,0,0,排课计划名称&1,0,0,0,13811223344&1,1,0,0,2018年会计基础精讲课&2,0,0,0,13511223344&2,1,0,0,2018年数学基础知识";
   
    
    public static void main(String args[]){
    	System.out.println(Status.PAUSE.getValue());
    }
}
