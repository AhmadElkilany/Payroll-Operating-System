package Processes;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.concurrent.Semaphore;

import OS.Console;

public class LogWorkingHours extends Process {
	
	int newHours;

	public LogWorkingHours(int processSize, int processArrivalTime, int processBurstTime, Color color) {
		super(processSize, processArrivalTime, processBurstTime, color);
		// TODO Auto-generated constructor stub
		this.processType=1;
	}
	
	//---------------------------------GETTERS-----------------------------------------
	
	public int getProcessID() {
		return processID;
	}

	public int getProcessMemoryAddress() {
		return processMemoryAddress;
	}
	
	public int getProcessSize() {
		return processSize;
	}

	public State getProcessStatus() {
		return processStatus;
	}
	
	public int getProcessArrivalTime() {
		return processArrivalTime;
	}

	public int getProcessBurstTime() {
		return processBurstTime;
	}

	public int getProcessRemainingTime() {
		return processRemainingTime;
	}
	
	public int getProcessFirstResponseTime() {
		return processFirstResponseTime;
	}

	public int getProcessWaitingTime() {
		return processWaitingTime;
	}

	public int getProcessLastResponseTime() {
		return processLastResponseTime;
	}

	public Color getProcessColor() {
		return processColor;
	}

	//---------------------------------SETTERS-----------------------------------------
	
	public void setNewHours(int newHours) {
		this.newHours = newHours;
	}
	
	public void setProcessStatus(State processStatus) {
		this.processStatus = processStatus;
	}

	public void setProcessMemoryAddress(int processMemoryAddress) {
		this.processMemoryAddress = processMemoryAddress;
	}
	
	public void setProcessRemainingTime(int processRemainingTime) {
		this.processRemainingTime = processRemainingTime;
	}
	
	public void setProcessFirstResponseTime(int t) {
		this.processFirstResponseTime = t;
	}
	
	public void setProcessLastResponseTime(int t) {
		this.processLastResponseTime = t;
	}
	
	public void setProcessWaitingTime(int t) {
		this.processWaitingTime = t+1-processBurstTime-processArrivalTime;
	}
	
	//---------------------------OTHER_METHODS-----------------------------------------------
	
	public void Decrement(){
		processRemainingTime--;
		processBar++;
	}
	
	public void newSetWaitTime(int t){
    	processWaitingTime=t+1-processBurstTime-processArrivalTime;
    	
	}
	
	//---------------------------------------------------------------------------------
	
	public void run(String userName, Console c, Semaphore cons, Semaphore file) { //if any process tried to run while another process is in the running queue will place in ready queue
		try {
			cons.acquire();
			c.print("Process "+this.getProcessID()+" has acquired the Console Semaphore");
			c.print("Running Process "+this.getProcessID()+" (Logging Working Hours)");
			// ---------------------Update data in file-------------
			BufferedWriter writer = null;
			BufferedReader reader2 = null;
	        try {
	        	file.acquire();
				c.print("Process "+this.getProcessID()+" has acquired the TextFile Semaphore to fetch data");
	        	reader2 = new BufferedReader(new FileReader("C:\\Users\\LENOVO\\Desktop\\file.txt"));
	        	file.release();
				c.print("Process "+this.getProcessID()+" has released the TextFile Semaphore");
				String line2 = reader2.readLine();
				String text = line2;
				line2 = reader2.readLine();
				while (line2 != null) {
					String[] words=line2.split("\\s");
					if(words[0].equals(userName)) {
						line2 = words[0]+" "+this.newHours+" "+words[2]+" "+words[3];
						text+= "\n"+line2;
					}else {
						text+= "\n"+line2;
					}
					line2 = reader2.readLine();
				}
				file.acquire();
				c.print("Process "+this.getProcessID()+" has acquired the TextFile Semaphore to edit data");
	            writer = new BufferedWriter(new FileWriter("C:\\Users\\LENOVO\\Desktop\\file.txt"));
	            writer.write(text);
	            file.release();
	            c.print(userName+"'s Working Hours have been successfully updated.");
				c.print("Process "+this.getProcessID()+" has released the TextFile Semaphore");
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                reader2.close();
	                writer.close();
	            } catch (Exception e) {
	            }
	        }
			
	        c.print("Process "+this.getProcessID()+" was "+this.getProcessStatus());
			c.print("Terminating Process "+this.getProcessID());
			this.setProcessStatus(State.FINISHED);
			c.print("Process "+this.getProcessID()+" is now "+this.getProcessStatus());
			c.print("Process "+this.getProcessID()+" is releasing the Console Semaphore");
			cons.release();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int getProcessType() {
		return this.processType;
	}

}
