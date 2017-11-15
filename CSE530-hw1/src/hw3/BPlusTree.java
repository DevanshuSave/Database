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
    
    //Method created by Devanshu
    private Node getLeftSibling(Node n) {
    	Field f = null;
    	if(n.isLeafNode()) {
    		f=((LeafNode)n).getEntries().get(0).getField();
    	}
    	else {
    		f= ((InnerNode)n).getKeys().get(0);
    	}
    	InnerNode in = getParent(f);
    	if (in==null) {
    		return null;
    	}
    	ArrayList<Field> field = in.getKeys();
    	//First Child has no left sibling
    	if(f.compare(RelationalOperator.LTE, field.get(0))) {
    		return null;
    	}
		for (int i=1;i<in.getKeys().size();i++) {
			if(f.compare(RelationalOperator.LTE, field.get(i))) {
				return in.getChildren().get(i-1);
			}
		}
		if(f.compare(RelationalOperator.GTE, field.get(in.getKeys().size()-1))) {
			return in.getChildren().get(in.getKeys().size()-1);
		}
    	return null;
    }
    
    //Method created by Devanshu
    private Node getRightSibling(Node n) {
    	Field f = null;
    	if(n.isLeafNode()) {
    		f=((LeafNode)n).getEntries().get(0).getField();
    	}
    	else {
    		f= ((InnerNode)n).getKeys().get(0);
    	}
    	InnerNode in = getParent(f);
    	if (in==null) {
    		return null;
    	}
    	ArrayList<Field> field = in.getKeys();
    	//First Child has no left sibling
    	if(f.compare(RelationalOperator.GT, field.get(in.getKeys().size()-1))) {
    		return null;
    	}
		for (int i=0;i<in.getKeys().size();i++) {
			if(f.compare(RelationalOperator.LTE, field.get(i))) {
				return in.getChildren().get(i+1);
			}
		}
    	return null;
    }
    
    //Method created by Devanshu
    private InnerNode getParent(Field f) {
    	if (getRoot()==null) {
    		return null;
    	}
    	Node node;
    	if(getRoot().isLeafNode()) {
    		node = getRoot();
    	}
    	else {
    		node = getRoot();
    	}
    	Stack<InnerNode> s = new Stack<InnerNode>();
    	while(!node.isLeafNode()) {
    		s.push((InnerNode)node);
    		ArrayList<Field> keys = ((InnerNode)node).getKeys();
    		for (int i=0;i<keys.size();i++) {
    			if(f.compare(RelationalOperator.LTE, keys.get(i))) {
    				node = ((InnerNode)node).getChildren().get(i);
    				break;
    			}
				if(f.compare(RelationalOperator.GT, keys.get(keys.size()-1))) {
    				node = (((InnerNode)node).getChildren().get((((InnerNode)node).getChildren()).size()-1));
    				break;
    			}
    		}
    	}
		ArrayList<Entry> entries = ((LeafNode)node).getEntries();
		for (Entry entry : entries) {
			if(f.compare(RelationalOperator.EQ, entry.getField())) {
				return s.pop();
			}
		}
    	return null;
    }
    
  //Method created by Devanshu
    private InnerNode getParent(Node n) {
    	if (getRoot()==null) {
    		return null;
    	}
    	
    	if(getRoot().isLeafNode()) {
    		return null;
    	}
    	Node node = getRoot();
    	if(n.isLeafNode()) {
	    	Field f = ((LeafNode)n).getEntries().get(0).getField();
	    	Stack<InnerNode> s = new Stack<InnerNode>();
	    	while(!node.isLeafNode()) {
	    		s.push((InnerNode)node);
	    		ArrayList<Field> keys = ((InnerNode)node).getKeys();
	    		for (int i=0;i<keys.size();i++) {
	    			if(f.compare(RelationalOperator.LTE, keys.get(i))) {
	    				node = ((InnerNode)node).getChildren().get(i);
	    				break;
	    			}
					if(f.compare(RelationalOperator.GT, keys.get(keys.size()-1))) {
	    				node = (((InnerNode)node).getChildren().get((((InnerNode)node).getChildren()).size()-1));
	    				break;
	    			}
	    		}
	    	}
			ArrayList<Entry> entries = ((LeafNode)node).getEntries();
			for (Entry entry : entries) {
				if(f.compare(RelationalOperator.EQ, entry.getField())) {
					return s.pop();
				}
			}
    	}
    	else {
    		Field f = ((InnerNode)n).getKeys().get(0);
    		Stack<InnerNode> s = new Stack<InnerNode>();
	    	while(!node.isLeafNode()) {
	    		s.push((InnerNode)node);
	    		ArrayList<Field> keys = ((InnerNode)node).getKeys();
	    		for (int i=0;i<keys.size();i++) {
	    			if(f.compare(RelationalOperator.EQ, keys.get(i))) {
	    				s.pop();
	    				return s.pop();
	    			}
	    			if(f.compare(RelationalOperator.LT, keys.get(i))) {
	    				node = ((InnerNode)node).getChildren().get(i);
	    				break;
	    			}
					if(f.compare(RelationalOperator.GT, keys.get(keys.size()-1))) {
	    				node = (((InnerNode)node).getChildren().get((((InnerNode)node).getChildren()).size()-1));
	    				break;
	    			}
	    		}
	    	}
    	}
    	return null;
    }
    
    //Method created by Devanshu
    private ArrayList<LeafNode> split(LeafNode lf){
    	ArrayList<LeafNode> leafs = new ArrayList<LeafNode>();
    	LeafNode lf1 = new LeafNode(getDegree());
    	LeafNode lf2 = new LeafNode(getDegree());
    	ArrayList<Entry> en1 = new ArrayList<Entry>(getDegree());
    	ArrayList<Entry> en2 = new ArrayList<Entry>(getDegree());
    	
    	int n = lf.getEntries().size();
    	for(int i=0;i<n;i++) {
    		if(i<(n+1)/2) {
    			en1.add(lf.getEntries().get(i));
    		}
    		else {
    			en2.add(lf.getEntries().get(i));
    		}
    	}
    	lf1.setEntries(en1);
    	lf2.setEntries(en2);
    	leafs.add(lf1);
    	leafs.add(lf2);
    	return leafs;
    }
    
    //Method created by Devanshu
    private LeafNode merge(LeafNode lf1, LeafNode lf2){
    	LeafNode lf = new LeafNode(getDegree());
    	ArrayList<Entry> entries = new ArrayList<Entry>();
    	entries.addAll(lf1.getEntries());
    	entries.addAll(lf2.getEntries());
    	lf.setEntries(entries);
    	return lf;
    }
    
    //Method created by Devanshu
    private ArrayList<InnerNode> split(InnerNode in){
    	//Node has degree+2 children(1 extra child)
    	
    	ArrayList<InnerNode> inners = new ArrayList<InnerNode>();
    	InnerNode in1 = new InnerNode(getDegree());
    	InnerNode in2 = new InnerNode(getDegree());
    	int n;
    	//boolean isChildLeaf = in.getChildren().get(0).isLeafNode();
    	
    	//Keys
    	ArrayList<Field> k1 = new ArrayList<Field>(getDegree());
    	ArrayList<Field> k2 = new ArrayList<Field>(getDegree());
    	n = in.getKeys().size();
    	for(int i=0;i<n;i++) {
    		if(i<(n+1)/2) {
    			k1.add(in.getKeys().get(i));
    		}
    		else {
    			k2.add(in.getKeys().get(i));
    		}
    	}
    	if(n%2==1) {
    		k1.remove(k1.size()-1);
    	}
    	in1.setKeys(k1);
    	in2.setKeys(k2);
    	
    	//Children
    	ArrayList<Node> c1 = new ArrayList<Node>(getDegree()+1);
    	ArrayList<Node> c2 = new ArrayList<Node>(getDegree()+1);
    	n = in.getChildren().size();
    	for(int i=0;i<n;i++) {
    		if(i<(n+1)/2) {
    			c1.add(in.getChildren().get(i));
    		}
    		else {
    			c2.add(in.getChildren().get(i));
    		}
    	}
    	in1.setChildren(c1);
    	in2.setChildren(c2);

    	inners.add(in1);
    	inners.add(in2);
    	return inners;
    }
    
    //Method created by Devanshu
    private InnerNode merge(InnerNode in1, InnerNode in2){
    	InnerNode in = new InnerNode(getDegree());
    	ArrayList<Node> c = new ArrayList<Node>();
    	c.addAll(in1.getChildren());
    	c.addAll(in2.getChildren());
    	in.setChildren(c);
    	
    	ArrayList<Field> k = new ArrayList<Field>();
    	k.addAll(in1.getKeys());
    	if(in1.getChildren().get(0).isLeafNode()) {
    		//Extracts largest element of rightmost child (a leafnode) of first innernode which has to be added in the new list of keys
    		k.add(((LeafNode)in1.getChildren().get(in1.getChildren().size()-1)).getEntries().get(((LeafNode)in1.getChildren().get(in1.getChildren().size()-1)).getEntries().size()-1).getField());
    	}
    	else {
    		//Extracts largest element of rightmost child (an innernode) of first innernode which has to be added in the new list of keys
    		k.add(((InnerNode)in1.getChildren().get(in1.getChildren().size()-1)).getKeys().get(((InnerNode)in1.getChildren().get(in1.getChildren().size()-1)).getKeys().size()-1));
    	}
    	
    	k.addAll(in2.getKeys());
    	in.setKeys(k);
    	return in;
    }
    
    public LeafNode search(Field f) {
    	//your code here
    	if (root==null) {
    		return null;
    	}
    	Node node;
    	if(root.isLeafNode()) {
    		node = (LeafNode)root;
    	}
    	else {
    		node = (InnerNode)root;
    	}
    	while(!node.isLeafNode()) {
    		//int q = ((InnerNode)node).getChildren().size();
    		ArrayList<Field> keys = ((InnerNode)node).getKeys();
    		for (int i=0;i<keys.size();i++) {
    			if(f.compare(RelationalOperator.LTE, keys.get(i))) {
    				node = ((InnerNode)node).getChildren().get(i);
    				break;
    			}
				if(f.compare(RelationalOperator.GT, keys.get(keys.size()-1))) {
    				node = (((InnerNode)node).getChildren().get((((InnerNode)node).getChildren()).size()-1));
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
    
    //Method created by Devanshu
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
    
    //Method created by Devanshu
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
			//LeafNode n = new LeafNode(getDegree());
			//n.setEntries(((LeafNode)node).getEntries());
			LeafNode n = (LeafNode)node;
			node = stack.peek();
			System.out.println("xxx"+((InnerNode)node).getChildren().size());
			System.out.println("yyy"+((InnerNode)node).getDegree());
			
    		if(((InnerNode)node).getChildren().size()<((InnerNode)node).getDegree()+1) {
				//System.out.println("my size:"+arrayList.size()+":"+(lf1.getEntries()).size()+":"+(lf2.getEntries()).size());
	    		
				ArrayList<Node> child = ((InnerNode)node).getChildren();
				child.add(child.indexOf(n),lf1);
				child.add(child.indexOf(n),lf2);
				child.remove(n);
				((InnerNode)node).setChildren(child);
				
				((InnerNode)node).setKeys(addAndSortField(((InnerNode)node).getKeys(), lf1.getEntries().get(lf1.getEntries().size()-1).getField()));
				System.out.println("am i here");
				return;
			}
			//Parent Node is full
    		
    		else {
    			boolean flag = true;
    			while(!stack.isEmpty()) {
	    			node = stack.pop();
	    			
	    			if(flag) {
	    				System.out.println("yo");
		    			ArrayList<Node> child = new ArrayList<Node>();
		    			child.addAll(((InnerNode)node).getChildren());
		    			int num = child.indexOf(n);
						child.add(num,lf1);
						child.add(num+1,lf2);
						child.remove(n);
						((InnerNode)node).setChildren(child);
						ArrayList<Field> fff = ((InnerNode)node).getKeys();
						//fff.remove(num);
						((InnerNode)node).setKeys(addAndSortField(fff, lf1.getEntries().get(lf1.getEntries().size()-1).getField()));
						System.out.println("mmmmmmmmmmmm"+((InnerNode)node).getKeys().toString());
		    			flag=false;
	    			}
	    			
					InnerNode i1 = new InnerNode(getDegree());
					InnerNode i2 = new InnerNode(getDegree());
					ArrayList<Field> iKeys = new ArrayList<Field>();
					iKeys.addAll(((InnerNode)node).getKeys());
					ArrayList<Node> iChildren = new ArrayList<Node>();
					iChildren.addAll(((InnerNode)node).getChildren());
	    			
					//node = (new InnerNode(getDegree()));
	    			
					i1.setKeys(new ArrayList<Field>(iKeys.subList(0, (iKeys.size()+1)/2)));
	    			i2.setKeys(new ArrayList<Field>(iKeys.subList((iKeys.size()+1)/2, iKeys.size())));
	    			System.out.println("i1:"+i1.getKeys().toString());
					System.out.println("i2:"+i2.getKeys().toString());
	    			i1.setChildren(new ArrayList<Node>(iChildren.subList(0, (iChildren.size()+1)/2)));
	    			i2.setChildren(new ArrayList<Node>(iChildren.subList((iChildren.size()+1)/2, iChildren.size())));

	    			
	    			//ArrayList<Field> k = new ArrayList<Field>();
	    			Field k = i1.getKeys().get((i1.getKeys()).size()-1);
	    			//k.add(lf2.getEntries().get((lf2.getEntries()).size()-1).getField());
	    			//k.add(i1.getKeys().get((i1.getKeys()).size()-1));
	    			
	    			
	    			iKeys = i1.getKeys();
	    			iKeys.remove(i1.getKeys().get((i1.getKeys()).size()-1));
	    			i1.setKeys(iKeys);
	    			ArrayList<Node> child1 = new ArrayList<Node>();
	    			child1.add(i1);
	    			child1.add(i2);
	    			
	    			((InnerNode)node).setChildren(child1);
	    			if(stack.size()!=0) {
		    			((InnerNode)stack.peek()).setKeys(addAndSortField(((InnerNode)stack.peek()).getKeys(), k));
	    			}
	    			//System.out.println("yoyoyoy"+i1.getKeys().get((i1.getKeys()).size()-1));
	    			else {
	    				((InnerNode)node).setChildren(child1);
	    				((InnerNode)node).setKeys(null);
	    				ArrayList<Field> kk = new ArrayList<>();
	    				kk.add(k);
	    				((InnerNode)node).setKeys(kk);
	    			}
	    			//((InnerNode)node).setKeys(addAndSortField(((InnerNode)node).getKeys(), k));
	    			System.out.println("m herrrrrrrrrrrrrrrreeeeee");
					System.out.println(((InnerNode)root).getKeys().toString());
					if(((InnerNode)node).getKeys().size()>getDegree()){
						System.out.println("alert"+e.getField());
					}
    			}
    			setRoot((InnerNode)node);
    		}
    	}
    	//printTree();
    }
    
    //Method created by Devanshu
    private void printTree() {
    	System.out.println("Printing tree");
    	for(int i=0;i<((InnerNode)getRoot()).getChildren().size();i++) {
    		System.out.println(((InnerNode)getRoot()).getKeys().toString());
			for(int j=0;j<((InnerNode)(((InnerNode)getRoot()).getChildren()).get(i)).getChildren().size();j++) {
				System.out.println(((InnerNode)(((InnerNode)getRoot()).getChildren()).get(i)).getKeys().toString());
				//System.out.println(((LeafNode)((InnerNode)getRoot()).getChildren().get(i)).getEntries().toString());
				//System.out.println(i+":"+j+":"+((LeafNode)child.get(i)).getEntries().get(j).getField());
			}
		}
    }

    //Method created by Devanshu

    //Method created by Devanshu
    private void borrow(LeafNode child, InnerNode parent, LeafNode sibling) {
    	if(getLeftSibling(child).equals(sibling)) {
    		//Left sibling - borrow last
    		ArrayList<Entry> en = new ArrayList<Entry>(getDegree());
    		en.add(sibling.getEntries().get(sibling.getEntries().size()-1));
    		en.addAll(child.getEntries());
    		child.setEntries(en);
    		
    		en = null;
    		//en.clear();
    		en = sibling.getEntries();
    		en.remove(en.size()-1);
    		sibling.setEntries(en);
    		
    		ArrayList<Field> key = new ArrayList<Field>(getDegree());
    		key.addAll(parent.getKeys());
    		key.add(key.indexOf(child.getEntries().get(0).getField()),sibling.getEntries().get(sibling.getEntries().size()-1).getField());
    		key.remove(child.getEntries().get(0).getField());
    		parent.setKeys(key);
    		return;
    	}
    	else {
    		//Right sibling - borrow first
    		ArrayList<Entry> en = new ArrayList<Entry>(getDegree());
    		en.addAll(child.getEntries());
    		en.add(sibling.getEntries().get(0));
    		child.setEntries(en);
    		
    		en = null;
    		en = sibling.getEntries();
    		en.remove(0);
    		sibling.setEntries(en);
    		
    		ArrayList<Field> key = new ArrayList<Field>(getDegree());
    		key.addAll(parent.getKeys());
    		key.add(key.indexOf(child.getEntries().get(child.getEntries().size()-2).getField()), child.getEntries().get(child.getEntries().size()-1).getField());
    		key.remove(child.getEntries().get(child.getEntries().size()-2).getField());
    		parent.setKeys(key);
    		return;
    	}
    }
 
  //Method created by Devanshu
    
    //Method created by Devanshu
    private void borrow(InnerNode child, InnerNode parent, InnerNode sibling) {
    	if(getLeftSibling(child).equals(sibling)) {
    		//Left sibling - borrow last
    		ArrayList<Node> en = new ArrayList<Node>(getDegree());
    		en.add(sibling.getChildren().get(sibling.getChildren().size()-1));
    		en.addAll(child.getChildren());
    		child.setChildren(en);
    		
    		en = null;
    		en = sibling.getChildren();
    		en.remove(en.size()-1);
    		sibling.setChildren(en);
    		
    		ArrayList<Field> key = new ArrayList<Field>(getDegree());
    		key.addAll(parent.getKeys());
    		key.add(key.indexOf(child.getKeys().get(0)),sibling.getKeys().get(sibling.getKeys().size()-1));
    		key.remove(child.getKeys().get(0));
    		parent.setKeys(key);
    		return;
    	}
    	else {
    		//Right sibling - borrow first
    		ArrayList<Node> en = new ArrayList<Node>(getDegree());
    		en.addAll(child.getChildren());
    		en.add(sibling.getChildren().get(0));
    		child.setChildren(en);
    		
    		en= null;
    		en = sibling.getChildren();
    		en.remove(0);
    		sibling.setChildren(en);
    		
    		ArrayList<Field> key = new ArrayList<Field>(getDegree());
    		key.addAll(parent.getKeys());
    		key.add(key.indexOf(child.getKeys().get(child.getKeys().size()-2)), child.getKeys().get(child.getKeys().size()-1));
    		key.remove(child.getKeys().get(child.getKeys().size()-2));
    		parent.setKeys(key);
    		return;
    	}
    }
    
    public void delete(Entry e) {
    	//your code here
    	System.out.println("Deleting element here-----------------------:"+e.getField());
    	LeafNode leafNode = new LeafNode(getDegree());
    	leafNode = search(e.getField());
    	if(leafNode==null) {
    		//System.out.println("inside delete: node not found");
    		return;
    	}
    	
    	ArrayList<Entry> entries = new ArrayList<Entry>();
    	entries.addAll(leafNode.getEntries());
    	
    	//If tree has only root (and search is not null hence entry exists in root)
    	if(getRoot().isLeafNode()) {
    		entries.remove(e);
    		if(entries.equals(null)) {
    			setRoot(null);
    			return;
    		}
    		leafNode.setEntries(entries);
    		return;
    	}
    	
    	Field f = e.getField();
    	Field replace = null;
    	int n = 0;
    	for (int i=0;i<entries.size();i++) {
    		if(f.compare(RelationalOperator.EQ, entries.get(i).getField())) {
    			n=i;
    			if (i==entries.size()-1 && i>0){
    				replace = entries.get(i-1).getField();
    			}
    			//entries.remove(e);
    			entries.remove(n);
    			break;
    		}
    	}
    	
    	InnerNode in = getParent(f);
    	//no underflow    	
    	if(entries.size()>=(getDegree()+1)/2) {
    		if(n!=entries.size()) {
    			leafNode.setEntries(entries);
    			return;
    		}
    		else {
    			while(in!=null) {
    	    		if(in.getKeys().contains(f)) {
    	    			ArrayList<Field> keys = new ArrayList<Field>();
    	    			keys.addAll(in.getKeys());
    	    			keys.add(keys.indexOf(f),replace);
    	    			keys.remove(f);
    	    			System.out.println("sssssss"+keys.toString());
    	    			in.setKeys(keys);//Does not set in the tree - updated: now sets
    	    			leafNode.setEntries(entries);
    	    			return;
    	    		}
    	    		in = getParent(in);
    	    	}
    		}
    		System.out.println("I am never here");
    	}
    	//else: underflow
    	else {
    		while(in!=null) {
    			ArrayList<Node> c = new ArrayList<Node>(getDegree());
        		ArrayList<Field> k = new ArrayList<Field>(getDegree());
        		c = in.getChildren();
        		k = in.getKeys();
        		LeafNode lf = new LeafNode(getDegree());
        		lf = (LeafNode)getLeftSibling(leafNode);
        		if(lf!=null) {
        			if(lf.getEntries().size()>(getDegree()+1)/2) {
        				borrow(leafNode, in, lf);
        				entries = leafNode.getEntries();
        				for (int i=0;i<entries.size();i++) {
        		    		if(f.compare(RelationalOperator.EQ, entries.get(i).getField())) {
        		    			entries.remove(i);
        		    			break;
        		    		}
        		    	}
        				leafNode.setEntries(entries);
        				ArrayList<Field> temp = new ArrayList<Field>();
        				temp = in.getKeys();
        				if(temp.indexOf(f)!=-1) {
        					temp.add(temp.indexOf(f), entries.get(entries.size()-1).getField());
        					temp.remove(f);
        				}
        				in.setKeys(temp);
        				return;
        			}
        			else {
        				lf = null;
        		}
        		
        		if(lf==null) {
        			lf = ((LeafNode)getRightSibling(leafNode));
        			if(lf!=null) {
            			if(lf.getEntries().size()>(getDegree()+1)/2) {
            				borrow(leafNode, in, lf);
            				entries = leafNode.getEntries();
            				for (int i=0;i<entries.size();i++) {
            		    		if(f.compare(RelationalOperator.EQ, entries.get(i).getField())) {
            		    			entries.remove(i);
            		    			break;
            		    		}
            		    	}
            				leafNode.setEntries(entries);
            				ArrayList<Field> temp = new ArrayList<Field>();
            				temp = in.getKeys();
            				if(temp.indexOf(f)!=-1) {
            					temp.add(temp.indexOf(f), entries.get(entries.size()-1).getField());
            					temp.remove(f);
            				}
            				in.setKeys(temp);
            				return;
            			}
	        		}
        		}
        		//Could not borrow from either siblings
        		//Merge
        		
        		if(c.size()>(getDegree()+2)/2) {
	        		LeafNode lfnew = new LeafNode(getDegree());
	        		LeafNode sibling = new LeafNode(getDegree());
	        		sibling = (LeafNode)getLeftSibling(leafNode);
	        		lfnew = merge(leafNode, sibling);
	        		ArrayList<Entry> temp = new ArrayList<Entry>();
	        		temp.addAll(sibling.getEntries());
	        		temp.addAll(entries);
	        		lfnew.setEntries(temp);
	        		c.add(c.indexOf(sibling), lfnew);
	        		c.remove(leafNode);
	        		c.remove(sibling);
	        		in.setChildren(c);
	        		
	        		//k.add(leafNode.getEntries().size()-1, lfnew.getEntries().get(lfnew.getEntries().size()-1).getField());
	        		k.remove(leafNode.getEntries().get(leafNode.getEntries().size()-1).getField());
	        		in.setKeys(k);
	        		return;
        		}
        		else {}
        		/*
        		if(c.size()==(getDegree()+2)/2) {
        			//underflow in terms of number of children
    	    			
        			}
        			if(sibling!=null) {
        				if(sibling.getChildren().size()==(getDegree()+2)/2) {
        					sibling=null;
        				}
        				else {
        					in = merge(sibling, in);
        				}
        			}
        			if(sibling==null) {
        				//no sibling to merge >>> reduce level
        			}
        		}
        		else {
        			c.remove(null);
        			in.setChildren(c);
        		}
        	
    	}
    	
    	leafNode.setEntries(entries);
    	
    	
    	//Stack Search and update
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
    	//Node should be leafnode
    	if(n==entries.size()-1) {
    		Field ff= entries.get(n-1).getField();
        	System.out.println("Deleting Last element");
    		entries.remove(e);
    		
        	if(node.equals(leafNode)) {
        		System.out.println("Yes -------------------------");
        	}
        	else {
        		System.out.println("No----------------------------");
        	}
    		
        	while(!stack.isEmpty()) {
        		node = stack.pop();
        		ArrayList<Field> keys = ((InnerNode)node).getKeys();
        		for(int i=0;i<keys.size();i++) {
        			if(keys.get(i).compare(RelationalOperator.EQ, e.getField())) {
        				keys.set(i, ff);
        				break;
        			}
        		}
        	}
        	return;
    	}
    	else {
    		System.out.println("internal element deleted");
    		
    		if(entries.size()<(getDegree()+1)/2) {
    			//merge
    			node = stack.pop();
    			ArrayList<Node> children = ((InnerNode)node).getChildren();
    			int c = children.indexOf(leafNode);
    			if(c>0) {
    				if(((LeafNode)children.get(c-1)).getEntries().size()>(getDegree()+1)/2) {
    					
    				}
    			}
    		}
    		entries.remove(e);
    		leafNode.setEntries(entries);
    		}*/
    	
        		}
    		}
    	}
    	return;
    }
    
    //Method created by Devanshu
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
