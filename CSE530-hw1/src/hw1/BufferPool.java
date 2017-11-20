/*
 * Student 1 name: Devanshu Save
 * Student 2 name: Saron Belay
 * Date: 11/16/17
 */

package hw1;

import java.io.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * BufferPool manages the reading and writing of pages into memory from
 * disk. Access methods call into it to retrieve pages, and it fetches
 * pages from the appropriate location.
 * <p>
 * The BufferPool is also responsible for locking;  when a transaction fetches
 * a page, BufferPool which check that the transaction has the appropriate
 * locks to read/write the page.
 */
public class BufferPool {
    /** Bytes per page, including header. */
    public static final int PAGE_SIZE = 4096;

    /** Default number of pages passed to the constructor. This is used by
    other classes. BufferPool should use the numPages argument to the
    constructor instead. */
    public static final int DEFAULT_PAGES = 50;

    /*
    private class CacheKey{
    	private int tabId;
    	private int pageId;
    	
    	public CacheKey(int tabId, int pageId) {
    		super();
    		this.tabId = tabId;
    		this.pageId = pageId;
    	}
    }
    
    private class CacheValue{
    	private int transactionId;
    	private Permissions p;
    	
    	public CacheValue(int transactionId, Permissions p) {
    		super();
    		this.transactionId = transactionId;
    		this.p = p;
    	}
    }
    */
    
    private int numPages;
    //private Map<CacheKey, CacheValue> hm = new HashMap<CacheKey, CacheValue>();
    private Map<HeapPage,List<SimpleEntry<Integer, Permissions>>> hm = new HashMap<HeapPage, List<SimpleEntry<Integer, Permissions>>>();
    private ArrayList<Integer> blocked = new ArrayList<Integer>();
    
    /**
     * Creates a BufferPool that caches up to numPages pages.
     *
     * @param numPages maximum number of pages in this buffer pool.
     */
    public BufferPool(int numPages) {
        // your code here
    	this.numPages = numPages;
    }
    
    //New method added
    public int getNumPages() {
    	return this.numPages;
    }
    
    //New method added
    public void setNumPages(int n) {
    	this.numPages = n;
    }
    
  //New method added
    public Map<HeapPage,List<SimpleEntry<Integer, Permissions>>> getHm() {
    	return this.hm;
    }
    
    //New method added
    public void setHm(Map<HeapPage,List<SimpleEntry<Integer, Permissions>>> hm) {
    	this.hm = hm;
    }
    
    //New method added
    public ArrayList<Integer> getBlocked() {
    	return this.blocked;
    }
    
    //New method added
    public void setBlocked(ArrayList<Integer> b) {
    	this.blocked = b;
    }
    
