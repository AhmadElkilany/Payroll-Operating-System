package Memory;
import Processes.Process;

public class MemSeg {
	
	private int segnum;
	private int segsize;
	private boolean occupied;
	private Process process; //contains objects stored in this segment (Processes, objects,...)
	
	public MemSeg(int segnum, int segsize) {
		this.segnum = segnum;
		this.segsize = segsize;
		this.occupied=false;
	}
	
	public int getSegnum() {
		return segnum;
	}
	
	public void setSegnum(int segnum) {
		this.segnum = segnum;
	}
	
	public int getSegsize() {
		return segsize;
	}
	
	public void setSegsize(int segsize) {
		this.segsize = segsize;
	}

	public void setProcess(Process p) {
		this.process = p;
	}
	
	public Process getProcess() {
		return this.process;
	}
	
	public boolean isOccupied() {
		return occupied;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	public String showProcess() {
		if(process==null) {
			return "-empty-";
		}else {
			return "Process "+process.getProcessID();
		}
	}

}
