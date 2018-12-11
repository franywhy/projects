package io.renren.mybatis.interceptor;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.renren.sqlparser.visitor.RTablesNamesFinder;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;
/**
 * http://aladdin1001.iteye.com/blog/804847
 * @class io.renren.mybatis.interceptor.PrasingUtils.java
 * @Description:
 * @author shihongjie
 * @dete 2017年9月5日
 */
public class PrasingUtils {
	protected static Logger logger = LoggerFactory.getLogger(PrasingUtils.class);
	public static void main(String[] args) throws Exception {
//		String sql1 = "select * from table";
//		String sql1 = "select *, (select * from selectTable) from fromTable1 as ft1 , fromtable2 as ft2 left join fromTable3 as ft3 on ft3.id = ft2.id";
		String sql = "SELECT "+
				"	course_userplan_detail.userplan_detail_id AS userplanDetailId, "+
				"	course_userplan_detail.userplan_id AS userplanId, "+
				"	course_userplan.dept_id AS deptId "+
				"FROM "+
				"	course_userplan AS course_userplan, "+
				"	mall_order AS mall_order, "+
				"	course_userplan_detail AS course_userplan_detail "+
				"LEFT JOIN ( "+
				"	SELECT "+
				"		course_userplan_class_detail.userplan_class_detail_id, "+
				"		course_userplan_class_detail.userplan_detail_id "+
				"	FROM "+
				"		course_userplan_class_detail "+
				"	JOIN course_userplan_class AS course_userplan_class ON ( "+
				"		course_userplan_class.userplan_class_id = course_userplan_class_detail.userplan_class_id "+
				"		AND course_userplan_class.dr = 0 "+
				"	) "+
				"	WHERE "+
				"		course_userplan_class_detail.dr = 0 "+
				") AS aa ON ( "+
				"	aa.userplan_detail_id = course_userplan_detail.userplan_detail_id "+
				") "+
				"WHERE "+
				"	course_userplan_detail.dr = 0 "+
				"AND course_userplan.user_plan_id = course_userplan_detail.userplan_id "+
				"AND mall_order.order_id = course_userplan.order_id "+
				"AND aa.userplan_class_detail_id IS NULL "+
				"AND mall_order.dr = 0 "+
				"AND course_userplan.dr = 0 "+
				"AND course_userplan_detail.course_id = 445";
//		String sql = "SELECT course_userplan_detail.userplan_detail_id AS userplanDetailId, course_userplan_detail.userplan_id AS userplanId, course_userplan.dept_id AS deptId FROM course_userplan AS course_userplan, mall_order AS mall_order, course_userplan_detail AS course_userplan_detail LEFT JOIN course_userplan_class_detail AS course_userplan_class_detail JOIN course_userplan_class AS course_userplan_class ON (	course_userplan_class.userplan_class_id = course_userplan_class_detail.userplan_class_id	AND course_userplan_class.dr = 0	AND course_userplan_class.`status`) ON ( course_userplan_class_detail.userplan_detail_id = course_userplan_detail.userplan_detail_id AND course_userplan_class_detail.dr = 0) WHERE course_userplan_detail.dr = 0 AND course_userplan.user_plan_id = course_userplan_detail.userplan_id AND mall_order.order_id = course_userplan.order_id AND course_userplan_class_detail.userplan_class_detail_id IS NULL AND mall_order.dr = 0 AND course_userplan.dr = 0 AND course_userplan_detail.course_id = 445";
//		String sql1 = "select *,(select table2.a from table2) from table where x = 1 and y=y";
//		build_select_where(sql1, "a=1 and c=1");
//		String sql_where = "a=1 and c=1";
		List<Table> test_select_table1 = test_select_table1(sql);
		System.out.println(test_select_table1);
//		if(StringUtils.isNotBlank(test_select_where)){
//			sql_where = test_select_where +" AND " +sql_where;
//		}
//		build_select_where(sql1, sql_where);
//		build_select_where(sql1, "a=1 and c=1");
//		System.out.println(test_select_where(sql1));
		
//		System.out.println(getTableNames(sql1));
//		System.out.println(getTableNames("delete from posts_99 where authorid in (select authorid from posts_99 group by authorid having count(message)<3)"));
	}
	
	  private static CCJSqlParserManager pm = new CCJSqlParserManager();
	
