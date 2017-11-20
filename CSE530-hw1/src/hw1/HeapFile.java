//Devanshu Save
package hw1;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * A heap file stores a collection of tuples. It is also responsible for managing pages.
 * It needs to be able to manage page creation as well as correctly manipulating pages
 * when tuples are added or deleted.
 * @author Sam Madden modified by Doug Shook
 *
 */
public class HeapFile {
	
	public static final int PAGE_SIZE = 4096;
	
	/**
	 * Creates a new heap file in the given location that can accept tuples of the given type
	 * @param f location of the heap file
	 * @param types type of tuples contained in the file
	 */
	private File f;
	private TupleDesc type;
	
	public HeapFile(File f, TupleDesc type) {
		//your code here
		this.f = f;
		this.type = type;
	}
	
	public File getFile() {
		//your code here
		return f;
	}
	
	public TupleDesc getTupleDesc() {
		//your code here
		return type;
	}
	
	/**
	 * Creates a HeapPage object representing the page at the given page number.
	 * Because it will be necessary to arbitrarily move around the file, a RandomAccessFile object
	 * should be used here.
	 * @param id the page number to be retrieved
	 * @return a HeapPage at the given page number
	 */
	public HeapPage readPage(int id) {
		//your code here
		//HeapPage hp = new HeapPage(id, new byte[4096], this.getId());
		
        byte[] byteStream = new byte[PAGE_SIZE];
        RandomAccessFile raf;
		try {
				raf = new RandomAccessFile(f, "r");
		        raf.seek(PAGE_SIZE * id);
		        raf.readFully(byteStream);
		        raf.close();
		        //String s = new String(byteStream, "UTF-8");
		        return new HeapPage(id, byteStream, this.getId());
	        } catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns a unique id number for this heap file. Consider using
	 * the hash of the File itself.
	 * @return
	 */
	public int getId() {
		//your code here
		return this.hashCode();
	}
	
	/**
	 * Writes the given HeapPage to disk. Because of the need to seek through the file,
	 * a RandomAccessFile object should be used in this method.
	 * @param p the page to write to disk
	 */
	public void writePage(HeapPage p) throws Exception {
		//your code here
		assert p instanceof HeapPage : "Write non-heap page to a heap file.";
	    RandomAccessFile raf = new RandomAccessFile(f, "rw");
	    raf.seek(PAGE_SIZE * p.getId());
	    raf.write(p.getPageData());
	    raf.close();
	}
	
	/**
	 * Adds a tuple. This method must first find a page with an open slot, creating a new page
	 * if all others are full. It then passes the tuple to this page to be stored. It then writes
	 * the page to disk (see writePage)
	 * @param t The tuple to be stored
	 * @return The HeapPage that contains the tuple
	 */
	public HeapPage addTuple(Tuple t) throws Exception {
		//your code here
		//This method calls add from Heap Page
		if (!type.equals(t.getDesc())) {
			throw new Exception("TupleDesc does not match.");
		}
        
        for (int i = 0; i < getNumPages(); i ++) {
        	HeapPage hp = readPage(i);
        	for (int j = 0; j < hp.getNumSlots(); j ++) {
                if (!hp.slotOccupied(j)) {
                	hp.addTuple(t);
                	System.out.println("in heapfile:addtuple:first:"+t);
                	try {
	                    byte[] byteStream = hp.getPageData();
	                    RandomAccessFile raf = new RandomAccessFile(f, "rw");
	                    raf.seek(PAGE_SIZE*i);
	                    raf.write(byteStream);
	                    raf.close();
	                }
	                catch (IOException e) {
	                    throw e;
	                }
                	return hp;
                }
            }
        }
        HeapPage hp = new HeapPage(getNumPages(), new byte[PAGE_SIZE], this.getId());
        hp.addTuple(t);
        this.writePage(hp);
        return hp;
	}
	
	/**
	 * This method will examine the tuple to find out where it is stored, then delete it
	 * from the proper HeapPage. It then writes the modified page to disk.
	 * @param t the Tuple to be deleted
	 */
	public void deleteTuple(Tuple t) throws Exception{
		//your code here
		//This method calls delete from Heap Page
		if (!type.equals(t.getDesc())) {
			throw new Exception("TupleDesc does not match.");
		}
        
        for (int i = 0; i < getNumPages(); i ++) {
        	HeapPage hp = readPage(i);
        	for (int j = 0; j < hp.getNumSlots(); j ++) {
                if (hp.slotOccupied(j)) {
                	hp.deleteTuple(t);
                	try {
	                    byte[] byteStream = hp.getPageData();
	                    RandomAccessFile raf = new RandomAccessFile(f, "rw");
	                    raf.seek(PAGE_SIZE*i);
	                    raf.write(byteStream);
	                    raf.close();
	                }
	                catch (IOException e) {
	                    throw e;
	                }
                }
            }
        }
	}
	
	/**
	 * Returns an ArrayList containing all of the tuples in this HeapFile. It must
	 * access each HeapPage to do this (see iterator() in HeapPage)
	 * @return
	 */
	public ArrayList<Tuple> getAllTuples() {
		//your code here
		ArrayList<Tuple> myList = new ArrayList<Tuple>();
        for (int i = 0; i < getNumPages(); i ++) {
        	HeapPage hp = readPage(i);
        	Iterator<Tuple> tupIterator = hp.iterator();
    		while (tupIterator.hasNext()) {
    			myList.add(tupIterator.next());
    		}
    		/*
        	Tuple[] tp = hp.getTuples();
        	for (int j = 0; j < hp.getNumSlots(); j ++) {
                if (hp.slotOccupied(j)) {
                	myList.add(tp[j]);
                }
            }
            */
        }
        return myList;
	}
	
	/**
	 * Computes and returns the total number of pages contained in this HeapFile
	 * @return the number of pages
	 */
	public int getNumPages() {
		//your code here
		return (int)Math.ceil((double)(f.length()/PAGE_SIZE));
	}
}