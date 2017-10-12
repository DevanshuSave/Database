//Devanshu Save
package hw1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A class to perform various aggregations, by accepting one tuple at a time
 * @author Doug Shook
 *
 */
public class Aggregator {
	
	private  AggregateOperator aggregateOperator;
	private boolean group;
	private TupleDesc tupleDesc;
	private Map tuples;

	public Aggregator(AggregateOperator o, boolean groupBy, TupleDesc td) {
		//your code here
		this.aggregateOperator = o;
		this.group = groupBy;
		this.tupleDesc = td;
		if(groupBy) {
			if(td.getType(0)==Type.INT) {
				tuples = new HashMap<Integer,Integer>();
			}
			else {
				tuples = new HashMap<String,Integer>();
			}
		}
		else {
			if(td.getType(0)==Type.INT) {
				tuples = new HashMap<Integer,Integer>();
			}
			else {
				if(o==AggregateOperator.COUNT) {
					tuples = new HashMap<String,Integer>();
				}
				tuples = new HashMap<String,String>();
			}
		}
	}

	/**
	 * Merges the given tuple into the current aggregation
	 * @param t the tuple to be aggregated
	 */
	public void merge(Tuple t) {
		//your code here
		if(!group) {
			if(tupleDesc.getType(0)==Type.INT) {
				switch (aggregateOperator) {
		        case MAX:
		        	if(tuples.get(0)!=null){
		        		if((int)tuples.get(0)<((IntField)t.getField(0)).getValue()) {
		        			tuples.put(0, ((IntField)t.getField(0)).getValue());
		        		}
		        	}
		        	else {
		        		tuples.put(0, ((IntField)t.getField(0)).getValue());
		        	}
		            break;
		            
		        case MIN:
		        	if(tuples.get(0)!=null){
		        		if((int)tuples.get(0)>((IntField)t.getField(0)).getValue()) {
		        			tuples.put(0, ((IntField)t.getField(0)).getValue());
		        		}
		        	}
		        	else {
		        		tuples.put(0, ((IntField)t.getField(0)).getValue());
		        	}
		            break;
		            
		        case SUM:
		        	if(tuples.get(0)!=null){
		        		tuples.put(0,(int)tuples.get(0)+((IntField)t.getField(0)).getValue());
		        	}
		        	else {
		        		tuples.put(0, ((IntField)t.getField(0)).getValue());
		        	}
		            break;

		        case AVG:
		        	if(tuples.get(0)!=null){
		        		tuples.put(tuples.size(),((IntField)t.getField(0)).getValue());
		        		tuples.put(0, ((tuples.size()-1*((int)tuples.get(0)))+(((IntField)t.getField(0)).getValue()))/tuples.size());
		        	}
		        	else {
		        		tuples.put(0, ((IntField)t.getField(0)).getValue());
		        	}
		            break;

		        case COUNT:
		        	if(tuples.get(0)!=null){
		        		tuples.put(0,(int)tuples.get(0)+1);
		        	}
		        	else {
		        		tuples.put(0, 1);
		        	}
		            break;
				}
			}
			if(tupleDesc.getType(0)==Type.STRING) {
				switch (aggregateOperator) {
		        case MAX:
		        	if(tuples.get(0)!=null){
		        		if(((StringField)t.getField(0)).getValue().compareTo(tuples.get(0).toString())==1) {
		        			tuples.put(0, ((StringField)t.getField(0)).getValue());
		        		}
		        	}
		        	else {
		        		tuples.put(0, ((StringField)t.getField(0)).getValue());
		        	}
		            break;
		            
		        case MIN:
		        	if(tuples.get(0)!=null){
		        		if(((StringField)t.getField(0)).getValue().compareTo(tuples.get(0).toString())==-1) {
		        			tuples.put(0, ((StringField)t.getField(0)).getValue());
		        		}
		        	}
		        	else {
		        		tuples.put(0, ((StringField)t.getField(0)).getValue());
		        	}
		            break;
		            
		        case COUNT:
		        	if(tuples.get(0)!=null){
		        		tuples.put(0,(int)tuples.get(0)+1);
		        	}
		        	else {
		        		tuples.put(0, 1);
		        	}
		            break;
				case AVG:
					break;
				case SUM:
					break;
				}
			}
		}
		//GroupBy functionality
		else {
			if(tupleDesc.getType(1)==Type.INT) {
				switch (aggregateOperator) {
		        case MAX:
		        	if(tuples.get(t.getField(0))!=null){
		        		if((int)tuples.get(t.getField(0))<((IntField)t.getField(1)).getValue()) {
		        			tuples.put(t.getField(0), ((IntField)t.getField(1)).getValue());
		        		}
		        	}
		        	else {
		        		tuples.put(t.getField(0), ((IntField)t.getField(1)).getValue());
		        	}
		            break;
		            
		        case MIN:
		        	if(tuples.get(t.getField(0))!=null){
		        		if((int)tuples.get(t.getField(0))>((IntField)t.getField(1)).getValue()) {
		        			tuples.put(t.getField(0), ((IntField)t.getField(1)).getValue());
		        		}
		        	}
		        	else {
		        		tuples.put(t.getField(0), ((IntField)t.getField(1)).getValue());
		        	}
		            break;
		            
		        case SUM:
		        	if(tuples.get(t.getField(0))!=null){
		        		tuples.put(t.getField(0),(int)tuples.get(t.getField(0))+((IntField)t.getField(1)).getValue());
		        	}
		        	else {
		        		tuples.put(t.getField(0), ((IntField)t.getField(1)).getValue());
		        	}
		            break;

		        case AVG:
		        	if(tuples.get(t.getField(0))!=null){
		        		tuples.put(t.getField(0),((IntField)t.getField(1)).getValue());
		        		tuples.put(t.getField(1), ((tuples.size()-1*((int)tuples.get(1)))+(((IntField)t.getField(1)).getValue()))/tuples.size());
		        	}
		        	else {
		        		tuples.put(t.getField(0), ((IntField)t.getField(0)).getValue());
		        	}
		            break;

		        case COUNT:
		        	if(tuples.get(t.getField(0))!=null){
		        		tuples.put(t.getField(0),(int)tuples.get(t.getField(0))+1);
		        	}
		        	else {
		        		tuples.put(t.getField(0), 1);
		        	}
		            break;
				}
			}
			if(tupleDesc.getType(1)==Type.STRING) {
				switch (aggregateOperator) {
		        case MAX:
		        	if(tuples.get(t.getField(0))!=null){
		        		if(((StringField)t.getField(0)).getValue().compareTo(tuples.get(t.getField(0)).toString())==1) {
		        			tuples.put(t.getField(0), ((StringField)t.getField(0)).getValue());
		        		}
		        	}
		        	else {
		        		tuples.put(t.getField(0), ((StringField)t.getField(0)).getValue());
		        	}
		            break;
		            
		        case MIN:
		        	if(tuples.get(t.getField(0))!=null){
		        		if(((StringField)t.getField(0)).getValue().compareTo(tuples.get(t.getField(0)).toString())==-1) {
		        			tuples.put(t.getField(0), ((StringField)t.getField(0)).getValue());
		        		}
		        	}
		        	else {
		        		tuples.put(t.getField(0), ((StringField)t.getField(0)).getValue());
		        	}
		            break;
		            
		        case COUNT:
		        	if(tuples.get(t.getField(0))!=null){
		        		tuples.put(t.getField(0),(int)tuples.get(t.getField(0))+1);
		        	}
		        	else {
		        		tuples.put(t.getField(0), 1);
		        	}
		            break;
				case AVG:
					break;
				case SUM:
					break;
				}
			}
		}
	}
	/**
	 * Returns the result of the aggregation
	 * @return a list containing the tuples after aggregation
	 */
	public ArrayList<Tuple> getResults() {
		//your code here
		ArrayList<Tuple> myTuples = new ArrayList<Tuple>();
		if(!group) {
			for(int i=0;i<tuples.size();i++) {
				Tuple tp = new Tuple(tupleDesc);
				if(tupleDesc.getType(0)==Type.INT) {
					tp.setField(i, new IntField((int)tuples.get(i)));
				}
				else {
					tp.setField(i, new StringField(tuples.get(i).toString()));
				}
				myTuples.add(tp);
			}
		}
		else {
			Iterator it = tuples.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        it.remove();

				Tuple tp = new Tuple(tupleDesc);
				if(tupleDesc.getType(0)==Type.INT) {
					tp.setField(0, new IntField((int)pair.getValue()));
				}
				else {
					tp.setField(0, new StringField(pair.getValue().toString()));
				}
				if(tupleDesc.getType(1)==Type.INT) {
					tp.setField(1, new IntField((int)pair.getValue()));
				}
				else {
					tp.setField(1, new StringField(pair.getValue().toString()));
				}
				myTuples.add(tp);
			}
		}
		return myTuples;
	}

}
