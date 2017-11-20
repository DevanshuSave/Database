//Devanshu Save
package hw1;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a tuple that will contain a single row's worth of information
 * from a table. It also includes information about where it is stored
 * @author Sam Madden modified by Doug Shook
 *
 */
public class Tuple {
	
	/**
	 * Creates a new tuple with the given description
	 * @param t the schema for this tuple
	 */
	private TupleDesc desc;
	private int pid;
	private int id;
	private Map<Integer, Field> field = new HashMap<Integer, Field>();
	
	public Tuple(TupleDesc t) {
		//your code here
		this.desc = t;
		this.pid = 0;
		/*for(int i = 0;i<t.numFields();i++) {
			tuples[i]=t.getType(i).toString();
		}
		*/
	}
	
	public TupleDesc getDesc() {
		//your code here
		return this.desc;
	}
	
	/**
	 * retrieves the page id where this tuple is stored
	 * @return the page id of this tuple
	 */
	public int getPid() {
		//your code here
		return this.pid;
	}

	public void setPid(int pid) {
		//your code here
		this.pid = pid;
	}

	/**
	 * retrieves the tuple (slot) id of this tuple
	 * @return the slot where this tuple is stored
	 */
	public int getId() {
		//your code here
		return this.id;
	}

	public void setId(int id) {
		//your code here
		this.id = id;
	}
	
	public void setDesc(TupleDesc td) {
		//your code here;
		this.desc = td;
	}
	
	/**
	 * Stores the given data at the i-th field
	 * @param i the field number to store the data
	 * @param v the data
	 */
	public void setField(int i, Field v) {
		//your code here
		this.field.put(i, v);
	}
	
	public Field getField(int i) {
		//your code here
		return this.field.get(i);
	}
	
	/**
	 * Creates a string representation of this tuple that displays its contents.
	 * You should convert the binary data into a readable format (i.e. display the ints in base-10 and convert
	 * the String columns to readable text).
	 */
	public String toString() {
		//your code here
		StringBuilder s = new StringBuilder();
		for(int i = 0;i < this.field.size();i++) {
			s.append(this.getField(i).toString());
		}
		return s.toString();
	}
	
	//New method Added for HW4
	public boolean equals(Object o) {
		
		if(o==null) {
			return false;
		}
		Tuple t = (Tuple)o;
		if(!t.desc.equals(this.getDesc())) {
			return false;
		}
		if(t.getId()!=this.getId()) {
			return false;
		}
		if(t.getPid()!=this.getPid()) {
			return false;
		}
		if(!t.field.equals(this.field)){
			return false;
		}
		return true;
	}
}