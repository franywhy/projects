package io.renren.service.impl;

import io.renren.controller.AppLabelController;
import io.renren.dao.AppLabelDao;
import io.renren.entity.AppLabel;
import io.renren.enums.KjLabelExcelEnum;
import io.renren.enums.ZkLabelExcelEnum;
import io.renren.service.AppLabelService;
import io.renren.utils.ExcelReaderJXL;
import io.renren.utils.JSONUtil;
import io.renren.utils.http.HttpClientUtil4_3;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/6 0006.
 */
@Service
public class AppLabelServiceImpl implements AppLabelService {
	protected static Logger logger = LoggerFactory.getLogger(AppLabelServiceImpl.class);
    @Autowired
    private AppLabelDao appLabelDao;
    
    private static String DOWN_KJ_EXCEL_STRING = "";
    private static String DOWN_ZK_EXCEL_STRING = "";

    //会答问题标签接口地址  http://10.0.85.172:9999
  	private static String KUAIDA_API = "";
      @Value("#{application['kuaida.api']}")
      private void setKUAIDA_API(String str){
    	  KUAIDA_API = str;
      }

    @Override
    public List<AppLabel> queryList(Map<String, Object> map) {
        return appLabelDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return appLabelDao.queryTotal(map);
    }

	@Override
	public AppLabel queryObject(Map<String, Object> map) {
		return appLabelDao.queryObject(map);
	}

	@Override
	public void update(AppLabel appLabel) {
		this.sendToKuaiDaUpdate(appLabel);
		appLabelDao.update(appLabel);
	}

	@Override
	public String downKJExcel() {
		if(StringUtils.isBlank(DOWN_KJ_EXCEL_STRING)){
			StringBuffer sbf = new StringBuffer();
			//表头
			sbf.append("0,0,0,0,专业名称&0,1,0,0,标签名称&0,2,0,0,小图标地址&0,3,0,0,大图标地址&0,4,0,0,课程代码");
			//示例1
			sbf.append("&1,0,0,0,会计实操&1,1,0,0,会计基础&1,2,0,0,http://xxx/xx.png&1,3,0,0,http://xxx/xx.png&1,4,0,0,00143");

			DOWN_KJ_EXCEL_STRING = sbf.toString();
		}
		return DOWN_KJ_EXCEL_STRING;
	}

	@Override
	public String downZKExcel() {
		if(StringUtils.isBlank(DOWN_ZK_EXCEL_STRING)){
			StringBuffer sbf = new StringBuffer();
			//表头
			sbf.append("0,0,0,0,专业名称&0,1,0,0,标签名称&0,2,0,0,课程代码");
			//示例1
			sbf.append("&1,0,0,0,会计&1,1,0,0,企业管理概论&1,2,0,0,00144");

			DOWN_ZK_EXCEL_STRING = sbf.toString();
		}
		return DOWN_ZK_EXCEL_STRING;
	}

	@Override
	public List<AppLabel> queryListByProfessionName(Map<String, Object> map) {
		return this.appLabelDao.queryListByProfessionName(map);
	}

	@Override
	public int queryCountByLabelNameAndParentId(Map<String, Object> map) {
		return this.appLabelDao.queryCountByLabelNameAndParentId(map);
	}

	@Override
	public void save(AppLabel appLabel) {
		this.sendToKuaiDaSave(appLabel);
		this.appLabelDao.save(appLabel);
	}
	
	@Override
	public void resume(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("id", id);
    	map.put("dr", 0);
    	
    	AppLabel appLabel = this.queryObject(map);
    	appLabel.setDr(0);
    	this.sendToKuaiDaUpdate(appLabel);
    	
    	this.appLabelDao.updateDr(map);
	}

	@Override
	public void pause(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("id", id);
    	map.put("dr", 1);
    	
    	AppLabel appLabel = this.queryObject(map);
    	appLabel.setDr(1);
    	this.sendToKuaiDaUpdate(appLabel);
    	
    	this.appLabelDao.updateDr(map);
	}
	
