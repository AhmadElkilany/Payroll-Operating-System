package GUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Processes.Process;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;


public class CPUChart extends JPanel implements ActionListener {
	private Timer timer;
	int time, n, x;
	int c=0;
	public Process p;
	public Queue<Process> JobQ,temp;
	
	public CPUChart(int t){
		JobQ=new LinkedList<Process>();
		temp=new LinkedList<Process>();
		timer=new Timer(1000,this);
		n=0;
		x=5;
		time=t;
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		temp.clear();
		temp.addAll(JobQ);
		n++;
		x=10;
	    repaint();
	}
	
	public void paint(Graphics g){
		super.paint(g);
		this.setBackground(Color.WHITE);
		if(n>=time){
			timer.stop();
		}
		for(int i=0;i<n;i++){
			if(temp.size()>0){
				g.setColor(temp.peek().getProcessColor());
				g.fillRect(x, 10, 15,80);
				g.setColor(Color.WHITE);
				g.drawString(Integer.toString(temp.peek().getProcessID()), x, 25);
				g.drawString(Integer.toString(1+i), x, 25+60);
				x+=20;
				temp.poll();	
			}
		}
		
	}
	
	

}
