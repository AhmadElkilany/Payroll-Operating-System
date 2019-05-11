package Processes;
import java.awt.Color;
import java.util.concurrent.Semaphore;

import OS.Console;

public abstract class Process { 
		
		public static int counter = 1;
		protected int processID; //Id
		protected int processType; // 1=log, 2=loan, 3=print
		protected int processSize;
		protected int processArrivalTime; //ATime
		protected int processBurstTime; //CpuBurst
		protected int processRemainingTime; //RTime
		protected int processWaitingTime; //WTime
		protected int processFirstResponseTime; //IstRTime
		protected int processLastResponseTime; //LstRTime
		protected State processStatus;
		protected int processMemoryAddress; //MemSeg number
		public int processBar=0; //pb
		protected Color processColor; //color
		
		//---------------------------------CONSTRUCTOR----------------------------------------------
		
		public Process(int processSize, int processArrivalTime, int processBurstTime, Color color) { 
			this.processID = counter;
			counter++;
			this.processSize = processSize;
			this.processStatus = State.NEW;
			this.processArrivalTime = processArrivalTime;
			this.processBurstTime = processBurstTime;
			this.processRemainingTime = processBurstTime;
			this.processColor = color;
		}

		//---------------------------------GETTERS----------------------------------------------
		
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

		/*public int getProcessLastBurstTime() {
			return processLastBurstTime;
		}*/

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
		
		public abstract void run(String userName, Console c, Semaphore cons, Semaphore file);

		public abstract int getProcessType();
		
		
}
