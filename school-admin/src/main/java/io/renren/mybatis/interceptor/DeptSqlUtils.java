package io.renren.mybatis.interceptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.schema.Table;

/**
 * 数据权限拦截器工具类:部门权限
 * @class io.renren.mybatis.interceptor.DeptSqlUtils.java
 * @Description:
 * @author shihongjie
 * @dete 2017年8月8日
 */
public class DeptSqlUtils {
	protected static Logger logger = LoggerFactory.getLogger(DeptSqlUtils.class);
	private static final String DEPT_SQL = "SELECT DISTINCT (sys_role_dept.dept_id) AS deptId FROM sys_user_role AS sys_user_role LEFT JOIN sys_role_dept AS sys_role_dept ON sys_user_role.role_id = sys_role_dept.role_id WHERE sys_user_role.user_id = ? ";
//	private static final String DEPT_SQL = "SELECT DISTINCT(sys_role_dept.dept_id) AS deptId FROM sys_user_role AS sys_user_role LEFT JOIN sys_role_menu AS sys_role_menu ON sys_user_role.role_id = sys_role_menu.role_id LEFT JOIN sys_role_dept AS sys_role_dept ON sys_role_menu.role_id = sys_role_dept.role_id WHERE sys_user_role.user_id = ? ";
	
	public static String selectSQL(String sql, Connection connection, Long userId , String properString) throws JSQLParserException {
		logger.info("<<<<<<<<<<<<<<<<<sql:{}" ,sql);
		if(StringUtils.isNotBlank(properString)){
			//获取表
			List<Table> tableList = PrasingUtils.test_select_table1(sql);
			if(null != tableList){
				for (Table tableItem : tableList) {
					//校验表是否需要加上数据权限
					if(tableData(tableItem.getName() , properString)){
//						表名
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
		}
		return sql;
	}
	
	/**
	 *	tableName是否是需要校验的表
	 * @param tableName	表名
	 * @return true 需要数据权限
	 */
	public static boolean tableData(String tableName , String str){
		boolean tag = false;
		if(StringUtils.isNotBlank(str) && StringUtils.isNotBlank(tableName)  && str.indexOf(tableName+",") > -1){
			tag = true;
		}
		return tag;
	}
	
	/**
	 * 获取部门ID字符串
	 * @param connection
	 * @param userId
	 * @return
	 */
	public static String queryDeptIdStr(Connection connection , Long userId){
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
	 * 
	 * @param parameterHandler
	 * @param boundSql
	 * @param connection
	 */
	public static List<Long> queryDeptIds(Connection connection, Long userId) {
		// 部门ID数组
		List<Long> deptIdList = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		if (null != connection) {
			try {
				// SQL
				pstmt = connection.prepareStatement(DEPT_SQL);
				// 参数
				pstmt.setLong(1, userId);
				// 执行查询
				resultSet = pstmt.executeQuery();
				while (resultSet.next()) {
					deptIdList.add(resultSet.getLong("deptId"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (resultSet != null)
						resultSet.close();// 关闭rs
					if (pstmt != null)
						pstmt.close();// 关闭ps
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return deptIdList;
	}
	
}
