//Devanshu Save
package hw1;

import java.util.ArrayList;
import java.util.Arrays;
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
		
		List<SelectItem> items = sb.getSelectItems();
		System.out.println("xxxx"+items);
		
		
		FromItem fromItem = sb.getFromItem();
		Catalog c = Database.getCatalog();
		int tab = c.getTableId(fromItem.toString());
		ArrayList<Tuple> myTuples = c.getDbFile(tab).getAllTuples();
		TupleDesc td = myTuples.get(0).getDesc();
		Relation r = new Relation(myTuples,td);
		
		
		System.out.println("joins:"+sb.getJoins());
		List<Join> myJoins= sb.getJoins();
		if(myJoins!=null) {
			for(Join j : myJoins) {
				FromItem temp = j.getRightItem();
				Catalog c1 = Database.getCatalog();
				int tab1 = c1.getTableId(temp.toString());
				ArrayList<Tuple> myTuples1 = c1.getDbFile(tab1).getAllTuples();
				TupleDesc td1 = myTuples1.get(0).getDesc();
				Relation r1 = new Relation(myTuples1,td1);
				String myS[] = j.getOnExpression().toString().split(" = ");
				String myS2[] = myS[0].split("\\.");
				String myS3[] = myS[1].split("\\.");
				int f1=0;
				int f2=0;
				if(c.getTableId(myS2[0].trim())==tab) {
					String[] ff = td.getFields();
					for (int i =0;i<ff.length;i++) {
						if(ff[i].equalsIgnoreCase(myS2[1].trim())) {
							f1=i;
						}
					}
					
					String[] ff1 = td1.getFields();
					for (int i=0;i<ff1.length;i++) {
						if(ff1[i].equalsIgnoreCase(myS3[1].trim())) {
							f2=i;
						}
					}
				}
				else {
					String[] ff = td1.getFields();
					for (int i =0;i<ff.length;i++) {
						if(ff[i].equals(myS2[1].trim())) {
							f2=i;
						}
					}
					
					String[] ff1 = td.getFields();
					for (int i=0;i<ff1.length;i++) {
						if(ff1[i].equals(myS3[1].trim())) {
							f1=i;
						}
					}
				}
				r = r.join(r1, f1, f2);
			}
		}
		ArrayList<Integer> projectItems = new ArrayList<>();
		if(!items.get(0).toString().equals("*")) {
			for(SelectItem i : items) {
				ColumnVisitor columnVisitor = new ColumnVisitor();
				i.accept(columnVisitor);
				projectItems.add(td.nameToId(columnVisitor.getColumn()));
			}
			r.project(projectItems);
		}
		
		WhereExpressionVisitor whereExpressionVisitor = new WhereExpressionVisitor();
		//PlainSelect sb2 = (PlainSelect)sb.getWhere();
		if(sb.getWhere()!=null) {
			sb.getWhere().accept(whereExpressionVisitor);
			r=r.select(td.nameToId(whereExpressionVisitor.getLeft()), whereExpressionVisitor.getOp(), whereExpressionVisitor.getRight());
		}
		
		//System.out.println("xxxxxx"+r.toString());
		return r;
		
	}
}
