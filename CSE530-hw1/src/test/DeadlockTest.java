package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import hw1.Actions;
import hw1.BufferPool;
import hw1.Catalog;
import hw1.Database;
import hw1.HeapFile;
import hw1.HeapPage;
import hw1.IntField;
import hw1.Permissions;
import hw1.StringField;
import hw1.Transaction;
import hw1.Tuple;
import hw1.TupleDesc;

public class DeadlockTest {
	
	private Catalog c;
	private BufferPool bp;
	private HeapFile hf;
	private TupleDesc td;
	private int tid;
	
	//Inner Class created to pass all data to a transaction 
	//Note: Transaction ID is auto-incremental and generated in Transaction class
	
	public class Trans{
		private Actions a;
		private Tuple tup;
		private int table;
		private int page;
		
		public Actions getA() {
			return a;
		}
		public void setA(Actions a) {
			this.a = a;
		}
		public Tuple getTup() {
			return tup;
		}
		public void setTup(Tuple tup) {
			this.tup = tup;
		}
		public int getTable() {
			return table;
		}
		public void setTable(int table) {
			this.table = table;
		}
		public int getPage() {
			return page;
		}
		public void setPage(int page) {
			this.page = page;
		}
	}
	
	
	@Before
	public void setup() {
		
		try {
			Files.copy(new File("testfiles/test.dat.bak").toPath(), new File("testfiles/test.dat").toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			System.out.println("unable to copy files");
			e.printStackTrace();
		}
		
		c = Database.getCatalog();
		c.loadSchema("testfiles/test.txt");
		
		int tableId = c.getTableId("test");
		td = c.getTupleDesc(tableId);
		hf = c.getDbFile(tableId);
		
		bp = Database.getBufferPool();
		
		tid = c.getTableId("test");
		
	}
	
	
	//This function starts one transaction which should acquire lock and insert one tuple.
	@Test
	public void testSingleTransaction() throws Exception {
		Transaction t = new Transaction();
		
		List<Trans> schedule = new ArrayList<Trans>();
		
		//Standard Tuple to insert
		Tuple tup = new Tuple(Database.getCatalog().getTupleDesc(tid));
		tup.setField(0, new IntField(new byte[] {0, 0, 0, (byte)131}));
		byte[] s = new byte[129];
		s[0] = 2;
		s[1] = 98;
		s[2] = 121;
		tup.setField(1, new StringField(s));
		
		Trans temp = new Trans();
		temp.a = Actions.FETCH;
		temp.table = tid;
		schedule.add(temp);
		
		temp = new Trans();
		temp.a = Actions.INSERT;
		temp.tup = tup;
		temp.table = tid;
		temp.page = 0;
		schedule.add(temp);
		
		temp = new Trans();
		temp.a = Actions.COMPLETE;
		schedule.add(temp);
		
		t.setSchedule(schedule);
		t.perform();
		while(t.isAlive()) {
		}
	    assertTrue(true); //will only reach this point if locks are properly released
	}
	
	//This test contains two transactions which are started on 2 different threads.
	//Both the (transactions)threads insert one tuple each on the same page of the same table.
	//After the first transaction has acquired lock, the second one waits till the first transaction is completed
	//If the second one does not get the lock for 15 wait intervals, it is aborted.(In this case, the lock is acquired)
	@Test
	public void testMultipleTransactionSimple() throws Exception {
		
		//Standard Tuple to insert
		Tuple tup = new Tuple(Database.getCatalog().getTupleDesc(tid));
		tup.setField(0, new IntField(new byte[] {0, 0, 0, (byte)131}));
		byte[] s = new byte[129];
		s[0] = 2;
		s[1] = 98;
		s[2] = 121;
		tup.setField(1, new StringField(s));
		
		
		Transaction t1 = new Transaction();
		List<Trans> schedule1 = new ArrayList<Trans>();
		
		Trans temp1 = new Trans();
		temp1.a = Actions.FETCH;
		temp1.table = tid;
		schedule1.add(temp1);
		
		temp1 = new Trans();
		temp1.a = Actions.INSERT;
		temp1.tup = tup;
		temp1.table = tid;
		temp1.page = 0;
		schedule1.add(temp1);
		
		temp1 = new Trans();
		temp1.a = Actions.COMPLETE;
		schedule1.add(temp1);
		
		t1.setSchedule(schedule1);
		
		Transaction t2 = new Transaction();
		List<Trans> schedule2 = new ArrayList<Trans>();
		
		Trans temp2 = new Trans();
		temp2.a = Actions.FETCH;
		temp2.table = tid;
		schedule2.add(temp2);
		
		temp2 = new Trans();
		temp2.a = Actions.INSERT;
		temp2.tup = tup;//Same tuple
		temp2.table = tid;
		temp2.page = 0;
		schedule2.add(temp2);
		
		temp2 = new Trans();
		temp2.a = Actions.COMPLETE;
		schedule2.add(temp2);
		
		t2.setSchedule(schedule2);
		
		t1.perform();
		t2.perform();
		
		while(t1.isAlive() || t2.isAlive()) {
		}
		
	    assertTrue(true); //will only reach this point if locks are properly released
	}
	
	//This test contains two transactions which are started on 2 different threads.
	//Both the (transactions)threads insert one tuple each on the same page of the same table.
	//After the first transaction has acquired lock, the second one waits till the first transaction is completed
	//If the second one does not get the lock for 15 wait intervals, it is aborted.(In this case, the lock is NOT acquired : T1 is not completed)
	@Test
	public void testMultipleTransactionIncomplete() throws Exception {
		//Standard Tuple to insert
		Tuple tup = new Tuple(Database.getCatalog().getTupleDesc(tid));
		tup.setField(0, new IntField(new byte[] {0, 0, 0, (byte)131}));
		byte[] s = new byte[129];
		s[0] = 2;
		s[1] = 98;
		s[2] = 121;
		tup.setField(1, new StringField(s));
		
		
		Transaction t1 = new Transaction();
		List<Trans> schedule1 = new ArrayList<Trans>();
		
		Trans temp1 = new Trans();
		temp1.a = Actions.FETCH;
		temp1.table = tid;
		schedule1.add(temp1);
		
		temp1 = new Trans();
		temp1.a = Actions.INSERT;
		temp1.tup = tup;
		temp1.table = tid;
		temp1.page = 0;
		schedule1.add(temp1);
		
		//temp1 = new Trans();
		//temp1.a = Actions.COMPLETE;
		//schedule1.add(temp1);
		
		t1.setSchedule(schedule1);
		
		Transaction t2 = new Transaction();
		List<Trans> schedule2 = new ArrayList<Trans>();
		
		Trans temp2 = new Trans();
		temp2.a = Actions.FETCH;
		temp2.table = tid;
		schedule2.add(temp2);
		
		temp2 = new Trans();
		temp2.a = Actions.INSERT;
		temp2.tup = tup;//Same tuple
		temp2.table = tid;
		temp2.page = 0;
		schedule2.add(temp2);
		
		temp2 = new Trans();
		temp2.a = Actions.COMPLETE;
		schedule2.add(temp2);
		
		t2.setSchedule(schedule2);
		
		t1.perform();
		t2.perform();
		
		while(t1.isAlive() || t2.isAlive()) {
		}
		
	    assertTrue(true); //will only reach this point if locks are properly released
	}
}