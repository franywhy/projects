package io.renren.service.impl.manage;

import io.renren.dao.manage.StudentCourseDao;
import io.renren.entity.OrderMessageConsumerEntity;
import io.renren.entity.manage.StudentCourse;
import io.renren.service.manage.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Administrator on 2018/1/9 0009.
 * @author hq
 */
@Service
public class StudentCourseServiceImpl implements StudentCourseService {

    @Autowired
    private StudentCourseDao studentCourseDao;

    @Override
    @Transactional(rollbackFor={RuntimeException.class, Exception.class})
    public void insertBatch(Long userId, OrderMessageConsumerEntity order) {
        String ncId = order.getNc_id();
        String ncUserId = order.getNc_user_id();
        String ncCommodityId = order.getNc_commodity_id();
        String businessId = order.getCompany_type();
        List<StudentCourse> list = order.getStudentCourses();
        if(null == list || list.size() == 0) {
            studentCourseDao.deleteByNcId(ncId);
            return;
        }

        Set<String> newCourseNo = new HashSet<>(list.size());
        Map<String,String> courseNoNameMap = new HashMap<>(list.size());
        for (StudentCourse item : list) {
            newCourseNo.add(item.getCourseNo());
            courseNoNameMap.put(item.getCourseNo(),item.getCourseName());
        }
        Set<String> oldCourseNo = studentCourseDao.queryCourseNoListByNcId(ncId);

        //库中的课程代码 与 新增的课程代码 取差集（用于删除）
        Set<String> oldRemoveNew = new HashSet<>();
        oldRemoveNew.addAll(oldCourseNo);
        oldRemoveNew.removeAll(newCourseNo);

        if(oldRemoveNew.size() > 0) {
            studentCourseDao.deleteByNcIdCourseNo(ncId, oldRemoveNew);
        }

        //新增的课程代码 与 库中的课程代码 取差集（用于新增）
        Set<String> newRemoveOld = new HashSet<>();
        newRemoveOld.addAll(newCourseNo);
        newRemoveOld.removeAll(oldCourseNo);

        if(newRemoveOld.size() > 0) {
            List<StudentCourse> addList = new ArrayList<>(newRemoveOld.size());
            for (String courseNo : newRemoveOld) {
                StudentCourse item = new StudentCourse();
                item.setCourseNo(courseNo);
                item.setCourseName(courseNoNameMap.get(courseNo));
                item.setUserId(userId);
                item.setNcId(ncId);
                item.setNcUserId(ncUserId);
                item.setNcCommodityId(ncCommodityId);
                item.setBusinessId(businessId);
                addList.add(item);
            }
            studentCourseDao.insertBatch(addList);
        }
    }

    @Override
    public int deleteByNcId(String ncId) {
        return studentCourseDao.deleteByNcId(ncId);
    }
}