	@Override
	public String importKJExcel(MultipartFile file) {
		if(null != file){
			StringBuffer errorMsg = new StringBuffer();
			List<Map<String,Object>> listMap = new ArrayList<>();
			
			try {
				FileInputStream fio = (FileInputStream) file.getInputStream();
                List<String[]> dataList = ExcelReaderJXL.readExcel(fio);
                String[] header = dataList.get(0);
                if(null == dataList || dataList.isEmpty() || dataList.size() <= 1) {
                    errorMsg.append("文件或内容有问题，请重新导入！");
                    return errorMsg.toString();
                }
                int columnLength = 5;
                //总共5列数据 
                if(header.length != columnLength){
                    errorMsg.append("总列数不正确，请核对一下列数或重新下载导入模板！");
                    return errorMsg.toString();
                }
                for(int i=1,length=dataList.size() ; i<length ; i++){
                	int line = i+1;
                	String[] dataItem = dataList.get(i);
                	//列数校验
                    if(dataItem.length < columnLength) {
                        errorMsg.append("第" + line + "行错误：" + "列数不正确！<br/>");
                        continue;
                    }
                    //列
                    String professionName = dataItem[KjLabelExcelEnum.professionName.getExcelIndex()].trim();
                    String labelName = dataItem[KjLabelExcelEnum.labelName.getExcelIndex()].trim();
                    String smallPicUrl = dataItem[KjLabelExcelEnum.smallPicUrl.getExcelIndex()].trim();
                    String bigPicUrl = dataItem[KjLabelExcelEnum.bigPicUrl.getExcelIndex()].trim();
                    String courseCode = dataItem[KjLabelExcelEnum.courseCode.getExcelIndex()].trim();
                    
                    if(StringUtils.isBlank(professionName)){
                    	errorMsg.append("第" + line + "行错误：" + "专业名称不能为空！<br/>");
                        continue;
                    }
                    if(StringUtils.isBlank(labelName)){
                    	errorMsg.append("第" + line + "行错误：" + "标签名称不能为空！<br/>");
                    	continue;
                    }
                    if(StringUtils.isBlank(smallPicUrl)){
                    	smallPicUrl = AppLabelController.SMALLPICURL;
                    }
                    if(StringUtils.isBlank(bigPicUrl)){
                    	bigPicUrl = AppLabelController.BIGPICURL;
                    }
					if(StringUtils.isBlank(courseCode)){
						errorMsg.append("第" + line + "行错误：" + "课程代码不能为空！<br/>");
						continue;
					}

                    this.saveLabel(professionName, labelName, 0L, courseCode);
                    
                    Map<String,Object> map = new HashMap<>();
                    map.put("professionName",professionName);
                    map.put("labelName",labelName);
                    map.put("smallPicUrl",smallPicUrl);
                    map.put("bigPicUrl",bigPicUrl);
                    map.put("courseCode",courseCode);
                    map.put("productId","0");
                    map.put("isCommon",0);
                    listMap.add(map);
                }
                if(null != listMap && listMap.size() > 0){
                	try {//调用main项目标签新增接口
                		Map<String,String> paramMap = new HashMap<>();
                		String listJSON = JSONUtil.beanToJson(listMap);
                		paramMap.put("labels", listJSON);
                		String result = null;
                		result = HttpClientUtil4_3.sendHttpPost(KUAIDA_API+"/api/tip/addlist", paramMap);
                		System.out.println(result);
                		logger.error("AppLabelServiceImpl .../api/tip/addlist result={},listJSON={}",result,listJSON);
					} catch (Exception e) {
						logger.error("AppLabelServiceImpl .../api/tip/addlist Exception:"+e.toString());
						e.printStackTrace();
					}
                }
                if(errorMsg.length() != 0){
                	errorMsg.append("其余导入成功！<br/>");
                	return errorMsg.toString();
                }else{
                	errorMsg.append("文件上传成功！！！");
                	return errorMsg.toString();
                }
			} catch (Exception e) {
				logger.error("AppLabelServiceImpl importKJExcel Exception:"+e.toString());
				e.printStackTrace();
				errorMsg.append("文件异常，请联系管理员！！！");
            	return errorMsg.toString();
			}
		}
		return null;
	}

