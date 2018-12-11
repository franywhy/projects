package io.renren.mybatis.interceptor;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.renren.entity.SysUserEntity;
import io.renren.utils.ShiroUtils;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Select;
/**
 * 数据权限拦截器
 * @class io.renren.mybatis.interceptor.DataInterceptor.java
 * @Description:
 * @author shihongjie
 * @dete 2017年8月8日
 */
@Intercepts({ @Signature(method = "prepare", type = StatementHandler.class, args = { Connection.class,Integer.class }) }) 
public class DataInterceptor  implements Interceptor {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected Map<String , Object> properties = new HashMap<>();
	
    //Invocation 类方法的一个封装,在拦截器中就是被调用的目标方法
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		//逻辑代码区 
		
		//登录用户
		SysUserEntity userEntity = null;
		try {
			//登录用户
			userEntity = ShiroUtils.getUserEntity();
			logger.info("userinfo:{}", userEntity);
		} catch (Exception e) {
			logger.info("user not login");
		}
		//是否有用户信息
		if(null != userEntity && userEntity.getUserId() != 1){
			//用户ID
			Long userId = userEntity.getUserId();
			//获取SQL
			StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
			BoundSql boundSql = statementHandler.getBoundSql();
			//获取当前要执行的Sql语句，也就是我们直接在Mapper映射语句中写的Sql语句 
			//sql
			String sql = boundSql.getSql();
			//SQL解析器
			CCJSqlParserManager pm = new CCJSqlParserManager();
			net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));
			//查询SQL
			if (statement instanceof Select) {
				//由于mappedStatement为protected的，所以要通过反射获取  
				MetaObject metaObject = SystemMetaObject.forObject(statementHandler);  
				// 拦截到的prepare方法参数是一个Connection对象
				Connection connection = (Connection) invocation.getArgs()[0];
				String newSql = sql;
				logger.info("oooooo newSql:{}", newSql);
				//产品权限
				newSql = ProductSqlUtils.selectSQL(newSql, connection, userId, properties.get("product").toString());
				logger.info("product newSql:{}", newSql);
//				//部门权限
//				newSql = DeptSqlUtils.selectSQL(newSql, connection, userId, properties.get("dept").toString());
//				logger.info("dept newSql:{}", newSql);
				//执行sql
				if(!sql.equals(newSql)){
					metaObject.setValue("delegate.boundSql.sql", newSql);
				}
			}
		}
		
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		logger.info("target:{}" , target);
		 //生成代理对象  
//        return Plugin.wrap(target, this);  
		// 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的
		// 次数
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
		logger.info("properties:{}" , properties);
		if(null != properties){
			for (String p : properties.stringPropertyNames()) {
				this.properties.put(p, properties.getProperty(p));
			}
		}
	}
	/**
	 *	tableName是否是需要校验的表
	 * @param tableName	表名
	 * @return true 需要数据权限
	 */
	protected boolean tableData(String tableName){
		boolean tag = false;
		Object object = properties.get(tableName);
		if(null != object){
			try {
				tag = "true".equals(object);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tag;
	}
	
	private static final String DEPT_SQL = "SELECT DISTINCT(sys_role_dept.dept_id) AS deptId FROM sys_user_role AS sys_user_role LEFT JOIN sys_role_menu AS sys_role_menu ON sys_user_role.role_id = sys_role_menu.role_id LEFT JOIN sys_role_dept AS sys_role_dept ON sys_role_menu.role_id = sys_role_dept.role_id WHERE sys_user_role.user_id = ? ";
	
	protected String selectSQL(String sql, Connection connection, Long userId) throws JSQLParserException {
		logger.info("<<<<<<<<<<<<<<<<<DeptDataInterceptor sql:{}" ,sql);
		//获取表
		List<Table> tableList = PrasingUtils.test_select_table1(sql);
		if(null != tableList){
			for (Table tableItem : tableList) {
				//校验表是否需要加上数据权限
				if(tableData(tableItem.getName())){
//					表名
					String sqlTableName = PrasingUtils.getSqlTableName(tableItem);
					//部门string数组
					String deptIdStr = queryDeptIdStr(connection, userId);
					//条件
					String whereDeptIds = "1=2";
					//如果条件非空
					if(StringUtils.isNotBlank(deptIdStr)){
						whereDeptIds = " " + sqlTableName + ".dept_id in (" + deptIdStr + " )";
					}
					sql = PrasingUtils.append_select_where(sql, whereDeptIds);
				}
			}
		}
		return sql;
	}

	
	/**
	 * 获取部门ID字符串
	 * @param connection
	 * @param userId
	 * @return
	 */
	private String queryDeptIdStr(Connection connection , Long userId){
		StringBuffer sbf = new StringBuffer();
		List<Long> deptIdList = queryDeptIds(connection, userId);
		if(null != deptIdList && !deptIdList.isEmpty()){
			for (int i = 0; i < deptIdList.size(); i++) {
				if(i < deptIdList.size() -1){
					sbf.append(deptIdList.get(i) + ",");
				}else{
					sbf.append(deptIdList.get(i));
				}
			}
		}
		return sbf.toString();
	}
	
	 /**
     * 查询用户拥有的部门权限
     * @param parameterHandler
     * @param boundSql
     * @param connection
     */
    private List<Long> queryDeptIds(Connection connection , Long userId) {
    	//部门ID数组
    	List<Long> deptIdList = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
        	//SQL
            pstmt = connection.prepareStatement(DEPT_SQL);
            //参数
            pstmt.setLong(1, userId);
            //执行查询
            resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
            	deptIdList.add(resultSet.getLong("deptId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();//关闭rs
                if (pstmt != null)
                    pstmt.close();//关闭ps
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return deptIdList;
    }

}