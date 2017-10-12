//Devanshu Save
package hw1;

import java.util.ArrayList;

/**
 * This class provides methods to perform relational algebra operations. It will be used
 * to implement SQL queries.
 * @author Doug Shook
 *
 */
public class Relation {

	private ArrayList<Tuple> tuples;
	private TupleDesc td;
	
	public Relation(ArrayList<Tuple> l, TupleDesc td) {
		//your code here
		this.td = td;
		this.tuples = l;
	}
	
	/**
	 * This method performs a select operation on a relation
	 * @param field number (refer to TupleDesc) of the field to be compared, left side of comparison
	 * @param op the comparison operator
	 * @param operand a constant to be compared against the given column
	 * @return
	 */
	public Relation select(int field, RelationalOperator op, Field operand) {
		//your code here
		if(getTuples().isEmpty()) {
			return new Relation(new ArrayList<Tuple>(),getDesc());
		}
		Relation r = new Relation(new ArrayList<Tuple>(),getDesc());
		for (Tuple tup :  getTuples()) {
			if(tup.getField(field).compare(op, operand)) {
				r.tuples.add(tup);
			}
		}
		return r;
	}
	
	/**
	 * This method performs a rename operation on a relation
	 * @param fields the field numbers (refer to TupleDesc) of the fields to be renamed
	 * @param names a list of new names. The order of these names is the same as the order of field numbers in the field list
	 * @return
	 */
	public Relation rename(ArrayList<Integer> fields, ArrayList<String> names) {
		//your code here
		Relation r = this;
		String s[] = r.td.getFields();
		int j = 0;
		for (Integer i : fields) {
			s[i]=names.get(j);
			j++;
		}
		r.td.setFields(s);
		return r;
	}
	
	/**
	 * This method performs a project operation on a relation
	 * @param fields a list of field numbers (refer to TupleDesc) that should be in the result
	 * @return
	 */
	public Relation project(ArrayList<Integer> fields) {
		//your code here
		String s[] = new String[fields.size()];
		Type t[] = new Type[fields.size()];
		TupleDesc tupleDesc = new TupleDesc(t, s);
		for(int i =0;i<fields.size();i++) {
			s[i]=td.getFieldName(fields.get(i));
			t[i]=td.getType(fields.get(i));
		}
		tupleDesc.setFields(s);
		tupleDesc.setTypes(t);
		
		Relation r = new Relation(new ArrayList<Tuple>(), tupleDesc);
		for(int i = 0;i<fields.size();i++) {
			for (Tuple myTup : getTuples()) {
				Tuple tup = new Tuple(tupleDesc);
				tup.setField(i, myTup.getField(fields.get(i)));
				r.getTuples().add(tup);
			}
		}
		return r;
	}
	
	/**
	 * This method performs a join between this relation and a second relation.
	 * The resulting relation will contain all of the columns from both of the given relations,
	 * joined using the equality operator (=)
	 * @param other the relation to be joined
	 * @param field1 the field number (refer to TupleDesc) from this relation to be used in the join condition
	 * @param field2 the field number (refer to TupleDesc) from other to be used in the join condition
	 * @return
	 */
	public Relation join(Relation other, int field1, int field2) {
		//your code here
		
		//Set the TupleDesc
		String s[] = new String[getDesc().numFields()+other.getDesc().numFields()];
		Type t[] = new Type[getDesc().numFields()+other.getDesc().numFields()];
		TupleDesc tupleDesc = new TupleDesc(t, s);
		
		for(int i=0;i<s.length;i++) {
			if(i<getDesc().numFields()) {
				s[i]=getDesc().getFieldName(i);
				t[i]=getDesc().getType(i);
			}
			else {
				s[i]=other.getDesc().getFieldName(i-getDesc().numFields());
				t[i]=other.getDesc().getType(i-getDesc().numFields());
			}
		}
		tupleDesc.setFields(s);
		tupleDesc.setTypes(t);
		
		Relation r = new Relation(new ArrayList<Tuple>(), tupleDesc);
			
		for (Tuple tuple :  this.getTuples()) {
			for(Tuple tuple2 : other.getTuples()) {
				if(tuple.getField(field1).compare(RelationalOperator.EQ, tuple2.getField(field2))) {
					Tuple tup = new Tuple(tupleDesc);
					for(int i = 0;i<s.length;i++) {
						tup.setField(i, tuple.getField(i));
						tup.setField(i+td.numFields(), tuple2.getField(i));
					}
					r.getTuples().add(tup);
				}
			}
		}
		return r;
	}
	
