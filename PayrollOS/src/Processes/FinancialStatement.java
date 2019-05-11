package Processes;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Semaphore;

import OS.Console;

public class FinancialStatement extends Process {

	public FinancialStatement(int processSize, int processArrivalTime, int processBurstTime, Color color) {
		super(processSize, processArrivalTime, processBurstTime, color);
		// TODO Auto-generated constructor stub
		this.processType=3;
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
				c.print("Running Process "+this.getProcessID()+" (Financial Statement Printing)");
				// ---------------------Fetching data from file-------------
				int userWH = 0, userSR = 0, userLoan = 0;
				BufferedReader reader;
				file.acquire();
				c.print("Process "+this.getProcessID()+" has acquired the TextFile Semaphore to fetch data");
				reader = new BufferedReader(new FileReader("C:\\Users\\LENOVO\\Desktop\\file.txt"));
				file.release();
				c.print("Process "+this.getProcessID()+" has released the TextFile Semaphore");
				String line = reader.readLine();
				while (line != null) {
					String[] words=line.split("\\s");
					if(words[0].equals(userName)) {
						userWH=Integer.parseInt(words[1]);
						userLoan=Integer.parseInt(words[2]);
						userSR=Integer.parseInt(words[3]);
						break;
					}else {
						line = reader.readLine();
					}
				}	
				reader.close();
				//------------------Print statement--------------------
				c.print(userName+"--> currently owes $"+userLoan+" as loan. You've worked for "+userWH+" hours so far. Your current salary is $"+userWH*userSR);
				c.print("Process "+this.getProcessID()+" was "+this.getProcessStatus());
				c.print("Terminating Process "+this.getProcessID());
				this.setProcessStatus(State.FINISHED);
				c.print("Process "+this.getProcessID()+" is now "+this.getProcessStatus());
				c.print("Process "+this.getProcessID()+" is releasing the Console Semaphore");
				cons.release();
				
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public int getProcessType() {
			return this.processType;
		}


}
