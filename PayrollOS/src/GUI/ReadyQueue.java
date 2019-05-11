package GUI;
import java.util.LinkedList;
import java.util.Queue;

import Processes.Process;

public class ReadyQueue {
	
	Queue<Process> readyQueue,tempQueue;
	
	public ReadyQueue(){
		readyQueue = new LinkedList<Process>();
		tempQueue = new LinkedList<Process>();	
	}
	
	public void InsertJob(Process p){
		readyQueue.add(p);
		tempQueue.add(p);
	}

}