	 /**
     *  detect table names from given table 
     *  ATTENTION : WE WILL SKIP SCALAR SUBQUERY IN PROJECTION CLAUSE 
     * */
    private static List<String> getTableNames(String sql) throws Exception {
        List<String> tablenames = null;
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        Statement statement = pm.parse(new StringReader(sql));
        if (statement instanceof Select) {
            tablenames = tablesNamesFinder.getTableList((Select) statement);
        } else if (statement instanceof Update) {
            return null;
        } else if (statement instanceof Delete) {
            return null;
        } else if (statement instanceof Replace) {
            return null;
        } else if (statement instanceof Insert) {
            return null;
        }
        return tablenames;
    }
	
	
	
//	public static void test_insert(String sql) throws JSQLParserException {
//		// ****insert table
//		String string_tablename = test_insert_table(sql);
//
//		// ********* insert table column
//		List<String> str_column = test_insert_column(sql);
//
//		// ********Insert values ExpressionList强制转换，参见InsertTest.java
//		List<String> str_values = test_insert_values(sql);
//	}
//
//	public static void test_update(String sql) throws JSQLParserException {
//
//		// *********update table name
//		List<String> str_table = test_update_table(sql);
//		// *********update column
//		List<String> str_column = test_update_column(sql);
//		// *********update values
//		List<String> str_values = test_update_values(sql);
//		// *******uodate where
//		String str_where = test_update_where(sql);
//
//	}

	// *********select body items内容
	public static List<String> test_select_items(String sql) throws JSQLParserException {
		CCJSqlParserManager parserManager = new CCJSqlParserManager();
		Select select = (Select) parserManager.parse(new StringReader(sql));
		PlainSelect plain = (PlainSelect) select.getSelectBody();
		List<SelectItem> selectitems = plain.getSelectItems();
		List<String> str_items = new ArrayList<String>();
		if (selectitems != null) {
			for (int i = 0; i < selectitems.size(); i++) {
				str_items.add(selectitems.get(i).toString());
			}
		}
		return str_items;
	}

	// **********select table
	// **********TablesNamesFinder:Find all used tables within an select
	public static List<String> test_select_table(String sql) throws JSQLParserException {
		Statement statement = (Statement) CCJSqlParserUtil.parse(sql);
		Select selectStatement = (Select) statement;
		TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
		List<String> tableList = tablesNamesFinder.getTableList(selectStatement);
		return tableList;
	}
	public static List<Table> test_select_table1(String sql) throws JSQLParserException {
		Statement statement = (Statement) CCJSqlParserUtil.parse(sql);
		Select selectStatement = (Select) statement;
		RTablesNamesFinder tablesNamesFinder = new RTablesNamesFinder();
		List<Table> tableList = tablesNamesFinder.getAliasTableList(selectStatement);
		return tableList;
	}

	// ******************* select join
	public static List<String> test_select_join(String sql) throws JSQLParserException {
		Statement statement = (Statement) CCJSqlParserUtil.parse(sql);
		Select selectStatement = (Select) statement;
		PlainSelect plain = (PlainSelect) selectStatement.getSelectBody();
		List<Join> joinList = plain.getJoins();
		List<String> tablewithjoin = new ArrayList<String>();
		if (joinList != null) {
			for (int i = 0; i < joinList.size(); i++) {
				tablewithjoin.add(joinList.get(i).toString());
				// 注意 ， leftjoin rightjoin 等等的to string()区别
			}
		}
		return tablewithjoin;
	}

	// *******select where
	public static String test_select_where(String sql) throws JSQLParserException {
		CCJSqlParserManager parserManager = new CCJSqlParserManager();
		Select select = (Select) parserManager.parse(new StringReader(sql));
		PlainSelect plain = (PlainSelect) select.getSelectBody();
		Expression where_expression = plain.getWhere();
		String str = null;
		if(null!=where_expression){
			str = where_expression.toString();
		}
		return str;
	}

	// ******select group by
	public static List<String> test_select_groupby(String sql) throws JSQLParserException {
		CCJSqlParserManager parserManager = new CCJSqlParserManager();
		Select select = (Select) parserManager.parse(new StringReader(sql));
		PlainSelect plain = (PlainSelect) select.getSelectBody();
		List<Expression> GroupByColumnReferences = plain.getGroupByColumnReferences();
		List<String> str_groupby = new ArrayList<String>();
		if (GroupByColumnReferences != null) {
			for (int i = 0; i < GroupByColumnReferences.size(); i++) {
				str_groupby.add(GroupByColumnReferences.get(i).toString());
			}
		}
		return str_groupby;
	}

	// **************select order by
	public static List<String> test_select_orderby(String sql) throws JSQLParserException {
		CCJSqlParserManager parserManager = new CCJSqlParserManager();
		Select select = (Select) parserManager.parse(new StringReader(sql));
		PlainSelect plain = (PlainSelect) select.getSelectBody();
		List<OrderByElement> OrderByElements = plain.getOrderByElements();
		List<String> str_orderby = new ArrayList<String>();
		if (OrderByElements != null) {
			for (int i = 0; i < OrderByElements.size(); i++) {
				str_orderby.add(OrderByElements.get(i).toString());
			}
		}
		return str_orderby;
	}