    /**
     * Retrieve the specified page with the associated permissions.
     * Will acquire a lock and may block if that lock is held by another
     * transaction.
     * <p>
     * The retrieved page should be looked up in the buffer pool.  If it
     * is present, it should be returned.  If it is not present, it should
     * be added to the buffer pool and returned.  If there is insufficient
     * space in the buffer pool, an page should be evicted and the new page
     * should be added in its place.
     *
     * @param tid the ID of the transaction requesting the page
     * @param tableId the ID of the table with the requested page
     * @param pid the ID of the requested page
     * @param perm the requested permissions on the page
     */
    public HeapPage getPage(int tid, int tableId, int pid, Permissions perm)
        throws Exception {
        // your code here
    	
    	HeapFile f = Database.getCatalog().getDbFile(tableId);
    	HeapPage hp = f.readPage(pid);
    	if(hp==null) {
    		return null;
    	}
    	if(hp.isDirty()) {
    		blocked.add(tid);
    		//Wait here
    		return null;
    		//Possible Deadlock
    	}
    	else {
    		//If requesting write access
    		if(perm.permLevel == 1) {
    			//Check in Cache
    			//Cache contains Page
    			
    			Iterator<Entry<HeapPage,List<SimpleEntry<Integer, Permissions>>>> it = getHm().entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry<HeapPage,List<SimpleEntry<Integer, Permissions>>> pair = (Map.Entry<HeapPage,List<SimpleEntry<Integer, Permissions>>>)it.next();
			        if (pair.getKey().equals(hp)) {
			        	System.out.println("Match");
			        }
			    }
    			if(getHm().containsKey(hp)) {
    				//Contains but no lock
    				if(getHm().get(hp).isEmpty()) {
    					hp.setDirty(true);
    					addToPool(tid, perm, hp);
    					return hp;
    				}
    				//Contains but only read locks exist
    				else{
    					if(getHm().get(hp).size()==1) {
    						if(getHm().get(hp).get(0).getKey().equals(tid)) {
    							Map<HeapPage,List<SimpleEntry<Integer, Permissions>>> myMap = getHm();
    					    	List<SimpleEntry<Integer, Permissions>> l = new ArrayList<SimpleEntry<Integer, Permissions>>();
    					    	l.clear();
    					    	l.add(new SimpleEntry<Integer, Permissions>(tid, perm));
    					    	myMap.put(hp, l);
    					    	hp.setDirty(true);
    							return hp;
    						}
    					}
    					blocked.add(tid);
    					//Wait here
    					return null;
    				}
    			}
    			//Page not in Cache
    			else {
    				hp.setDirty(true);
    				addToPool(tid, perm, hp);
    				return hp;
    			}
    		}
    		//Requesting read-only access
    		else {
    			//Add to Cache
    			addToPool(tid, perm, hp);
    			return hp;
    		}
    	}
    }
    
    //New Method added
    public void addToPool(int t, Permissions p, HeapPage h) throws Exception {
    	Map<HeapPage,List<SimpleEntry<Integer, Permissions>>> myMap = getHm();
    	List<SimpleEntry<Integer, Permissions>> l = new ArrayList<SimpleEntry<Integer, Permissions>>();
    	if(myMap.get(h)!=null) {
    		l = myMap.get(h);
	    	if(myMap.size()==getNumPages()) {
				Iterator<Entry<HeapPage,List<SimpleEntry<Integer, Permissions>>>> it = myMap.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry<HeapPage,List<SimpleEntry<Integer, Permissions>>> pair = (Map.Entry<HeapPage,List<SimpleEntry<Integer, Permissions>>>)it.next();
			        if (pair.getValue().isEmpty()) {
			        	it.remove();
			        	break;
			        }
			    }
	    	}
	    	if(myMap.size()==getNumPages()) {
	    		throw new Exception();
	    	}
    	}
    	l.add(new SimpleEntry<Integer,Permissions>(t, p));
    	myMap.put(h, l);
    	setHm(myMap);
    }

    /**
     * Releases the lock on a page.
     * Calling this is very risky, and may result in wrong behavior. Think hard
     * about who needs to call this and why, and why they can run the risk of
     * calling it.
     *
     * @param tid the ID of the transaction requesting the unlock
     * @param tableID the ID of the table containing the page to unlock
     * @param pid the ID of the page to unlock
     */
    public  void releasePage(int tid, int tableId, int pid) {
        // your code here
    	HeapFile f = Database.getCatalog().getDbFile(tableId);
    	HeapPage hp = f.readPage(pid);
    	Map<HeapPage,List<SimpleEntry<Integer, Permissions>>> myMap = getHm();
    	List<SimpleEntry<Integer, Permissions>> l = myMap.get(hp);
    	for(int i=0;i<l.size();i++) {
    		if(l.get(i).getKey()==tid) {
    			if(l.get(i).getValue().permLevel==1) {
    				hp.setDirty(false);
    			}
    			l.remove(i);
    			break;
    		}
    	}
    	myMap.put(hp, l);
    	setHm(myMap);
    }

    /** Return true if the specified transaction has a lock on the specified page */
    public   boolean holdsLock(int tid, int tableId, int pid) {
        // your code here
        return false;
    }

    /**
     * Commit or abort a given transaction; release all locks associated to
     * the transaction. If the transaction wishes to commit, write
     *
     * @param tid the ID of the transaction requesting the unlock
     * @param commit a flag indicating whether we should commit or abort
     */
    public   void transactionComplete(int tid, boolean commit)
        throws IOException {
        // your code here
    	
    	//if commit
	    	//list = getPages(tid)
	    	//for each page	
	    		//if page is dirty >>> flush + releasePage
	    		//if page is not dirty >>> releasePage
    	//if not commit
	    	//if page is dirty >>> remove page from map + releasePage
			//if page is not dirty >>> releasePage
    }
    
    
    //New method added for HW4
    public ArrayList<HeapPage> getPages(int tid){
    	//calculate pages locked by transaction
    	return null;
    }

    /**
     * Add a tuple to the specified table behalf of transaction tid.  Will
     * acquire a write lock on the page the tuple is added to. May block if the lock cannot 
     * be acquired.
     * 
     * Marks any pages that were dirtied by the operation as dirty
     *
     * @param tid the transaction adding the tuple
     * @param tableId the table to add the tuple to
     * @param t the tuple to add
     */
    public  void insertTuple(int tid, int tableId, Tuple t)
        throws Exception {
        // your code here
    	//find first page with empty slot
    	//is dirty?
    	HeapFile f = Database.getCatalog().getDbFile(tableId);
    	HeapPage h;
    	for (int i = 0; i < f.getNumPages(); i ++) {
    		HeapPage hp = f.readPage(i);
    		if(hp.getNumberOfEmptySlots()>0) {
    			h = getPage(tid, tableId, hp.getId(), Permissions.READ_WRITE);
    			if(h!=null) {
    				hm.put(h, getHm().get(h));
    				return;
    			}
    		}
    	}
    }

    /**
     * Remove the specified tuple from the buffer pool.
     * Will acquire a write lock on the page the tuple is removed from. May block if
     * the lock cannot be acquired.
     *
     * Marks any pages that were dirtied by the operation as dirty.
     *
     * @param tid the transaction adding the tuple.
     * @param tableId the ID of the table that contains the tuple to be deleted
     * @param t the tuple to add
     */
    public  void deleteTuple(int tid, int tableId, Tuple t)
        throws Exception {
        // your code here
    }

    private synchronized  void flushPage(int tableId, int pid) throws IOException {
        // your code here
    }

    /**
     * Discards a page from the buffer pool.
     * Flushes the page to disk to ensure dirty pages are updated on disk.
     */
    private synchronized  void evictPage() throws Exception {
        // your code here
    }

}
