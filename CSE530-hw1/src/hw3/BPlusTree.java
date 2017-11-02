//Devanshu Save

package hw3;



import java.util.ArrayList;

import hw1.Field;
import hw1.RelationalOperator;

public class BPlusTree {
	
	int degree;
	Node root;
	Node node;
    public BPlusTree(int degree) {
    	//your code here
    	this.degree = degree;
    	root = null;
    	node = root;
    }
    
    public LeafNode search(Field f) {
    	//your code here
    	if (root==null) {
    		return null;
    	}
		while(!node.isLeafNode()) {
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
			//if children are not LeafNodes
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
		ArrayList<Entry> entries = ((LeafNode)node).getEntries();
		for (Entry entry : entries) {
			if(f.compare(RelationalOperator.EQ, entry.getField())) {
				return (LeafNode)node;
			}
		}
    	return null;
    }
    
    public void insert(Entry e) {
    	//your code here
    }
    
    public void delete(Entry e) {
    	//your code here
    }
    
    public Node getRoot() {
    	//your code here
    	return null;
    }
}
