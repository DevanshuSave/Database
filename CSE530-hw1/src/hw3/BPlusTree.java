//Devanshu Save

package hw3;

import java.util.ArrayList;
import java.util.Stack;

import hw1.Field;
import hw1.RelationalOperator;

public class BPlusTree {
	
	private int degree;
	private Node root;
	//private Node node;
	
    public BPlusTree(int degree) {
    	//your code here
    	this.degree = degree;
    	this.root = null;
    	//this.node = root;
    }
    
    public LeafNode search(Field f) {
    	//your code here
    	if (root==null) {
    		return null;
    	}
    	
    	Node node = root;
    	while(!node.isLeafNode()) {
    		int q = ((InnerNode)node).getChildren().size();
    		ArrayList<Field> keys = ((InnerNode)node).getKeys();
    		for (int i=0;i<keys.size();i++) {
    			if(f.compare(RelationalOperator.LT, keys.get(i))) {
    				node = ((InnerNode)node).getChildren().get(i);
    				break;
    			}
				if(f.compare(RelationalOperator.GT, keys.get(keys.size()-1))) {
    				node = ((InnerNode)node).getChildren().get(q-1);
    				break;
    			}
    		}
    	}
		ArrayList<Entry> entries = ((LeafNode)node).getEntries();
		for (Entry entry : entries) {
			if(f.compare(RelationalOperator.EQ, entry.getField())) {
				return (LeafNode)node;
			}
		}
    	return null;
    }
    
    
    //Method created by Devanshu
    private LeafNode getLeaf(Field f) {
    	Node node = root;
    	if (node==null) {
    		return null;
    	}
    	while(!node.isLeafNode()) {
			//find LeafNode to search from
			ArrayList<Field> keys = ((InnerNode)node).getKeys();
			int j=0;
			for (j = 0; j<keys.size();j++) {
				if(f.compare(RelationalOperator.LT, keys.get(j))) {
					node = (((InnerNode)node).getChildren()).get(j);
					break;
				}
			}
			if(j==keys.size()) {
				node = (((InnerNode)node).getChildren()).get(keys.size()-1);
			}
		}
    	return (LeafNode)node;
    }
    
    private ArrayList<Entry> addAndSort(ArrayList<Entry> myList, Entry entry) {
    	for(Entry e : myList) {
    		if(entry.getField().compare(RelationalOperator.LT, e.getField())) {
    			myList.add(myList.indexOf(e),entry);
    			break;
    		}
    	}
    	myList.add(entry);
    	return myList;
    }
    
