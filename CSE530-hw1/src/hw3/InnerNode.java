package hw3;

import java.util.ArrayList;

import hw1.Field;

public class InnerNode implements Node {
	
	//Create variables
	int degree;
	ArrayList<Node> children;
	ArrayList<Field> keys;
	
	public InnerNode(int degree) {
		//your code here
		this.degree = degree;
		this.children = new ArrayList<Node>(degree+1);
		this.keys = new ArrayList<Field>(degree);
	}
	
	public ArrayList<Field> getKeys() {
		//your code here
		return this.keys;
	}
	
	public ArrayList<Node> getChildren() {
		//your code here
		return this.children;
	}

	public int getDegree() {
		//your code here
		return this.degree;
	}
	
	public boolean isLeafNode() {
		return false;
	}

}