	@Override
	public String importZKExcel(MultipartFile file) {
		if(null != file){
			StringBuffer errorMsg = new StringBuffer();
			List<Map<String,Object>> listMap = new ArrayList<>();
			
			try {
				FileInputStream fio = (FileInputStream) file.getInputStream();
                List<String[]> dataList = ExcelReaderJXL.readExcel(fio);
                String[] header = dataList.get(0);
                if(null == dataList || dataList.isEmpty() || dataList.size() <= 1) {
                    errorMsg.append("文件或内容有问题，请重新导入！");
                    return errorMsg.toString();
                }
                int columnLength = 3;
                //总共3列数据 
                if(header.length != columnLength){
                    errorMsg.append("总列数不正确，请核对一下列数或重新下载导入模板！");
                    return errorMsg.toString();
                }
                for(int i=1,length=dataList.size() ; i<length ; i++){
                	int line = i+1;
                	String[] dataItem = dataList.get(i);
                	//列数校验
                    if(dataItem.length < columnLength) {
                        errorMsg.append("第" + line + "行错误：" + "列数不正确！<br/>");
                        continue;
                    }
                    //列
                    String professionName = dataItem[ZkLabelExcelEnum.professionName.getExcelIndex()].trim();
                    String labelName = dataItem[ZkLabelExcelEnum.labelName.getExcelIndex()].trim();
                    String courseCode = dataItem[ZkLabelExcelEnum.courseCode.getExcelIndex()].trim();
                    //非空校验
                    if(StringUtils.isBlank(professionName)){
                    	errorMsg.append("第" + line + "行错误：" + "专业名称不能为空！<br/>");
                        continue;
                    }
                    if(StringUtils.isBlank(labelName)){
                    	errorMsg.append("第" + line + "行错误：" + "标签名称不能为空！<br/>");
                    	continue;
                    }
                    if(StringUtils.isBlank(courseCode)){
                    	errorMsg.append("第" + line + "行错误：" + "课程代码不能为空！<br/>");
                    	continue;
                    }
                    
                    this.saveLabel(professionName, labelName, 1L, courseCode);

                    Map<String,Object> map = new HashMap<>();
                    map.put("professionName",professionName);
                    map.put("labelName",labelName);
                    map.put("courseCode",courseCode);
                    map.put("productId","1");
					map.put("isCommon",0);
                    listMap.add(map);
                }
                if(null != listMap && listMap.size() > 0){
                	try {//调用main项目批量标签新增接口
                		Map<String,String> paramMap = new HashMap<>();
                		String listJSON = JSONUtil.beanToJson(listMap);
                		paramMap.put("labels", listJSON);
                		String result = null;
                		result = HttpClientUtil4_3.sendHttpPost(KUAIDA_API+"/api/tip/addlist", paramMap);
                		System.out.println(result);
                		logger.error("AppLabelServiceImpl .../api/tip/addlist result={},listJSON={}",result,listJSON);
					} catch (Exception e) {
						logger.error("AppLabelServiceImpl .../api/tip/addlist Exception:"+e.toString());
						e.printStackTrace();
					}
                }
                
                if(errorMsg.length() != 0){
                	errorMsg.append("其余导入成功！<br/>");
                	return errorMsg.toString();
                }else{
                	errorMsg.append("文件上传成功！！！");
                	return errorMsg.toString();
                }
                
			} catch (Exception e) {
				logger.error("AppLabelServiceImpl importKJExcel Exception:"+e.toString());
				e.printStackTrace();
				errorMsg.append("文件异常，请联系管理员！！！");
            	return errorMsg.toString();
			}
		}
		return null;
	}
	private void saveLabel(String professionName, String labelName, Long productId, String courseCode){
		try {
			Map<String,Object> map = new HashMap<>();
	        map.put("professionName", professionName);
	        map.put("labelName", labelName);
	        map.put("productId", productId);
	        List<AppLabel> appLabelList = this.queryListByProfessionName(map);
	        if(appLabelList.size() > 0){
	        	map.put("parentId", appLabelList.get(0).getId());
	        	int count = this.queryCountByLabelNameAndParentId(map);
	        	if(count == 0){
	        		AppLabel appLabel = new AppLabel();
	        		appLabel.setParentId(appLabelList.get(0).getId());
	        		appLabel.setLabelName(labelName);
	        		appLabel.setProductId(productId);
	        		appLabel.setDr(0);
	        		appLabel.setCourseCode(courseCode);
	        		appLabel.setIsCommon(0);
	        		this.save(appLabel);
	        	}
	        }else{
	        	AppLabel profession = new AppLabel();
	        	profession.setParentId(0L);
	        	profession.setLabelName(professionName);
	        	profession.setProductId(productId);
	        	profession.setDr(0);
	        	this.save(profession);
	        	
	        	AppLabel appLabel = new AppLabel();
	        	appLabel.setParentId(profession.getId());
	        	appLabel.setLabelName(labelName);
	        	appLabel.setProductId(productId);
	        	appLabel.setDr(0);
				appLabel.setCourseCode(courseCode);
				appLabel.setIsCommon(0);
	        	this.save(appLabel);
	        }
		} catch (Exception e) {
			logger.error("AppLabelServiceImpl saveLabel Exception:"+e.toString());
			e.printStackTrace();
		}
		
	}
	