	/**
	 * Performs an aggregation operation on a relation. See the lab write up for details.
	 * @param op the aggregation operation to be performed
	 * @param groupBy whether or not a grouping should be performed
	 * @return
	 */
	public Relation aggregate(AggregateOperator op, boolean groupBy) {
		//your code here
		Relation r = new Relation(new ArrayList<>(), getDesc());
		TupleDesc myDesc = getDesc();
		if(groupBy) {
			if(op==AggregateOperator.COUNT) {
				myDesc.setTypes(new Type[] {myDesc.getType(0),Type.INT});
			}
		}
		else {
			if(op==AggregateOperator.COUNT) {
				myDesc.setTypes(new Type[] {Type.INT});
			}
		}
		
		Aggregator aggregator = new Aggregator(op, groupBy, myDesc);
		for (Tuple tuple : getTuples()) {
			aggregator.merge(tuple);
		}
		r.tuples = aggregator.getResults();
		return r;
		/*
		if(getDesc().getType(0)==Type.INT) {
			Tuple t = new Tuple(getDesc());
			switch (op) {
	        case MAX:
	        	int max = ((IntField) tuples.get(0).getField(0)).getValue();
	        	for (Tuple tuple : getTuples()) {
	        		if(((IntField) tuple.getField(0)).getValue()>max) {
	        			max = ((IntField) tuple.getField(0)).getValue();
	        			t=tuple;
	        		}
				}
	            r.tuples.add(t);
	            break;
	            
	        case MIN:
	        	int min = ((IntField) tuples.get(0).getField(0)).getValue();
	        	for (Tuple tuple : getTuples()) {
	        		if(((IntField) tuple.getField(0)).getValue()<min) {
	        			min = ((IntField) tuple.getField(0)).getValue();
	        			t=tuple;
	        		}
				}
	            r.tuples.add(t);
	            break;
	            
	        case SUM:
	        	int sum = 0;
	        	for (Tuple tuple : getTuples()) {
	        		sum += ((IntField) tuple.getField(0)).getValue();
	        	}
	        	t.setField(0, new IntField(sum));
	            r.tuples.add(t);
	            break;

	        case AVG:
	        	int avg = 0;
	        	for (Tuple tuple : getTuples()) {
	        		avg += ((IntField) tuple.getField(0)).getValue();
	        	}
	        	t.setField(0, new IntField(avg/tuples.size()));
	            r.tuples.add(t);
	            break;

	        case COUNT:
	        	t.setField(0, new IntField(tuples.size()));
	            r.tuples.add(t);
	            break;
			}
		}
		if(getDesc().getType(0)==Type.STRING) {
			Tuple t = new Tuple(getDesc());
			switch (op) {
	        case MAX:
	        	String max = ((StringField) tuples.get(0).getField(0)).getValue();
	        	for (Tuple tuple : getTuples()) {
	        		if((((StringField) tuple.getField(0)).getValue()).compareTo(max)==1) {
	        			max = ((StringField) tuple.getField(0)).getValue();
	        			t=tuple;
	        		}
				}
	            r.tuples.add(t);
	            break;
	            
	        case MIN:
	        	String min = ((StringField) tuples.get(0).getField(0)).getValue();
	        	for (Tuple tuple : getTuples()) {
	        		if((((StringField) tuple.getField(0)).getValue()).compareTo(min)==-1) {
	        			min = ((StringField) tuple.getField(0)).getValue();
	        			t=tuple;
	        		}
				}
	            r.tuples.add(t);
	            break;
	            
	        case COUNT:
	        	t.setField(0, new StringField(Integer.toString(tuples.size())));
	            r.tuples.add(t);
	            break;
			case AVG:
				break;
			case SUM:
				break;
			}
		}
		
		return r;
		*/
	}
	
	public TupleDesc getDesc() {
		//your code here
		return this.td;
	}
	
	public ArrayList<Tuple> getTuples() {
		//your code here
		return this.tuples;
	}
	
	/**
	 * Returns a string representation of this relation. The string representation should
	 * first contain the TupleDesc, followed by each of the tuples in this relation
	 */
	public String toString() {
		//your code here
		StringBuilder s = new StringBuilder();
		TupleDesc desc = getDesc();
    	for(int i =0;i<desc.numFields();i++) {
    		s.append(desc.getFields()[i]+"("+desc.getType(i)+")");
    	}
    	for (Tuple tuple : getTuples()) {
    		for(int i = 0;i<desc.numFields();i++){
    			s.append(tuple.getField(i).toString());
			}
    		s.append("\n");
		}
    	return s.toString();
	}
}
