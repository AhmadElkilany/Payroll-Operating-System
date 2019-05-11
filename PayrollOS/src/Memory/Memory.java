package Memory;
import java.util.concurrent.Semaphore;

import OS.Console;
import Processes.Process;

public class Memory { //First Fit
	
	private static MemSeg[] memory = new MemSeg[6];
	private static int totalUsedSpace;
	private static int totalAvailableSpace; 
	private static int totalSpace;
	
	public Memory(int[] segsizes) {
		Memory.totalUsedSpace = 0;
		for(int i=0; i<memory.length; i++) {
			memory[i] = new MemSeg(i+1,segsizes[i]);
			totalAvailableSpace += segsizes[i];
		}
		totalSpace=totalAvailableSpace;
	}
	
	public static int getTUS() {
		return totalUsedSpace;
	}
	
	public static int getTS() {
		return totalSpace;
	}

	public static void setTUS(int totalUsedSpace) {
		Memory.totalUsedSpace = totalUsedSpace;
	}

	public static int getTAS() {
		return totalAvailableSpace;
	}

	public static void setTAS(int totalAvailableSpace) {
		Memory.totalAvailableSpace = totalAvailableSpace;
	}
	
	public void assignMemory(Semaphore cons, Console c, Process p) {	
		try {
			cons.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c.print("Attempting to assign Process "+p.getProcessID()+ " into Memory");
		int x = p.getProcessSize();
		int i;
		for(i=0; i<memory.length;i++) {
			if(!memory[i].isOccupied()) {
				int y = memory[i].getSegsize();
				if(y>=x) {
					memory[i].setProcess(p);
					memory[i].setOccupied(true);
					setTUS(getTUS()+x);
					setTAS(getTAS()-x);
					break;
				}
			}
			
		}
		c.print("Assignment was successful. Process "+p.getProcessID()+ " was assigned to Segment("+i+") in the memory");
		cons.release();
		memoryStatistics(cons, c);
		
	}

	public void memoryStatistics(Semaphore cons, Console c) {
		try {
			cons.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c.printMem("Total Memory Size = "+this.totalSpace);
		c.printMem(" Used = "+totalUsedSpace);
		c.printNT(" Available = "+totalAvailableSpace);
		cons.release();
		this.print(cons, c);
	}
	
	public void emptyMemorySpace(Process p) {
		memory[p.getProcessMemoryAddress()].setOccupied(false);
	}
	
	public void print(Semaphore cons, Console c) {
		try {
			cons.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c.printMem(" --Memory-- ");
		for(int i=0; i<memory.length; i++) {
			if(i==(memory.length)-1) {
				c.printNT("Segment "+i+": "+ memory[i].showProcess());
			}else {
				c.printMem("Segment "+i+": "+ memory[i].showProcess());
			}
		}
		cons.release();
	}
	
	public boolean isFull() {
		for(int i=0; i<memory.length;i++) {
			if(!memory[i].isOccupied()) {
				return false;
			}
		}
		return true;
	}
	
	public float getMemoryUtilization() {
		//count occupied spaces
		//add sizes of processes in memory
		//average
		int seg=0;
		int process =0;
		for(int i=0; i<memory.length;i++) {
			seg+=memory[i].getSegsize();
			if(memory[i].isOccupied()) {
				process+=memory[i].getProcess().getProcessSize();
			}
		}
		if(seg==0 && process==0) {
			return 0;
		}
		return (process/seg)*100; //percentage
	}

}
