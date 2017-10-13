//Devanshu Save
package hw1;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

public class Query {

	private String q;
	
	public Query(String q) {
		this.q = q;
	}
	
	public Relation execute()  {
		Statement statement = null;
		try {
			statement = CCJSqlParserUtil.parse(q);
		} catch (JSQLParserException e) {
			System.out.println("Unable to parse query");
			e.printStackTrace();
		}
		Select selectStatement = (Select) statement;
		PlainSelect sb = (PlainSelect)selectStatement.getSelectBody();
		//your code here
		
		((SelectExpressionItem) sb.getSelectItems().get(0)).getAlias();
		List<SelectItem> items = sb.getSelectItems();
		
		
		FromItem fromItem = sb.getFromItem();
		
		Catalog c = Database.getCatalog();
		int tab = c.getTableId(fromItem.toString());
		ArrayList<Tuple> myTuples = c.getDbFile(tab).getAllTuples();
		TupleDesc td = myTuples.get(0).getDesc();
		
		ArrayList<Integer> projectItems = new ArrayList<>();
		for(SelectItem i : items) {
			ColumnVisitor columnVisitor = new ColumnVisitor();
			i.accept(columnVisitor);
			projectItems.add(td.nameToId(columnVisitor.getColumn()));
		}
		
		Relation r = new Relation(myTuples,td);
		//if(r.getDesc().equals(td)) {
		r.project(projectItems);
		//}
		
		WhereExpressionVisitor whereExpressionVisitor = new WhereExpressionVisitor();
		//PlainSelect sb2 = (PlainSelect)sb.getWhere();
		if(sb.getWhere()!=null) {
			sb.getWhere().accept(whereExpressionVisitor);
			r=r.select(td.nameToId(whereExpressionVisitor.getLeft()), whereExpressionVisitor.getOp(), whereExpressionVisitor.getRight());
		}
		System.out.println("xxxxxx"+r.toString());
		return r;
		
	}
}