    public void insert(Entry e) {
    	//your code here
       	
    	//First insertion
    	if (getRoot()==null) {
    		setRoot(new LeafNode(getDegree()));
    		ArrayList<Entry> entries = new ArrayList<>();
    		entries.add(e);
    		((LeafNode)root).setEntries(entries);
    		return;
    	}
    	//Root with less than max entries
    	if(getRoot().isLeafNode()) {
    		if(((LeafNode)getRoot()).getEntries().size()<getDegree()) {
    			((LeafNode)getRoot()).setEntries(addAndSort(((LeafNode)getRoot()).getEntries(), e));
    			return;
    		}
    	}
    	
    	Node node = getRoot();
    	Stack<Node> stack = new Stack<Node>();
    	while(!node.isLeafNode()) {
    		stack.push(node);
    		int q = ((InnerNode)node).getChildren().size();
    		ArrayList<Field> keys = ((InnerNode)node).getKeys();
    		for (int i=0;i<keys.size();i++) {
    			if(e.getField().compare(RelationalOperator.LT, keys.get(i))) {
    				node = ((InnerNode)node).getChildren().get(i);
    				break;
    			}
				if(e.getField().compare(RelationalOperator.GT, keys.get(keys.size()-1))) {
    				node = ((InnerNode)node).getChildren().get(q-1);
    				break;
    			}
    		}
    	}
    	ArrayList<Entry> entries = ((LeafNode)node).getEntries();
		for (Entry entry : entries) {
			if(e.getField().compare(RelationalOperator.EQ, entry.getField())) {
				System.out.println("Record already exists");
				return;
			}
		}
		//Here node is always a LeafNode
		//LeafNode is not full
    	if (((LeafNode)node).getEntries().size()<((LeafNode)node).getDegree()) {
    		entries = ((LeafNode)node).getEntries();
    		int i;
    		for(i=0;i<entries.size();i++) {
    			if(e.getField().compare(RelationalOperator.LT, entries.get(i).getField())) {
    				entries.add(i, e);
    				break;
    			}
    		}
    		if(i==entries.size()) {
    			entries.add(e);
    		}
    		((LeafNode)node).setEntries(entries);
    	}
    	else {
    		//split LeafNode
    		LeafNode lf = new LeafNode(getDegree()+1);
    		//lf.setEntries(((LeafNode)node).getEntries());
    		entries = ((LeafNode)node).getEntries();
    		int i;
    		for(i = 0;i<entries.size();i++) {
    			if(e.getField().compare(RelationalOperator.LT, entries.get(i).getField())) {
    				entries.add(i, e);
    				break;
    			}
    		}
    		if(i==entries.size()) {
    			entries.add(e);
    		}
    		ArrayList<Entry> en = new ArrayList<>();
    		ArrayList<Entry> en1 = new ArrayList<>();
    		
    		for (i=0;i<entries.size();i++) {
    			if(i<(entries.size()+1)/2) {
    				en.add(entries.get(i));
    			}
    			else {
    				en1.add(entries.get(i));
    			}
    		}
    		
    		((LeafNode)node).setEntries(en);
    		((LeafNode)lf).setEntries(en1);
    		
    		boolean finished  = false;
    		//There is no Parent
    		if(stack.isEmpty()) {
    			root = new InnerNode(getDegree());
    			ArrayList<Node> arrayList = new ArrayList<>();
        		arrayList.add(((LeafNode)node));
        		arrayList.add(lf);
    			((InnerNode)root).setChildren(arrayList);
    			ArrayList<Field> keyList = new ArrayList<>();
    			keyList.add(en.get(en.size()-1).getField());
    			((InnerNode)root).setKeys(keyList);
    		}
    		//Parent node exists
    		else {
	    		while(!stack.isEmpty()) {
	    			node = stack.pop();
	    			//Parent node is not full
	    			if(((InnerNode)node).getChildren().size()<((InnerNode)node).getDegree()) {
	    				ArrayList<Field> keys = ((InnerNode)node).getKeys();
	    	    		for(i = 0;i<keys.size();i++) {
	    	    			if(en1.get(0).getField().compare(RelationalOperator.LT, keys.get(i))) {
	    	    				keys.add(i, en.get(en.size()-1).getField());
	    	    				break;
	    	    			}
	    	    		}
	    	    		((InnerNode)node).setKeys(keys);
	    	    		finished = true;
	    			}
	    			//Parent Node is full
	    			else {
	    				//while(((InnerNode)node).getChildren().size()==((InnerNode)node).getDegree()) {
    					InnerNode temp = new InnerNode(node.getDegree()+1);
    					temp.setChildren(((InnerNode)node).getChildren());
    					temp.setKeys(((InnerNode)node).getKeys());
    					ArrayList<Field> arrayList = temp.getKeys();
    					for(Field field : arrayList) {
    						if(field.compare(RelationalOperator.LT, en.get(en.size()-1).getField())){
    							arrayList.add(en.get(en.size()-1).getField());
    							break;
    						}
    					}
    					if(!arrayList.contains(en.get(en.size()-1).getField())) {
    						arrayList.add(en.get(en.size()-1).getField());
    					}
    					temp.setKeys(arrayList);
	    				//}
    		    		ArrayList<Entry> en2 = new ArrayList<>();
    		    		ArrayList<Entry> en3 = new ArrayList<>();
    		    		/*
    		    		for (i=0;i<arrayList.size();i++) {
    		    			if(i<(arrayList.size()+1)/2) {
    		    				en2.add(arrayList.get(i));
    		    			}
    		    			else {
    		    				en3.add(arrayList.get(i));
    		    			}
    		    		}
    		    		
    		    		((InnerNode)node).setEntries(en2);
    		    		((LeafNode)lf).setEntries(en3);
    		    		*/
	    			}
	    		}
    		}
    	}
    }
    
    public void delete(Entry e) {
    	//your code here
    	LeafNode leafNode = search(e.getField());
    	if(leafNode!=null) {
    		System.out.println("here");
    	}
    	ArrayList<Entry> entries = leafNode.getEntries();
    	for (int i=0;i<entries.size();i++) {
    		if(e.getField()==entries.get(i).getField()) {
    			entries.remove(i);
    		}
    	}
    	leafNode.setEntries(entries);
    }
    
    public Node getRoot() {
    	//your code here
    	return this.root;
    }
    
    //Method added by Devanshu
    public void setRoot(Node root) {
    	//your code here
    	this.root = root;
    }
    
    //Method added by Devanshu    
    public int getDegree() {
    	//your code here
    	return this.degree;
    }
    
    //Method added by Devanshu
    public void setDegree(int degree) {
    	//your code here
    	this.degree = degree;
    }
}
