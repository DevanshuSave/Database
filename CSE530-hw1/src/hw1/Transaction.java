package hw1;

import java.util.List;

import test.DeadlockTest.Trans;

public class Transaction extends Thread {
	
	private int tid;
	private static int count = 0;
	private boolean blocked;
	private BufferPool bp = Database.getBufferPool();
	
	private List<Trans> schedule;
	
	public Transaction() {
		this.tid = count;
		this.blocked = false;
		count ++;
	}
	
	public int getTID() {
		return this.tid;
	}
	
	public boolean getBlocked() {
		return this.blocked;
	}
	
	public void setBlocked(boolean b) {
		this.blocked = b;
	}
	
	
	public List<Trans> getSchedule() {
		return this.schedule;
	}
	
	public void setSchedule(List<Trans> sch) {
		this.schedule = sch;
	}
	
	public void perform() {
		start();
	}
	
	public void run() {
		List<Trans> sch = getSchedule();
		if(sch.isEmpty()) {
			return;
		}
		int count = 0;
		for (int i =0;i<sch.size();i++) {
			System.out.println("Current Thread:"+getTID());
			Trans x = sch.get(i);
			if(x.getA() == Actions.FETCH) {
				try {
					bp.getPage(getTID(), x.getTable(), x.getPage(), Permissions.READ_ONLY);
				}
				catch (Exception e) {
					System.out.println("Lock not granted");
					this.setBlocked(true);
				}
			}
			else if (x.getA() == Actions.INSERT) {
				try {
					bp.insertTuple(getTID(), x.getTable(), x.getTup());
				}
				catch (Exception e) {
					System.out.println("Lock not granted ++");
					this.setBlocked(true);
				}
			}
			else if(x.getA() == Actions.DELETE) {
				try {
					bp.deleteTuple(getTID(), x.getTable(), x.getTup());
				}
				catch (Exception e) {
					System.out.println("Lock not granted --");
					this.setBlocked(true);
				}
			}
			else if(x.getA() == Actions.COMPLETE) {
				try {
					bp.transactionComplete(getTID(), true);
				}
				catch (Exception e) {
					System.out.println("Lock not granted ***");
					this.setBlocked(true);
				}
			}
			else {
				System.out.println("Incorrect Input");
			}
			if(Database.getBufferPool().getBlocked().contains(getTID())) {
				try {
					sleep(500);
					System.out.println("Waiting"+getTID());
					count++;
					i--;
					if(count==15) {
						bp.transactionComplete(getTID(), false);
						break;
					}
					System.out.println("Count"+count);
				}
				catch (Exception e) {
					System.out.println("Thread mishandled");
				}
			}
		}
	}
}