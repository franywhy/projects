package io.renren.mongo.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import io.renren.mongo.dao.ICourseDao;
import io.renren.mongo.entity.ChapterEntity;
import io.renren.mongo.entity.CourseEntity;
import io.renren.mongo.entity.MongoPagination;
import io.renren.mongo.entity.SectionEntity;

@Repository("courseDao")
public class CourseDaoImpl extends MongodbBaseDao<CourseEntity> implements ICourseDao {

	@Override
	public MongoPagination findAllCourse(int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		Query query = new Query();
		return super.getPage(pageNo, pageSize, query);
		//return this.mongoTemplate.findAll(CourseEntity.class);
	}

	@Override
	protected Class<CourseEntity> getEntityClass() {
		// TODO Auto-generated method stub
		return CourseEntity.class;
	}

	@Override
	public MongoPagination findCourseByName(int pageNo, int pageSize,String name) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.addCriteria(Criteria.where("name").regex(".*?\\" + name + ".*"));
		return super.getPage(pageNo, pageSize, query);
		//return this.mongoTemplate.find(query, CourseEntity.class);

	}

	@Override
	public CourseEntity findCourseById(String id) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.addCriteria(Criteria.where("nc_id").is(id));
		List list = this.mongoTemplate.find(query, CourseEntity.class);
		if (null!=list && list.size()>0) {
			return (CourseEntity) list.get(0);
		}
		return null;
	}

	@Override
	public int queryTotal() {
		// TODO Auto-generated method stub
		long count = this.mongoTemplate.count(new Query(), CourseEntity.class);
		
		return (int) count;
	}

	@Override
	public int queryTotalByName(String name) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.addCriteria(Criteria.where("name").regex(".*?\\" + name + ".*"));
		long count = this.mongoTemplate.count(new Query(), CourseEntity.class);
		return (int) count;
	}

	@Override
	public Map findCourseInfoById(String courseId,String chapterId,String verseId) {
		// TODO Auto-generated method stub
		Query query = new Query();
		Map map = new HashMap();
		query.addCriteria(Criteria.where("nc_id").is(courseId));
		List list = this.mongoTemplate.find(query, CourseEntity.class);
		CourseEntity ce = null;
		ChapterEntity cpe = null;
		SectionEntity se = null;
		if (null!=list && list.size()>0) {
			ce =  (CourseEntity) list.get(0);
		}
		if (null!=ce) {
			map.put("courseName", ce.getName());
			List chapters = ce.getChapter();
			if (null!=chapters && chapters.size()>0) {
				for (int i = 0; i < chapters.size(); i++) {
					cpe = (ChapterEntity) chapters.get(i);
					if (cpe.getNc_id().equals(chapterId)) {
						map.put("chapterName", cpe.getName());
						List<SectionEntity> sections = cpe.getSection();
						
						if (null!=chapters && sections.size()>0) {
							for (int j = 0; j < sections.size(); j++) {
								se = (SectionEntity) sections.get(j);
								if (se.getNc_id().equals(verseId)) {
									map.put("verseName", se.getName());
									
								}
							}
						}
					}
				}
			}
		}
		return map;
	}

	@Override
	public List<ChapterEntity> findChapterByCourseId(String courseId) {
		// TODO Auto-generated method stub
		
		Query query = new Query();
		query.addCriteria(Criteria.where("nc_id").is(courseId));
		List list = this.mongoTemplate.find(query, CourseEntity.class);
		List<ChapterEntity> chapters = null;
		
		
		if (null!=list && list.size()>0) {
			CourseEntity ce =  (CourseEntity) list.get(0);
			if (null!=ce) {
				
			 chapters = ce.getChapter();
			 removeNull(chapters);
			}
		}
		
		return chapters;
	}

	@Override
	public List<SectionEntity> findVerseById(String courseId, String chapterId) {
		// TODO Auto-generated method stub
		
		Query query = new Query();
		query.addCriteria(Criteria.where("nc_id").is(courseId));
		List list = this.mongoTemplate.find(query, CourseEntity.class);
		CourseEntity ce = null;
		ChapterEntity cpe = null;
		List<SectionEntity> sections = null;
		if (null!=list && list.size()>0) {
			ce =  (CourseEntity) list.get(0);
		}
		if (null!=ce) {
			
			List chapters = ce.getChapter();
			if (null!=chapters && chapters.size()>0) {
				for (int i = 0; i < chapters.size(); i++) {
					cpe = (ChapterEntity) chapters.get(i);
					if (cpe.getNc_id().equals(chapterId)) {
						
						sections = cpe.getSection();
						removeNull(sections);
						
					}
				}
			}
		}
		
		
		return sections;
	}

	private void  removeNull(List list){
		
		for (int i = 0; i < list.size(); i++) {
			if (null==list.get(i)) {
				list.remove(i);
			}
		}
	}
	
	public List<CourseEntity> findAll(){
		return super.findAll();
	}

}