	private void sendToKuaiDaSave(AppLabel appLabel){
		try {
			
			Map<String,Object> mapLabel = new HashMap<>();
			
			if(appLabel.getType() == 0){//新增专业
				mapLabel.put("type", "0");
				mapLabel.put("professionName", appLabel.getLabelName());
				mapLabel.put("productId", appLabel.getProductId());
				mapLabel.put("dr", "0");
				try {//调用main项目单个标签更新接口
	        		Map<String,String> paramMap = new HashMap<>();
	        		String mapJSON = JSONUtil.beanToJson(mapLabel);
	        		paramMap.put("label", mapJSON);
	        		String result = null;
	        		result = HttpClientUtil4_3.sendHttpPost(KUAIDA_API+"/api/tip/add", paramMap);
	        		logger.info(result);
	        		logger.error("AppLabelServiceImpl .../api/tip/add result={},mapJSON={}",result,mapJSON);
				} catch (Exception e) {
					logger.error("AppLabelServiceImpl .../api/tip/add Exception:"+e.toString());
					e.printStackTrace();
				}
        	}else{//新增标签
        		mapLabel.put("type", "1");
    			mapLabel.put("professionName", appLabel.getParentName());//专业名称
    			mapLabel.put("labelName", appLabel.getLabelName());//标签名称
    			mapLabel.put("productId", appLabel.getProductId().toString());//产品线 0：会计 1：自考
    			mapLabel.put("smallPicUrl", appLabel.getSmallPicUrl());//小图地址
    			mapLabel.put("bigPicUrl", appLabel.getBigPicUrl());//大图地址
    			mapLabel.put("dr", "0");//状态 0：正常 1：禁用
    			mapLabel.put("courseCode", appLabel.getCourseCode());//课程代码
				mapLabel.put("isCommon",appLabel.getIsCommon());
    			try {//调用main项目单个标签更新接口
	        		Map<String,String> paramMap = new HashMap<>();
	        		String mapJSON = JSONUtil.beanToJson(mapLabel);
	        		paramMap.put("label", mapJSON);
	        		String result = null;
	        		result = HttpClientUtil4_3.sendHttpPost(KUAIDA_API+"/api/tip/add", paramMap);
	        		logger.info(result);
	        		logger.error("AppLabelServiceImpl .../api/tip/add result={},mapJSON={}",result,mapJSON);
				} catch (Exception e) {
					logger.error("AppLabelServiceImpl .../api/tip/add Exception:"+e.toString());
					e.printStackTrace();
				}
        	}

		} catch (Exception e) {
			logger.error("AppLabelServiceImpl sendToKuaiDaSave Exception:"+e.toString());
			e.printStackTrace();
		}

	}

