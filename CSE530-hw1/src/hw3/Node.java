//Devanshu Save

package hw3;

public interface Node {
	
	public int getDegree();
	public boolean isLeafNode();
	
	//Methods added by Devanshu
	public void setDegree(int degree);
    public boolean equals(Object node);//Do we need this?
}
