package io.renren.task;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import io.renren.utils.HttpUtils;

public class AttendancePushJob implements Job{

	//private static String URL = "http://183.63.120.222:8010/msgServer/push_msg_by_plan_id";
	private static String URL = "http://177.77.83.111:8080/msgServer/push_msg_by_plan_id";
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		Map map = context.getJobDetail().getJobDataMap();
		final String classId = (String) map.get("class_id");
		final List list = (List) map.get("datas");
		
		if (null!=list) {
			ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
			cachedThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Map data = null;
					for (int i = 0; i < list.size(); i++) {
						data = (Map) list.get(i);
						if (null!=data) {
							String title = (String) data.get("title");
							String code = (String) data.get("code");
							
						    String para = "msg_title="+title+"&code="+code+"&plan_id="+classId;
		                    HttpUtils.sendGet(URL,para);
		                    System.out.println("AttendancePushJob:"+i);
						}
						
					}
				}
			});
		}
		
		
	}

}
