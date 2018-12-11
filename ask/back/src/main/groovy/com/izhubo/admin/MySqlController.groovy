package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$

import java.sql.Timestamp

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.StringUtils
import org.hibernate.SessionFactory
import org.springframework.web.bind.ServletRequestUtils

import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.ReplicaSetStatus.Tag;
import com.mysqldb.model.UserDemo

/**
 * 测试例子
 * @author shihongjie
 * 2016-01-21
 *
 */
@RestWithSession
class MySqlController extends BaseController {

	@Resource
	private SessionFactory sessionFactory;

	private static final String TAG = "mysql";
	/**
	 * 查询
	 */
	def list(HttpServletRequest req){

		int size = ServletRequestUtils.getIntParameter(req , "size" , 20);
		int page = ServletRequestUtils.getIntParameter(req , "page" , 1);

		List userList = sessionFactory
				.getCurrentSession().createCriteria(UserDemo.class)
				.setFirstResult((page-1)*size)
				.setMaxResults(size)
				.list();

		return getResultOKS(userList);
	}

	/**
	 * 新增
	 */
	def add(HttpServletRequest req){
		UserDemo userDto = new UserDemo();
		userDto.setUserName(req.getParameter("userName").toString());
		userDto.setUtimestamp(new Timestamp(System.currentTimeMillis()));

		sessionFactory.getCurrentSession().save(userDto);

		Crud.opLog(TAG,[save:userDto.getId()]);
		return OK();
	}

	/**
	 * 修改
	 */
	def edit(HttpServletRequest req){
		Integer id = req.getParameter("id") as Integer;
		//非空校验
		if(id == null)
			return [code:0];
		//查询
		UserDemo userDto = (UserDemo) sessionFactory.getCurrentSession().get(UserDemo.class, id);
		//非空校验
		if(userDto){
			String userName = req["userName"];
			//非空校验
			if(StringUtils.isNotBlank(userName)){
				userDto.setUserName(userName);
				//保存
				sessionFactory.getCurrentSession().update(userDto);
				sessionFactory.getCurrentSession().flush();
			}
		}
		//日志
		Crud.opLog(TAG,[edit:userDto.getId()]);
		return OK();
	}

	/**
	 * 删除
	 */
	def del(HttpServletRequest req){
		Integer id = req.getParameter("_id") as Integer;
		if(id == null)
			return [code:0]
		UserDemo userDto = (UserDemo) sessionFactory.getCurrentSession().get(UserDemo.class, id);

		sessionFactory.getCurrentSession().delete(userDto);

		sessionFactory.getCurrentSession().flush();
		Crud.opLog(TAG,[del:id]);
		return OK();
	}
}
