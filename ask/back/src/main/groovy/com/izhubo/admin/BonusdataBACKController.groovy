package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import org.hibernate.Query
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.transform.Transformers
import org.springframework.web.bind.ServletRequestUtils

import com.izhubo.common.util.DataUtils
import com.izhubo.model.BonusDto
import com.izhubo.rest.anno.RestWithSession
import com.mongodb.BasicDBList;
import com.mongodb.DBCollection

@RestWithSession
class BonusdataBACKController extends BaseController {
	
	@Resource
	private SessionFactory sessionFactory;
	
	public DBCollection topics(){return mainMongo.getCollection("topics");}
	
	@TypeChecked(TypeCheckingMode.SKIP)
	def list(HttpServletRequest request){
		
		//翻页查询
		int size = ServletRequestUtils.getIntParameter(request, "size", 20);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		
		Date stime = Web.getStime(request);
		Date etime = Web.getEtime(request);
		
		//时间校验
		Long stime_long = DataUtils.beginTime(stime.getTime());
		Long etime_long = DataUtils.beginTime(etime.getTime());
		//		总条数
		int count =(Integer)( (etime_long - stime_long) / DataUtils.DAY_MI + ((etime_long - stime_long) % DataUtils.DAY_MI >0 ? 1 : 0 ));
		
		//查询的开始时间
		Long startTime = stime_long + (page-1) * size * DataUtils.DAY_MI;
		//查询的结束时间
		Long endTime = startTime + size * DataUtils.DAY_MI - 1;
		//判断结束时间是否大于用户选择的结束时间
		if(endTime > etime_long ){
			endTime = etime_long;
		}
		Session session = sessionFactory.getCurrentSession();
		//总和数据
		StringBuilder sql_sum = new StringBuilder();
		sql_sum.append(" SELECT ");
//		sql_sum.append("             COUNT(1) as 'open_bonus' , ");
		sql_sum.append("             COUNT(1) as 'open_bonus' , ");
		sql_sum.append("             sum(mmoney) as 'bonus_count'  ");
		sql_sum.append("   FROM bunus");
		sql_sum.append(" WHERE open_time IS NOT NULL ");
		sql_sum.append("     AND open_time >= ? ");//时间和分页
		sql_sum.append("     AND open_time <   ? ");//时间和分页
		
		def dto_sum = session.createSQLQuery(sql_sum.toString())
//								.setResultTransformer(Transformers.aliasToBean(BonusDto.class))
								.setParameter(0, DataUtils.dateToString(stime_long))
								.setParameter(1, DataUtils.dateToString(etime_long))
								.uniqueResult();
		
		//分页数据
		int page_number =( (endTime - startTime ) / DataUtils.DAY_MI )+ 1;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append("             date(open_time) as 'optime', ");
		sql.append("             COUNT(1) as 'open_bonus' , ");
		sql.append("             sum(mmoney) as 'bonus_count'  ");
		sql.append("   FROM bunus");
		sql.append(" WHERE open_time IS NOT NULL ");
		sql.append("     AND open_time >= ? ");//时间和分页
		sql.append("     AND open_time <   ? ");//时间和分页
		sql.append(" GROUP BY date(open_time) ");
		
	
		Query query = session.createSQLQuery(sql.toString()).setResultTransformer(Transformers.aliasToBean(BonusDto.class));
		//时间参数赋值							
		query.setParameter(0, DataUtils.dateToString(startTime));
		query.setParameter(1, DataUtils.dateToString(endTime));
		//查询结果集
		List<BonusDto> list = query.list();
		
		//新增当前页总计
		//发出红包数
		Double page_sum_open_bonus = 0d;
		//奖金数
		Double page_sum_bonus_count = 0d;
		
		List pageList = new ArrayList();
		
		//item时间
		Long itemTime_addTopic = startTime;
		for(int i = 0 ; i < page_number ; i++){
			BonusDto dto = getItemByDateLong(itemTime_addTopic , list);
			
			if( dto== null){
				dto = new BonusDto(new java.sql.Date(itemTime_addTopic));
			}
			//求和
			page_sum_open_bonus = addBigDecimal(page_sum_open_bonus, dto.getOpen_bonus()) ;
			page_sum_bonus_count = addBigDecimal(page_sum_bonus_count, dto.getBonus_count()) ;
			
			pageList.add(dto);
/*			if( dto != null){
				pageList.add(dto);
				//求和
				page_sum_open_bonus = addBigDecimal(page_sum_open_bonus, dto.getOpen_bonus()) ;
				page_sum_bonus_count = addBigDecimal(page_sum_bonus_count, dto.getBonus_count()) ;
			}else{
				dto = new BonusDto(new java.sql.Date(itemTime_addTopic));
			}
*/			
			itemTime_addTopic += DataUtils.DAY_MI;
		}
		
		Map dataMap = new HashMap();
		dataMap["list"] = pageList;
		dataMap["page_sum"] = [ "open_bonus" : page_sum_open_bonus , "bonus_count" : page_sum_bonus_count ];
		dataMap["sum"] = dto_sum;
		
		Map resMap = new HashMap();
		resMap["code"] = 1;
		resMap["data"] = dataMap;
		resMap["count"] = count;
		
		return resMap;
	}
	
	/**
	 * 根据时间匹配数据库查询的结果集
	 * @param time
	 * @param dbo
	 * @return
	 */
	private BonusDto getItemByDateLong(Long time ,  List<BonusDto> list ){
		BonusDto res  = null;
		if(list != null && list.size() > 0){
			for(int i = 0 ; i < list.size(); i++){
				BonusDto item = list.get(i);
				if(item.getOptime().getTime() == time){
					res = item;
					list.remove(item);
					break;
				}
			}
		}
		return res;
	}
	
	/**
	 * java精度运算double 相加
	 * @param d1
	 * @param d2
	 * @return
	 */
	public double addBigDecimal(double d1,BigInteger d2){
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(d2);
		return bd1.add(bd2).doubleValue();
	}
	
	/**
	 * java精度运算double 相加
	 * @param d1
	 * @param d2
	 * @return
	 */
	public double addBigDecimal(double d1,BigDecimal d2){
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		return bd1.add(d2).doubleValue();
	}
	
	
}



 
 