	private void sendToKuaiDaUpdate(AppLabel appLabel){

		Map<String,Object> mapLabel = new HashMap<>();

		if(appLabel.getParentId() == 0){//更新专业
			Map<String,Object> map = new HashMap<>();
			map.put("id", appLabel.getId());//获取专业id
			AppLabel oldParent = this.queryObject(map);//获取专业对象

			mapLabel.put("type", "0");
			mapLabel.put("oldProfessionName", oldParent.getLabelName());//旧的专业名称
			mapLabel.put("newProfessionName", appLabel.getLabelName());//新专业名称
			mapLabel.put("productId", appLabel.getProductId().toString());//产品线 0：会计 1：自考
			mapLabel.put("dr", appLabel.getDr().toString());//状态 0：正常 1：禁用
			try {//调用main项目标签更新接口
        		Map<String,String> paramMap = new HashMap<>();
        		String mapJSON = JSONUtil.beanToJson(mapLabel);
        		paramMap.put("label", mapJSON);
        		String result = null;
        		result = HttpClientUtil4_3.sendHttpPost(KUAIDA_API+"/api/tip/update", paramMap);
        		logger.info(result);
        		logger.error("AppLabelServiceImpl .../api/tip/update result={},mapJSON={}",result,mapJSON);
			} catch (Exception e) {
				logger.error("AppLabelServiceImpl .../api/tip/update Exception:"+e.toString());
				e.printStackTrace();
			}
		}else{//更新标签
			AppLabel oldLabel = appLabelDao.selectByPrimaryKey(appLabel.getId());
			AppLabel oldParent = appLabelDao.selectByPrimaryKey(oldLabel.getParentId());

			Map<String,Object> mapParent = new HashMap<>();
			mapParent.put("id", appLabel.getParentId());//获取该标签对应专业的id
			AppLabel parent = this.queryObject(mapParent);//获取该标签对应专业的对象

			Map<String,Object> mapChild = new HashMap<>();
			mapChild.put("id", appLabel.getId());//获取该标签的id
			AppLabel oldChild = this.queryObject(mapChild);//获取标签对象

			mapLabel.put("type", "1");
			mapLabel.put("oldProfessionName", oldParent.getLabelName());//旧专业名称
			mapLabel.put("professionName", parent.getLabelName());//专业名称
			mapLabel.put("oldLabelName", oldChild.getLabelName());//旧的标签名称
			mapLabel.put("newLabelName", appLabel.getLabelName());//新的标签名称
			mapLabel.put("productId", appLabel.getProductId().toString());//产品线 0：会计 1：自考
			mapLabel.put("dr", appLabel.getDr().toString());//状态 0：正常 1：禁用
			mapLabel.put("courseCode", appLabel.getCourseCode());//课程代码
			mapLabel.put("isCommon",appLabel.getIsCommon());
			try {//调用main项目标签更新接口
        		Map<String,String> paramMap = new HashMap<>();
        		String mapJSON = JSONUtil.beanToJson(mapLabel);
        		paramMap.put("label", mapJSON);
        		String result = null;
        		result = HttpClientUtil4_3.sendHttpPost(KUAIDA_API+"/api/tip/update", paramMap);
        		logger.info(result);
        		logger.error("AppLabelServiceImpl .../api/tip/update result={},mapJSON={}",result,mapJSON);
			} catch (Exception e) {
				logger.error("AppLabelServiceImpl .../api/tip/update Exception:"+e.toString());
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<AppLabel> queryParentList(Map<String, Object> map) {
		return this.appLabelDao.queryParentList(map);
	}

	@Override
	public int queryParentTotal(Map<String, Object> map) {
		return this.appLabelDao.queryParentTotal(map);
	}
}
