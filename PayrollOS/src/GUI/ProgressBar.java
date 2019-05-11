package GUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Processes.Process;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;


public class ProgressBar extends JPanel implements ActionListener{

	private Timer timer;
	JProgressBar bars[];
	JLabel barsLabels[];
	Process p, ptemp;
	public Queue<Process> JobQ,temp;
	int c[];
	
	public ProgressBar(int n, Queue<Process> q){ 
		
		timer = new Timer(950,this);
		bars = new JProgressBar[n];
		barsLabels = new JLabel[n];
		c = new int [n];
		JobQ = new LinkedList<Process>();
		temp = new LinkedList<Process>();
		JobQ.addAll(q);
		temp.addAll(q);
		
		for(int i=0;i<n;i++){
			p=temp.poll();
			if(i>0){
				c[i]=-1;
			}
			bars[i]=new JProgressBar(0,p.getProcessBurstTime());
			barsLabels[i]=new JLabel();
			barsLabels[i].setText(Integer.toString(p.getProcessID()));
			barsLabels[i].setBounds(0, 10+i*50, 15, 50);
			// Progress Bar
			bars[i].setValue(0);
			bars[i].setBounds(15, 10+i*50, 140, 50);
			bars[i].setBackground(Color.WHITE);
			bars[i].setForeground(new Color(0,204,102));
			bars[i].setStringPainted(true);
			this.add(barsLabels[i]);
			this.add(bars[i]);
		}
		timer.start();
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if(!JobQ.isEmpty()){
			ptemp=JobQ.poll();
			int id=ptemp.getProcessID()-1; 
			c[id]++;
			if(c[id]<=ptemp.getProcessBurstTime()){
				bars[id].setValue(c[id]);	
			}
		}
	}

}
