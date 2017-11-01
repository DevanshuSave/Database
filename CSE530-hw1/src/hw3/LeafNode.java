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

	public int getDegree() {
		//your code here
		return this.degree;
	}
	
	public boolean isLeafNode() {
		return true;
	}

}