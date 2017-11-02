//Devanshu Save

package hw3;

import java.util.ArrayList;

import hw1.Field;
import hw1.RelationalOperator;

public class BPlusTree {
	
	private int degree;
	private Node root;
	private Node node;
	
    public BPlusTree(int degree) {
    	//your code here
    	this.degree = degree;
    	this.root = null;
    	this.node = root;
    }
    
    public LeafNode search(Field f) {
    	//your code here
    	if (root==null) {
    		return null;
    	}
		node = getLeaf(f);
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
    	node = root;
    	if (node==null) {
    		return null;
    	}
    	while(!node.isLeafNode()) {
			/*
			ArrayList<Node> nodes = ((InnerNode)node).getChildren();
			if(nodes.get(0).isLeafNode()) {
				
				for(int i=0;i<nodes.size();i++) {
					ArrayList<Entry> entries = ((LeafNode)nodes.get(i)).getEntries();
					for (Entry entry : entries) {
						if(f.compare(RelationalOperator.EQ, entry.getField())) {
							return (LeafNode)node;
						}
					}
				}
			}
			*/
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
    
    public void insert(Entry e) {
    	//your code here
    	LeafNode leafNode;
    	if (root!=null) {
    		leafNode = getLeaf(e.getField());
    	}
    	else {
    		leafNode = new LeafNode(getDegree());
    	}
    	
    	if (leafNode.getEntries().size()==leafNode.getDegree()) {
    		//split
    		//
    		LeafNode lf = new LeafNode(getDegree());
    	}
    	else {
    		ArrayList<Entry> entries = leafNode.getEntries();
    		for(int i = 0;i<entries.size();i++) {
    			if(e.getField().compare(RelationalOperator.LT, entries.get(i).getField())) {
    				entries.add(i, e);
    				break;
    			}
    		}
    		entries.add(e);
    		leafNode.setEntries(entries);
    		setRoot(leafNode);
    	}
    }
    
    public void delete(Entry e) {
    	//your code here
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
