package io.renren.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.renren.dao.QuestionBankDao;
import io.renren.entity.QuestionBankEntity;
import io.renren.service.QuestionBankService;
import io.renren.ws.chapterinfo.WebService1;
import io.renren.ws.chapterinfo.WebService1Soap;
import net.sf.json.JSONArray;

@Service("questionBankService")
public class QuestionBankServiceImpl implements QuestionBankService {

	@Autowired
	private QuestionBankDao questionBankDao;
	@Autowired
	private SchedulerFactoryBean scheduler;

	@Override
	public QuestionBankEntity queryObject(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return questionBankDao.queryObject(map);
	}

	

	@Override
	public List<QuestionBankEntity> queryList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return questionBankDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return questionBankDao.queryTotal(map);
	}

	@Override
	public void save(QuestionBankEntity questionBank) {
		// TODO Auto-generated method stub
		questionBankDao.save(questionBank);
	}

	@Override
	public void update(QuestionBankEntity questionBank) {
		// TODO Auto-generated method stub
		questionBankDao.update(questionBank);
	}

	@Override
	public void delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		questionBankDao.delete(map);
	}

	@Override
	@Transactional
	public void syncData(List list) {
		// TODO Auto-generated method stub
		try {
			questionBankDao.deleteAll();
			questionBankDao.saveBatch(list);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public List queryCourse() {
		// TODO Auto-generated method stub
		return this.questionBankDao.queryCourse();
	}

	@Override
	public List queryChapter(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.questionBankDao.queryChapter(map);
	}

	@Override
	public List queryVerse(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.questionBankDao.queryVerse(map);
	}

	@Override
	public void syncData() {
		// TODO Auto-generated method stub
		WebService1Soap serviceSoap = new WebService1().getWebService1Soap();
		String result = serviceSoap.getChapterInfoHQZX();
		System.out.println(result);
		if (null!=result && !result.trim().equals("")) {
			job(result);
		}
	}

	private void job(String result) {
		// TODO Auto-generated method stub
		QuestionBankEntity questionBank = null;
		JSONArray jArray= JSONArray.fromObject(result);
        Collection collection = JSONArray.toCollection(jArray, QuestionBankEntity.class);
        Iterator it = collection.iterator();
        List list = new ArrayList<>(); 
        while (it.hasNext()) {
        	questionBank = (QuestionBankEntity) it.next();
        	list.add(questionBank);
        }
        if (null!=list && list.size()>0) {
        	syncData(list);
		}
	}

}
