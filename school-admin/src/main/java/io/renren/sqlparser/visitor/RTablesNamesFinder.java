package io.renren.sqlparser.visitor;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.util.TablesNamesFinder;

/**
 * 解析类
 * @class io.renren.sqlparser.visitor.RTablesNamesFinder.java
 * @Description:
 * @author shihongjie
 * @dete 2017年8月3日
 */
public class RTablesNamesFinder extends TablesNamesFinder {
	/** 表 */
	private List<Table> aliasTables;
	
    public List<Table> getAliasTableList(Statement statement) {
        super.init();
        aliasTables = new ArrayList<>();
        statement.accept(this);
        return aliasTables;
    }
    /**
     * 重新解析from
     */
    @Override
    public void visit(Table table) {
    	super.visit(table);
    	aliasTables.add(table);
    }
    
//    select 
    @Override
    public void visit(SelectExpressionItem item) {
//        item.getExpression().accept(this);
    }
    //where
    @Override
    public void visit(SubSelect subSelect) {
//        if (subSelect.getWithItemsList() != null) {
//            for (WithItem withItem : subSelect.getWithItemsList()) {
//                withItem.accept(this);
//            }
//        }
//        subSelect.getSelectBody().accept(this);
    }

    @Override
    public void visit(PlainSelect plainSelect){
    	 if (plainSelect.getFromItem() != null) {
             plainSelect.getFromItem().accept(this);
         }
    }
    
//    @Override
//    public void visit(Table tableName) {
//        String tableWholeName = tableName.getFullyQualifiedName();
//        if (!otherItemNames.contains(tableWholeName.toLowerCase())
//                && !tables.contains(tableWholeName)) {
//            tables.add(tableWholeName);
//        }
//    }
}
