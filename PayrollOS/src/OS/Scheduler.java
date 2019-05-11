package OS;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import GUI.Bg;
import GUI.CPUChart;
import GUI.PaintReadyQueue;
import GUI.ProgressBar;
import Memory.Memory;
import Processes.FinancialStatement;
import Processes.LoanPayment;
import Processes.LogWorkingHours;
import Processes.Process;
import Processes.State;
import Processes.FinancialStatement.*;
import OS.Console;

public class Scheduler extends JFrame {

	static Console MyConsole;
	Semaphore cons = new Semaphore(1);
	Semaphore file = new Semaphore(1);
	static Memory MyMemory;
	JFrame f1,f2,finput,f3, whinput;
	JRadioButton r1;
	JButton jb1,add,start,insert,done,colorc;
	JButton statement, loan, workinghours;
	JButton start2;
	JLabel avgTime, avgTimeLabel, avgTurn, avgTurnLabel, avgRes, avgResLabel;
	JLabel memUtil, memUtilLabel;
	JTable	table;
	JScrollPane scrollPane,scrollPane2;
	String dataValues[][], readyData[][];
	JTextField txt1=new JTextField(), txt2=new JTextField(), txt3=new JTextField();
	int c,readyc;//counter
	float AvgWaitTime=0, AvgTurnTime=0, AvgResTime=0, memUsed, procInMem;
	float displayResult=0, displayResult2=0, displayResult3=0;
	int totalTime=0;
	String columnNames[] = {"PROCESS", "BURST","ARRIVAL" };
	Color color;
	PaintReadyQueue prqObj;
	CPUChart gcObj;
	ProgressBar pbObj;
	int i = 0; //arrival time counter
	static String userName;
	static int userWH, userLoan, userSR, userSalary;
	
	public Queue<Process> JobQueue,ReadyQueue;
	
	public Scheduler(){
		c=0;// counter
		color=Color.WHITE; 
		f1=new JFrame();
		f1.setSize(630, 450);
		f1.setVisible(true);
		f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f1.setResizable(false);
		f1.setLocationRelativeTo(null);
		f1.setLayout(null);
		f1.setTitle("Payroll OS");
		
		f2=new JFrame();
		f2.setSize(800, 430);
		f2.setVisible(false);
		f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f2.setResizable(false);
		f2.setLocationRelativeTo(null);
		f2.setLayout(null);
		f2.setTitle("Adding Processes");
		
		f3=new JFrame();
		f3.setSize(900, 555);
		f3.setVisible(false);
		f3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f3.setResizable(false);
		f3.setLocationRelativeTo(null);
		f3.setLayout(null);
		f3.setTitle("Running OS");
		
		int[] n = {10,10,10,10,10,10}; // --> memory segment sizes
		MyMemory=new Memory(n);
		MyConsole=new Console();
		MyConsole.printNT("Welcome to the OS!");
		MyConsole.printMem("Specs:");
		MyConsole.printMem("Process Execution --> FCFS"); 
		MyConsole.printNT("Memory Assigment --> First Fit");
		MyConsole.printNT("Please choose your name from the dropdown list:");
		load1();
		load2();
		
		f1.validate();
	}
	
