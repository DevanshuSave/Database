//Devanshu Save

package hw3;

import java.util.ArrayList;
import java.util.Stack;

import hw1.Field;
import hw1.RelationalOperator;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;

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
    			return myList;
    		}
    	}
    	myList.add(entry);
    	return myList;
    }
    
    private ArrayList<Field> addAndSortField(ArrayList<Field> myList, Field field) {
    	for(Field e : myList) {
    		if(field.compare(RelationalOperator.LT, e)) {
    			myList.add(myList.indexOf(e),field);
    			return myList;
    		}
    	}
    	myList.add(field);
    	return myList;
    }
    
    public void insert(Entry e) {
    	//your code here
       	
    	//First insertion
    	if (getRoot()==null) {
    		System.out.println("Entry1:"+e.getField());
    		setRoot(new LeafNode(getDegree()));
    		ArrayList<Entry> entries = new ArrayList<>();
    		entries.add(e);
    		((LeafNode)root).setEntries(entries);
    		/*if(root ==null) {
    			System.out.println("lol");
    		}
    		System.out.println("Not lol");
    		if(root.isLeafNode()){
    			System.out.println("First insert, Root:Leaf, size:"+((LeafNode)getRoot()).getEntries().size());
    		}
    		*/
    		return;
    	}
    	//Root with less than max entries
    	if(getRoot().isLeafNode()) {
    		if(((LeafNode)getRoot()).getEntries().size()<getDegree()) {
    			System.out.println("Entry2:"+e.getField());
    			((LeafNode)getRoot()).setEntries(addAndSort(((LeafNode)getRoot()).getEntries(), e));
    			/*if(root.isLeafNode()) {
    				System.out.println("Only root, Root:leaf, size:"+((LeafNode)getRoot()).getEntries().size());
    			}
    			else {
    				System.out.println("Root is not leaf");
    			}*/
    			return;
    		}
    	//}
    	//Root has max entries
    	//if(getRoot().isLeafNode()) {
    		//if(((LeafNode)getRoot()).getEntries().size()==getDegree()) {
    		else {
        		System.out.println("Entry3:"+e.getField());
    			LeafNode lf1 = new LeafNode(getRoot().getDegree());
    			LeafNode lf2 = new LeafNode(getRoot().getDegree());
    			ArrayList<Entry> arrayList = addAndSort(((LeafNode)getRoot()).getEntries(), e);
    			/*if(getRoot().isLeafNode()) {
    				System.out.println("Before is Leaf*************");
    			}*/
    			setRoot(new InnerNode(getDegree()));
    			/*if(getRoot().isLeafNode()) {
    				System.out.println("After is Leaf*************");
    			}*/
    			lf1.setEntries(new ArrayList<Entry>(arrayList.subList(0, (arrayList.size()+1)/2)));
    			lf2.setEntries(new ArrayList<Entry>(arrayList.subList((arrayList.size()+1)/2, arrayList.size())));
    			ArrayList<Node> child = new ArrayList<Node>();
    			child.add(lf1);
    			child.add(lf2);
    			((InnerNode)root).setChildren(child);
    			ArrayList<Field> k = new ArrayList<Field>();
    			k.add(lf1.getEntries().get(lf1.getEntries().size()-1).getField());
    			((InnerNode)root).setKeys(k);
    			System.out.println("zzzzzzzzzzzzz"+((InnerNode)getRoot()).getKeys().size());
    			return;
    		}
    	}
    	System.out.println("Entry4:"+e.getField());
    	System.out.println("keys in root:"+((InnerNode)getRoot()).getKeys().toString());
    	//Search the leafNode 
    	Node node = new InnerNode(getDegree());
    	((InnerNode)node).setKeys(((InnerNode)root).getKeys());
    	((InnerNode)node).setChildren(((InnerNode)root).getChildren());
    	Stack<Node> stack = new Stack<Node>();
    	while(!node.isLeafNode()) {
    		stack.push(node);
    		int q = ((InnerNode)node).getChildren().size();
    		ArrayList<Field> keys = ((InnerNode)node).getKeys();
    		for (int i=0;i<keys.size();i++) {
    			if(e.getField().compare(RelationalOperator.LTE, keys.get(i))) {
    				node = ((InnerNode)node).getChildren().get(i);
    				break;
    			}
				if(e.getField().compare(RelationalOperator.GT, keys.get(keys.size()-1))) {
    				node = ((InnerNode)node).getChildren().get(q-1);
    				break;
    			}
    		}
    	}
    	
    	//Here node is always a LeafNode
    	
    	ArrayList<Entry> entries = ((LeafNode)node).getEntries();
		for (Entry entry : entries) {
			if(e.getField().compare(RelationalOperator.EQ, entry.getField())) {
				System.out.println("Record already exists");
				return;
			}
		}
		
		//LeafNode is not full
    	if (((LeafNode)node).getEntries().size()<((LeafNode)node).getDegree()) {
    		System.out.println("leaf is not full");
    		//entries = addAndSort(((LeafNode)node).getEntries(), e);
    		entries = ((LeafNode)node).getEntries();
    		for(int i=0;i<entries.size();i++) {
    			//Find placement of the entry
    			if(e.getField().compare(RelationalOperator.LT, entries.get(i).getField())) {
    				entries.add(i, e);
    				((LeafNode)node).setEntries(entries);
    				return;
    			}
    		}
    		//If last space available
    		if(e.getField().compare(RelationalOperator.GT, entries.get(entries.size()-1).getField())) {
    			System.out.println("Last element");
    			entries.add(e);
    			((LeafNode)node).setEntries(entries);
    			return;
    			//update all parents' keys
    			
    			//Is this required??? >>>Should not happen: Value goes to last place, parent is never updated
    			/*
    			ArrayList<Field> entryFields = new ArrayList<Field>(entries.size());
    			System.out.println("Idhar");
    			while(!stack.isEmpty()) {
    				InnerNode temp = (InnerNode)stack.pop();
    				
    				//Extract all the field values from the entries list
    				for(int i = 0;i<entries.size();i++) {
    					entryFields.add(entries.get(i).getField());
    				}
    				boolean flag = true;
    				for(int i=0;i<temp.getKeys().size();i++) {
	    				if(entryFields.get(entries.size()-2).compare(RelationalOperator.EQ, temp.getKeys().get(i))) {
	    					ArrayList<Field> fields = temp.getKeys();
	    					fields.set(i, e.getField());
	    					temp.setKeys(fields);
	    					flag = false;
	    				}
    				}
    				if (flag) {
    					return;
    				}
    				entryFields = temp.getKeys();
    			}
    			*/
    		}
    	}
    	else {
    		//split LeafNode
    		/*
    		LeafNode lf = new LeafNode(getDegree()+1);
    		entries = addAndSort(((LeafNode)node).getEntries(), e);
    		for(int i = 0;i<entries.size();i++) {
    			if(e.getField().compare(RelationalOperator.LT, entries.get(i).getField())) {
    				entries.add(i, e);
    				break;
    			}
    		}
    		if(e.getField().compare(RelationalOperator.GT, entries.get(entries.size()-1).getField())){
    			entries.add(e);
    		}
    		
    		ArrayList<Entry> en = new ArrayList<>();
    		ArrayList<Entry> en1 = new ArrayList<>();
    		
    		for (int i=0;i<entries.size();i++) {
    			if(i<(entries.size()+1)/2) {
    				en.add(entries.get(i));
    			}
    			else {
    				en1.add(entries.get(i));
    			}
    		}
    		
    		((LeafNode)node).setEntries(en);
    		((LeafNode)lf).setEntries(en1);
    		*/
    		//boolean finished  = false;
    		//There is no Parent >>> Happens only when root is split: case already considered
    		/*if(stack.isEmpty()) {
    			System.out.println("Never here");
    			root = new InnerNode(getDegree());
    			ArrayList<Node> arrayList = new ArrayList<>();
        		arrayList.add(((LeafNode)node));
        		arrayList.add(lf);
    			((InnerNode)root).setChildren(arrayList);
    			ArrayList<Field> keyList = new ArrayList<>();
    			keyList.add(en.get(en.size()-1).getField());
    			((InnerNode)root).setKeys(keyList);
    		}
    		*/
			LeafNode lf1 = new LeafNode(getDegree());
			LeafNode lf2 = new LeafNode(getDegree());
			ArrayList<Entry> arrayList = new ArrayList<Entry>();
			arrayList = addAndSort(((LeafNode)node).getEntries(), e);
			/*
			for(int j=0;j<arrayList.size();j++) {
				System.out.println("wow"+arrayList.get(j).getField());
			}
			*/
			lf1.setEntries(new ArrayList<Entry>(arrayList.subList(0, (arrayList.size()+1)/2)));
			lf2.setEntries(new ArrayList<Entry>(arrayList.subList((arrayList.size()+1)/2, arrayList.size())));
    		//Parent node exists
			LeafNode n = (LeafNode)node;
			node = stack.peek();
			System.out.println("xxx"+((InnerNode)node).getChildren().size());
			System.out.println("yyy"+((InnerNode)node).getDegree());
			
    		if(((InnerNode)node).getChildren().size()<((InnerNode)node).getDegree()+1) {
				//System.out.println("my size:"+arrayList.size()+":"+(lf1.getEntries()).size()+":"+(lf2.getEntries()).size());
	    		
				ArrayList<Node> child = ((InnerNode)node).getChildren();
				/*
				System.out.println("# children:"+((InnerNode)node).getChildren().size());
				System.out.println("aaaaaaaaaaaaaaaaaaaa");
				for(int i=0;i<child.size();i++) {
					for(int j=0;j<((LeafNode)child.get(i)).getEntries().size();j++) {
						System.out.println(i+":"+j+":"+((LeafNode)child.get(i)).getEntries().get(j).getField());
					}
				}
				*/
				child.add(child.indexOf(n),lf1);
				child.add(child.indexOf(n),lf2);
				child.remove(n);
				((InnerNode)node).setChildren(child);
				
				((InnerNode)node).setKeys(addAndSortField(((InnerNode)node).getKeys(), lf1.getEntries().get(lf1.getEntries().size()-1).getField()));
				System.out.println("am i here");
				return;
				/*
				System.out.println("bbbbbbbbbbbbbbbbbbb");
				for(int i=0;i<child.size();i++) {
					for(int j=0;j<((LeafNode)child.get(i)).getEntries().size();j++) {
						System.out.println(i+":"+j+":"+((LeafNode)child.get(i)).getEntries().get(j).getField());
					}
				}
				*/
				/*
				((InnerNode)node).setKeys(addAndSortField(((InnerNode)node).getKeys(), en.get(en.size()-1).getField()));
				
				ArrayList<Field> keys = ((InnerNode)node).getKeys();
	    		for(int i = 0;i<keys.size();i++) {
	    			if(en.get(en.size()-1).getField().compare(RelationalOperator.LT, keys.get(i))) {
	    				keys.add(i, en.get(en.size()-1).getField());
	    				break;
	    			}
	    		}
	    		((InnerNode)node).setKeys(keys);
	    		
	    		finished = true;
	    		*/
			}
			//Parent Node is full
    		
    		else {
    			boolean flag = true;
    			while(!stack.isEmpty()) {
	    			node = stack.pop();
	    			
	    			if(flag) {
		    			ArrayList<Node> child = ((InnerNode)node).getChildren();
						child.add(child.indexOf(n),lf1);
						child.add(child.indexOf(n),lf2);
						child.remove(n);
						((InnerNode)node).setChildren(child);
						((InnerNode)node).setKeys(addAndSortField(((InnerNode)node).getKeys(), lf1.getEntries().get(lf1.getEntries().size()-1).getField()));
		    			flag=false;
	    			}
	    			
    				//while(((InnerNode)node).getChildren().size()==((InnerNode)node).getDegree()) {
					/*InnerNode temp = new InnerNode(node.getDegree()+1);
					temp.setChildren(((InnerNode)node).getChildren());
					System.out.println("children count"+temp.getChildren().size());
					temp.setKeys(((InnerNode)node).getKeys());*/
					//ArrayList<Field> arrayList1 = temp.getKeys();
					
					InnerNode i1 = new InnerNode(getDegree());
					InnerNode i2 = new InnerNode(getDegree());
					ArrayList<Field> iKeys = (((InnerNode)node).getKeys());
					ArrayList<Node> iChildren = (((InnerNode)node).getChildren());
	    			
	    			//node = (new InnerNode(getDegree()));
	    			
					i1.setKeys(new ArrayList<Field>(iKeys.subList(0, (iKeys.size()+1)/2)));
	    			i2.setKeys(new ArrayList<Field>(iKeys.subList((iKeys.size()+1)/2, iKeys.size())));
	    			i1.setChildren(new ArrayList<Node>(iChildren.subList(0, (iChildren.size()+1)/2)));
	    			i2.setChildren(new ArrayList<Node>(iChildren.subList((iChildren.size()+1)/2, iChildren.size())));
	    			
	    			ArrayList<Node> child1 = new ArrayList<Node>();
	    			child1.add(i1);
	    			child1.add(i2);
	    			((InnerNode)node).setChildren(child1);
	    			
	    			ArrayList<Field> k = new ArrayList<Field>();
	    			k.add((i1.getKeys()).get(i1.getKeys().size()-1));
	    			((InnerNode)node).setKeys(null);
	    			((InnerNode)node).setKeys(k);
	    			System.out.println("m herrrrrrrrrrrrrrrreeeeee");
					System.out.println(((InnerNode)root).getKeys().toString());
					/*
					for(Field field : arrayList1) {
						if(field.compare(RelationalOperator.LT, lf1.getEntries().get(lf1.getEntries().size()-1).getField())){
							arrayList1.add(lf1.getEntries().get(lf1.getEntries().size()-1).getField());
							break;
						}
					}
					if(!arrayList1.contains(lf1.getEntries().get(lf1.getEntries().size()-1).getField())) {
						arrayList1.add(lf1.getEntries().get(lf1.getEntries().size()-1).getField());
					}
					temp.setKeys(arrayList1);
					*/
    				//}
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
    			setRoot((InnerNode)node);
    		}
    	}
    }
    
    public void delete(Entry e) {
    	//your code here
    	LeafNode leafNode = search(e.getField());
    	if(leafNode!=null) {
    		System.out.println("inside delete: node not found");
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