	// ****insert table
	public static String test_insert_table(String sql) throws JSQLParserException {
		Statement statement = CCJSqlParserUtil.parse(sql);
		Insert insertStatement = (Insert) statement;
		String string_tablename = insertStatement.getTable().getName();
		return string_tablename;
	}

	// ********* insert table column
	public static List<String> test_insert_column(String sql) throws JSQLParserException {
		Statement statement = CCJSqlParserUtil.parse(sql);
		Insert insertStatement = (Insert) statement;
		List<Column> table_column = insertStatement.getColumns();
		List<String> str_column = new ArrayList<String>();
		for (int i = 0; i < table_column.size(); i++) {
			str_column.add(table_column.get(i).toString());
		}
		return str_column;
	}

	// ********* Insert values ExpressionList
	public static List<String> test_insert_values(String sql) throws JSQLParserException {
		Statement statement = CCJSqlParserUtil.parse(sql);
		Insert insertStatement = (Insert) statement;
		List<Expression> insert_values_expression = ((ExpressionList) insertStatement.getItemsList()).getExpressions();
		List<String> str_values = new ArrayList<String>();
		for (int i = 0; i < insert_values_expression.size(); i++) {
			str_values.add(insert_values_expression.get(i).toString());
		}
		return str_values;
	}

	// *********update table name
	public static List<String> test_update_table(String sql) throws JSQLParserException {
		Statement statement = CCJSqlParserUtil.parse(sql);
		Update updateStatement = (Update) statement;
		List<Table> update_table = updateStatement.getTables();
		List<String> str_table = new ArrayList<String>();
		if (update_table != null) {
			for (int i = 0; i < update_table.size(); i++) {
				str_table.add(update_table.get(i).toString());
			}
		}
		return str_table;

	}

	// *********update column
	public static List<String> test_update_column(String sql) throws JSQLParserException {
		Statement statement = CCJSqlParserUtil.parse(sql);
		Update updateStatement = (Update) statement;
		List<Column> update_column = updateStatement.getColumns();
		List<String> str_column = new ArrayList<String>();
		if (update_column != null) {
			for (int i = 0; i < update_column.size(); i++) {
				str_column.add(update_column.get(i).toString());
			}
		}
		return str_column;

	}

	// *********update values
	public static List<String> test_update_values(String sql) throws JSQLParserException {
		Statement statement = CCJSqlParserUtil.parse(sql);
		Update updateStatement = (Update) statement;
		List<Expression> update_values = updateStatement.getExpressions();
		List<String> str_values = new ArrayList<String>();
		if (update_values != null) {
			for (int i = 0; i < update_values.size(); i++) {
				str_values.add(update_values.get(i).toString());
			}
		}
		return str_values;

	}

	// *******update where
	public static String test_update_where(String sql) throws JSQLParserException {
		Statement statement = CCJSqlParserUtil.parse(sql);
		Update updateStatement = (Update) statement;
		Expression where_expression = updateStatement.getWhere();
		String str = where_expression.toString();
		return str;
	}
	// ********************change where
	public static String build_select_where(String sql, String str_where)
			throws JSQLParserException {
//		logger.info("PrasingUtils build_select_where start sql:{}  str_where:{}" , sql , str_where);
		CCJSqlParserManager parserManager = new CCJSqlParserManager();
		Select select = (Select) parserManager.parse(new StringReader(sql));
		PlainSelect plain = (PlainSelect) select.getSelectBody();
		// parseCondExpression函数 不会被空格截断
		Expression where_expression = (Expression) (CCJSqlParserUtil.parseCondExpression(str_where));
		plain.setWhere(where_expression);
//		logger.info("PrasingUtils build_select_where end select:{}" , select);
		return select.toString();
	}
	// ********************append where
	public static String append_select_where(String sql, String str_where)
			throws JSQLParserException {
		logger.info("PrasingUtils append_select_where start sql:{}  str_where:{}" , sql , str_where);
		String test_select_where = test_select_where(sql);
		logger.info("PrasingUtils append_select_where start test_select_where:{}" , test_select_where);
		if(StringUtils.isNotBlank(test_select_where)){
			str_where = test_select_where +" AND " +str_where;
		}
		String build_select_where = build_select_where(sql, str_where);
		logger.info("PrasingUtils append_select_where end select:{}" , build_select_where);
		return build_select_where;
	}
	
	/**
	 * 获取表名
	 * @return
	 */
	protected static String getSqlTableName(Table table){
		String tableName = null;
		if(null != table){
			//表别名
			Alias alias = table.getAlias();
			if(null != alias){
				//别名
				tableName = alias.getName();
				//如果非空
				if(StringUtils.isNotBlank(tableName)){
					//去除空格
					tableName = tableName.trim();
				}
			}
			if(StringUtils.isBlank(tableName)){
				tableName = table.getName();
			}
		}
		return tableName;
	}
	
	
}
