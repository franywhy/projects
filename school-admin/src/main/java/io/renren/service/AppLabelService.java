package io.renren.service;

import io.renren.entity.AppLabel;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Administrator on 2018/1/6 0006.
 */
public interface AppLabelService {

    List<AppLabel> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

	AppLabel queryObject(Map<String, Object> map);

	void update(AppLabel appLabel);

	String downKJExcel();

	String downZKExcel();

	String importKJExcel(MultipartFile file);
	
	List<AppLabel> queryListByProfessionName(Map<String,Object> map);
	
	int queryCountByLabelNameAndParentId(Map<String,Object> map);
	
	void save(AppLabel appLabel);

	String importZKExcel(MultipartFile file);

	void resume(Long id);

	void pause(Long id);

	List<AppLabel> queryParentList(Map<String, Object> map);

	int queryParentTotal(Map<String, Object> map);

}