	public void load1(){
		try{
			BufferedImage bf = ImageIO.read(new File("./resources/bgmain.jpg"));  
			f1.setContentPane(new Bg(bf));
			
			
		}catch(Exception e){
			
		}
		ArrayList<String> temp = getUsers();
		int l = temp.size();
		String[] names = new String[l];
		l=0;
		while(!temp.isEmpty()) {
			names[l]=temp.remove(0);
			l++;
		}
		JComboBox<String> dropDown = new JComboBox<String>(names);
		dropDown.setBounds(170, 320, 100, 25);
		dropDown.setForeground(Color.BLACK);
		dropDown.setFont(new Font("Garamond", Font.BOLD , 15));
		dropDown.setEditable(true);
		jb1=new JButton("Begin");
		jb1.setBackground(new Color(51, 153, 255));
		jb1.setBounds(400, 300, 150, 50);
		jb1.setForeground(Color.WHITE);
		jb1.setFont(new Font("Garamond", Font.BOLD , 30));
		jb1.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				f1.dispose();
				userName = (String) dropDown.getSelectedItem();
				try {
					cons.acquire();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				MyConsole.printNT("Welcome "+userName+"!");
				cons.release();
				getUserData(userName);
				f2.setVisible(true);
			}
			
		});
		
		f1.add(jb1);
		f1.add(dropDown);
		f1.validate();
	}
	
	public void load2(){
		
		JobQueue=new LinkedList<Process>();
		ReadyQueue=new LinkedList<Process>();
		
		try{
			BufferedImage bf = ImageIO.read(new File("./resources/3.jpg"));  
			f2.setContentPane(new Bg(bf));
			
		}catch(Exception e){
			
		}
		 dataValues=new String[10][4];
		 inData(); // --> initialize  dataValues to empty strings
		 table = new JTable( dataValues, columnNames );
		 table.setFont(new Font("Garamond",  Font.BOLD , 20));
		 table.setEnabled(false);

		 TableColumnModel columnModel = table.getColumnModel();
		 columnModel.getColumn(0).setPreferredWidth(320);
		 columnModel.getColumn(1).setPreferredWidth(60);
		 columnModel.getColumn(2).setPreferredWidth(70);
		 
		 table.setRowHeight(30);
		 scrollPane = new JScrollPane( table );
		 scrollPane.setBounds(20, 35, 550, 325);
		 
		 
		//---------Working_Hours_Process(blue)-----------
			
		workinghours=new JButton("Log Hours");
		workinghours.setBackground(new Color(39, 47, 156));
		workinghours.setForeground(Color.WHITE);
		workinghours.setFont(new Font("Garamond", Font.BOLD, 21));
		workinghours.setBounds(600, 50, 150, 50);
		workinghours.addActionListener(new ActionListener(){
			 
			public void actionPerformed(ActionEvent e){
				Random rand = new Random();
				int randomNum = rand.nextInt((8 - 2) + 1) + 2;
				userWH+= randomNum;  
				txt1.setText(Integer.toString(Process.counter));
				txt2.setText(""+i);
				i++;
				txt3.setText(""+3); //burst for this is 3 units
				color=new Color(0, 102, 204);
				loadData("wh");
				f2.add(scrollPane);
				txt1.setText(Integer.toString(Process.counter));
				txt2.setText("");
				txt3.setText("");	
				}
				
		 });
			
		//--------Printing_Statement(orange)----------------
			
		statement=new JButton("Print Report");
		statement.setBackground(new Color(39, 47, 156));
		statement.setForeground(Color.WHITE);
		statement.setFont(new Font("Garamond", Font.BOLD, 21));
		statement.setBounds(600, 200, 150, 50); 
		statement.addActionListener(new ActionListener(){
			 
			public void actionPerformed(ActionEvent e){
				userSalary=userWH*userSR;
				txt1.setText(Integer.toString(Process.counter));
				txt2.setText(""+i);
				i++;
				txt3.setText(""+5); //burst for this is 5 unit
				color=new Color(255, 152, 51);
				loadData("ps");
				f2.add(scrollPane);
				txt1.setText(Integer.toString(Process.counter));
				txt2.setText("");
				txt3.setText("");	
			}
					
		 });
			
		//--------Loan_Payment(red)----------------
			
		loan=new JButton("Pay Loan");
		loan.setBackground(new Color(39, 47, 156));
		loan.setForeground(Color.WHITE);
		loan.setFont(new Font("Garamond", Font.BOLD, 21));
		loan.setBounds(600, 125, 150, 50); 
		loan.addActionListener(new ActionListener(){
			 
			public void actionPerformed(ActionEvent e){
				if(userLoan==0) {
					JOptionPane.showMessageDialog(null, "No loan to pay.");
					return;
				}
				int pay = Integer.parseInt(JOptionPane.showInputDialog(null, "Please enter amount you want to pay:"));				
				while(pay>userLoan) {										
					JOptionPane.showMessageDialog(null, "You only owe $"+userLoan+". Please pay that amount or less only.");																			//3. show how much is due and take amount to pay
					pay = Integer.parseInt(JOptionPane.showInputDialog(null, "Please enter amount you want to pay:"));	 
				}
				userLoan-= pay;
				txt1.setText(Integer.toString(Process.counter)); //job number
				txt2.setText(""+i); 
				i++;
				txt3.setText(""+4); //burst for this is 5 unit
				color=new Color(204, 0, 0);
				loadData("lp");
				f2.add(scrollPane);
				txt1.setText(Integer.toString(Process.counter));
				txt2.setText("");
				txt3.setText("");	
			}
					
		 });
			
		 //-----------Start_Executing--------------------------------
			
		 start=new JButton("START");
		 start.setBackground(new Color(39, 47, 156));
		 start.setForeground(Color.WHITE);
		 start.setFont(new Font("Garamond", Font.BOLD, 30));
		 start.setBounds(600, 275, 150, 75);
		 start.addActionListener(new ActionListener(){
			 
			 public void actionPerformed(ActionEvent e){
				 prqObj=new PaintReadyQueue(totalTime);
				 gcObj=new CPUChart(totalTime);
				 pbObj=new ProgressBar(c,JobQueue);
				 load3();
				 f3.setVisible(true);
				 f2.setVisible(false);
				 FCFS();
			 }
		 
		 });
		 
		 f2.add(scrollPane);
		 f2.add(workinghours);
		 f2.add(statement);
		 f2.add(loan);
		 f2.add(start);
		 f2.validate();
	}
	
	public void load3(){
		
		try{
			BufferedImage bf = ImageIO.read(new File("./resources/3.jpg"));  
			f3.setContentPane(new Bg(bf));
		}catch(Exception e){
			
		}
		 
		 start2=new JButton("BEGIN");
		 start2.setBounds(10, 10, 150, 50);
		 scrollPane2 = new JScrollPane( table );
		 
		 //--------Analytics------------
		 
		 avgTime=new JLabel();
		 avgTimeLabel = new JLabel();
		 avgTurn = new JLabel();
		 avgTurnLabel = new JLabel();
		 avgRes = new JLabel();
		 avgResLabel = new JLabel();
		 
		 avgTime.setForeground(new Color (255, 255, 255));
		 avgTimeLabel.setForeground(new Color (255, 255, 255));
		 avgTurn.setForeground(new Color (255, 255, 255));
		 avgTurnLabel.setForeground(new Color (255, 255, 255));
		 avgRes.setForeground(new Color (255, 255, 255));
		 avgResLabel.setForeground(new Color (255, 255, 255));
		 
		 avgTime.setFont(new Font("SansSerif", Font.BOLD, 15));
		 avgTimeLabel.setFont(new Font("SansSerif", Font.ITALIC, 15));
		 avgTurn.setFont(new Font("SansSerif", Font.BOLD, 20));
		 avgTurnLabel.setFont(new Font("SansSerif", Font.ITALIC, 15));
		 avgRes.setFont(new Font("SansSerif", Font.BOLD, 20));
		 avgResLabel.setFont(new Font("SansSerif", Font.ITALIC, 15));
		 
		 
		 //-------Setting Bounds----------
		 
		 avgTurn.setBounds(204, 170, 200, 30);
		 avgTurnLabel.setBounds(204, 140, 200, 30);
		 avgRes.setBounds(204, 240, 200, 30);
		 avgResLabel.setBounds(204, 210, 200, 30);
		 scrollPane2.setBounds(503, 50, 360, 325); //process table
		 prqObj.setBounds(204, 50, 280, 80); // ready queue
		 gcObj.setBounds(20, 395, 847, 100); //cpu chart
		 pbObj.setBounds(20, 50, 165, 325); //progress bars
		 
		 
		 start2.addActionListener(new ActionListener(){
			 
			 public void actionPerformed(ActionEvent e){
				FCFS();
			 }
			 
		 });
		 
		 //---------Filling Final Frame------
		 
		 memUtil =new JLabel();
		 memUtil.setBackground(new Color(39, 47, 156));
		 memUtil.setForeground(Color.WHITE);
		 memUtil.setFont(new Font("SansSerif", Font.BOLD, 20));
		 memUtil.setBounds(204, 310, 200, 30);
		 
		 memUtilLabel =new JLabel();
		 memUtilLabel.setBackground(new Color(39, 47, 156));
		 memUtilLabel.setForeground(Color.WHITE);
		 memUtilLabel.setFont(new Font("SansSerif", Font.ITALIC, 15));
		 memUtilLabel.setBounds(204, 280, 200, 30);
		 
		 JLabel label =new JLabel();
		 label.setText("Payroll Operating System - Behind The Scenes");
		 label.setBackground(new Color(39, 47, 156));
		 label.setForeground(Color.WHITE);
		 label.setFont(new Font("Helvetica", Font.BOLD, 25));
		 label.setBounds(20, 10, 800, 30);
		 
		 f3.add(memUtil);
		 f3.add(memUtilLabel);
		 f3.add(label);
		 f3.add(avgTurn);
		 f3.add(avgTurnLabel);
		 f3.add(avgRes);
		 f3.add(avgResLabel);
		 
		 f3.add(gcObj);
		 f3.add(scrollPane2);
		 f3.add(prqObj);
		 f3.add(pbObj);
		 f3.validate();
		 f3.revalidate();
	}
	
	public static void getUserData(String name) { //--> updates user static fields with appropriate info
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("C:\\Users\\LENOVO\\Desktop\\file.txt"));
			String line = reader.readLine();
			boolean flag=false;
			while (line != null) {
				String[] words=line.split("\\s");
				if(words[0].equals(name)) {
					userWH=Integer.parseInt(words[1]);
					userLoan=Integer.parseInt(words[2]);
					userSR=Integer.parseInt(words[3]);
					flag=true;
					break;
				}else {
					line = reader.readLine();
				}
				
			}
			if(!flag) {
				System.out.println("Not an Employee");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> getUsers() { //--> gets all employees from txt file
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("C:\\Users\\LENOVO\\Desktop\\file.txt"));
			ArrayList<String> names = new ArrayList<String>();
			String line = reader.readLine(); //remove headers
			line = reader.readLine();
			while (line != null) {
				String[] words=line.split("\\s");
				names.add(words[0]);
				line = reader.readLine();
			}
			reader.close();
			return names;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void loadData(String type){
		String str1=txt1.getText();
		String str2=txt2.getText();
		String str3=txt3.getText();
		
		//check if there is enough space in memory to create another process
		if(MyMemory.isFull()) {
			try {
				cons.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			MyConsole.print("Memory cannot fit anymore processes.");
			cons.release();
			JOptionPane.showMessageDialog(null, "That's enough jobs for now. Press Start to begin!");
			return;
		}
		
		if(type.equals("lp")) {
			dataValues[c][0]="Paying Loan Instalment";
		}
		if(type.equals("ps")) {
			dataValues[c][0]="Printing Financial Statement";
		}
		if(type.equals("wh")) {
			dataValues[c][0]="Logging Working Hours";
		}
		
		dataValues[c][1]=str3;
		dataValues[c][2]=str2;
		totalTime=totalTime+ Integer.parseInt(str3); //test here
		InsertJob(type, 10, Integer.parseInt(str2), Integer.parseInt(str3), color); //by default all processes have size 10
		c++;
		color=Color.WHITE;
	}
	
	public void inData(){
		for(int i=0;i<10;i++){
			dataValues[i][0]="";
			dataValues[i][1]="";
			dataValues[i][2]="";
		}
	}
	
	public void InsertJob(String type, int size,int atime,int cputime,Color c) { // --> insert process in jobpool
		Process p = null;
		if(type.equals("wh")) {
			p = new LogWorkingHours(size,atime,cputime,c);
			JobQueue.add(p);
		}
		if(type.equals("lp")) {
			p = new LoanPayment(size,atime,cputime,c);
			JobQueue.add(p);
		}
		if(type.equals("ps")) {
			p = new FinancialStatement(size,atime,cputime,c);
			JobQueue.add(p);
		}
		MyMemory.assignMemory(cons, MyConsole, p);
		try {
			cons.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		MyConsole.print("Process "+p.getProcessID()+" was "+p.getProcessStatus());
		p.setProcessStatus(State.READY);
		MyConsole.print("Process "+p.getProcessID()+" is now "+p.getProcessStatus());
		cons.release();
	}
	
	public void updateReadyQueue(int t){	// --> Updates the ReadyQueue
		Process temp;
		for(int i=0;i<JobQueue.size();i++){
			temp=JobQueue.remove();
			if(temp.getProcessArrivalTime()==t){
				insertProcess(temp); // --> Insert The Process To Ready Queue
				JobQueue.add(temp);
			}else{
				JobQueue.add(temp);
			}
			
		}
	}
	
	public void insertProcess(Process p){ // --> Inserts process into the ReadyQueue
		ReadyQueue.add(p);
	}
	
	public void FCFS(){
		int n=JobQueue.size();
		Process CpuProcess;
		CpuProcess=null;// Intilize null process
		for(int i=0;i<totalTime;i++){
			updateReadyQueue(i);
			allQueue(i);
			if(CpuProcess==null){
				if(!ReadyQueue.isEmpty()){
					CpuProcess=ReadyQueue.remove();
					try {
						cons.acquire();
						MyConsole.print("Process "+CpuProcess.getProcessID()+" was "+CpuProcess.getProcessStatus());
						CpuProcess.setProcessStatus(State.RUNNING);
						MyConsole.print("Process "+CpuProcess.getProcessID()+" is now "+CpuProcess.getProcessStatus());
						cons.release();
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
					
					
					CpuProcess.setProcessFirstResponseTime(i);
					CpuProcess.setProcessLastResponseTime(i);
				}
			}
			if(CpuProcess!=null){
				CpuProcess.Decrement();
				gcObj.temp.add(CpuProcess);
				pbObj.JobQ.add(CpuProcess);
				if(CpuProcess.getProcessRemainingTime()==0){
					if(CpuProcess.getProcessType()==2) {
						((LoanPayment)CpuProcess).setNewLoan(userLoan);
					}
					if(CpuProcess.getProcessType()==1) {
						((LogWorkingHours)CpuProcess).setNewHours(userWH);
					}
					CpuProcess.run(userName, MyConsole, cons, file); //habd
					CpuProcess.setProcessStatus(State.FINISHED);
					MyMemory.emptyMemorySpace(CpuProcess);
					CpuProcess.setProcessWaitingTime(i);
					
					CpuProcess.setProcessFirstResponseTime(i); // habd
					CpuProcess.setProcessLastResponseTime(i+CpuProcess.getProcessBurstTime()); // habd
					
					memUsed+=CpuProcess.getProcessSize();
					
					AvgTurnTime=AvgTurnTime+(CpuProcess.getProcessWaitingTime()+CpuProcess.getProcessBurstTime());
					AvgResTime=AvgResTime+CpuProcess.getProcessWaitingTime();
					CpuProcess=null;
				}
			}
		}
		
		gcObj.JobQ.addAll(gcObj.temp); // CPUChart Class
		
		if(n>0){	
				
			 displayResult2=AvgTurnTime/n;
			 displayResult3=AvgResTime/n;
			 displayResult=memUsed/MyMemory.getTS(); //
		}
		avgTurnLabel.setText("Average TurnAround Time:");
		avgTurn.setText(Float.toString(displayResult2));
		
		avgResLabel.setText("Average Response Time:");
		avgRes.setText(Float.toString(displayResult3));
		
		memUtilLabel.setText("Memory Utilization:");
		memUtil.setText(Float.toString(displayResult*100)+"%");
		
		f3.add(memUtil);
		f3.add(avgTurn);
		f3.add(avgRes);
		f3.revalidate();
	}
	
	public void allQueue(int t){ // --> PaintReadyQueue
		if(!ReadyQueue.isEmpty()){
			prqObj.all.addAll(ReadyQueue);
			prqObj.array[t]=ReadyQueue.size();	
		}else{
			prqObj.array[t]=0;
		}
	}
	
	public static void main(String args[]){
		new Scheduler();
		
	}
	
}
