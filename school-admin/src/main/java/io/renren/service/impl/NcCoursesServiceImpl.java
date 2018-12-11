package io.renren.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.renren.dao.NcCoursesDao;
import io.renren.entity.NcCoursesEntity;
import io.renren.mongo.dao.ICourseDao;
import io.renren.mongo.entity.ChapterEntity;
import io.renren.mongo.entity.CourseEntity;
import io.renren.mongo.entity.SectionEntity;
import io.renren.service.NcCoursesService;

//@Service("ncCoursesService")
public class NcCoursesServiceImpl implements NcCoursesService {

	@Autowired
	private NcCoursesDao ncCoursesDao;
	@Autowired
	private SchedulerFactoryBean scheduler;
	@Autowired
	private ICourseDao courseDao;

	@Override
	public NcCoursesEntity queryObject(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return ncCoursesDao.queryObject(map);
	}

	

	@Override
	public List<NcCoursesEntity> queryList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return ncCoursesDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return ncCoursesDao.queryTotal(map);
	}

	@Override
	public void save(NcCoursesEntity questionBank) {
		// TODO Auto-generated method stub
		ncCoursesDao.save(questionBank);
	}

	public void update(NcCoursesEntity questionBank) {
		// TODO Auto-generated method stub
		ncCoursesDao.update(questionBank);
	}

	@Override
	public void delete(Map<String, Object> map) {
		// TODO Auto-generated method stub
		ncCoursesDao.delete(map);
	}

	@Override
	@Transactional
	public void syncData(List<NcCoursesEntity> coursess) {
		// TODO Auto-generated method stub
		ncCoursesDao.deleteAll();
		ncCoursesDao.saveBatch(coursess);
	}

	@Override
	public void syncData() {
		// TODO Auto-generated method stub
		List<CourseEntity> results = courseDao.findAll();
		CourseEntity course = null;
		ChapterEntity chapter = null;
		SectionEntity setion = null;
		NcCoursesEntity nc = null;
		List<NcCoursesEntity> ncCourses = new ArrayList<NcCoursesEntity>();
		if (null!=results && results.size()>0) {
			for (int i = 0; i < results.size(); i++) {
				course = results.get(i);
				List<ChapterEntity> chapters = course.getChapter();
				if (null!=chapters && chapters.size()>0) {
					for (int j = 0; j < chapters.size(); j++) {
						chapter = chapters.get(j);
						List<SectionEntity> setions = chapter.getSection();
						if (null!=setions && setions.size()>0) {
							for (int z = 0; z < setions.size(); z++) {
								setion = setions.get(z);
								//
								if (null!=setion) {
									
								
								nc = new NcCoursesEntity();
								nc.setCode(course.getCode());
								nc.setCourseCode(course.getNc_id());
								nc.setCourseName(course.getName());
								//
								nc.setCharpterCode(chapter.getNc_id());
								nc.setCharpterName(chapter.getName());
								//
								nc.setSectionCode(setion.getNc_id());
								nc.setSectionName(setion.getName());
								
								//
								String chapterIdx = "";
								String setionIdx = "";
								if (!chapter.getName().trim().equals("") && chapter.getName().trim().length()>2) {
									chapterIdx = String.valueOf(io.renren.utils.NumberUtils.numberCN2Arab(
											chapter.getName().trim().substring(1, chapter.getName().length()).split("章")[0]));
								}else{
									chapterIdx = "-1";
								}
								
								if (null!=setion.getName()&&!setion.getName().trim().equals("") && setion.getName().trim().length()>2) {
									setionIdx = String.valueOf(io.renren.utils.NumberUtils.numberCN2Arab(
											setion.getName().trim().substring(1, setion.getName().length()).split("节")[0]));
								}else{
									setionIdx = "-1";
								}
								
								nc.setChapterIdx(Long.parseLong(String.valueOf(chapterIdx)));
								nc.setSectionIdx(Long.parseLong(String.valueOf(setionIdx)));
								ncCourses.add(nc);
								}
							}
						}else{
							nc = new NcCoursesEntity();
							nc.setCode(course.getCode());
							nc.setCourseCode(course.getNc_id());
							nc.setCourseName(course.getName());
							
							//
							nc.setCharpterCode(chapter.getNc_id());
							nc.setCharpterName(chapter.getName());
							
							//
							String chapterIdx = "";
							
							if (null!=chapter.getName() && !chapter.getName().trim().equals("") && chapter.getName().trim().length()>2) {
								chapterIdx = String.valueOf(io.renren.utils.NumberUtils.numberCN2Arab(
										chapter.getName().trim().substring(1, chapter.getName().length()).split("章")[0]));
							}else{
								chapterIdx = "-1";
							}
							nc.setChapterIdx(Long.parseLong(String.valueOf(chapterIdx)));
							nc.setSectionIdx(new Long(0));
							ncCourses.add(nc);
						}
					}
				}else{
					nc = new NcCoursesEntity();
					nc.setCode(course.getCode());
					nc.setCourseCode(course.getNc_id());
					nc.setCourseName(course.getName());
					ncCourses.add(nc);
				}
				
				
				
			}
			syncData(ncCourses);
		}
	}

}
