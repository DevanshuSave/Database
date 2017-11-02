//Devanshu Save

package hw3;

import java.util.ArrayList;

public class LeafNode implements Node {
	
	int degree;
	ArrayList<Entry> entries;
	
	public LeafNode(int degree) {
		//your code here
		this.degree=degree;
		this.entries = new ArrayList<Entry>(degree);
	}
	
	public ArrayList<Entry> getEntries() {
		//your code here
		return this.entries;
	}
	
	//Method added by Devanshu
	public void setEntries(ArrayList<Entry> arrayList) {
		this.entries = arrayList;
	}
	
	public int getDegree() {
		//your code here
		return this.degree;
	}
	
	//Method added by Devanshu
	public void setDegree(int deg) {
		this.degree = deg;
	}
	
	public boolean isLeafNode() {
		return true;
	}

}