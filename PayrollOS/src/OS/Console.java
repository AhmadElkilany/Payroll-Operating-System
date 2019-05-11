package OS;

import javax.swing.*;

public class Console extends JFrame{
		
	JFrame frame;
	JPanel displayArea;
	JScrollPane pane;
	BoxLayout box;
	static int time;

	public Console() {
		time = 0;
		frame=new JFrame();
		frame.setSize(700, 870);
		frame.setVisible(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);
		frame.setTitle("Console");
		displayArea = new JPanel();
		box = new BoxLayout(displayArea, BoxLayout.Y_AXIS);
		displayArea.setLayout(box);
		pane = new JScrollPane(displayArea);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setViewportView(displayArea);
		pane.setBounds(15, 15, 655, 800);
		frame.add(pane);
		frame.setVisible(true);
	}
	
	public void print(String s) {
		this.displayArea.revalidate();
		JLabel label = new JLabel("T="+time+": "+s);
		JLabel space = new JLabel("\n");
		label.setHorizontalAlignment(JLabel.CENTER);
		space.setHorizontalAlignment(JLabel.CENTER);
		displayArea.add(label);
		displayArea.add(space);
		time++;
	}
	
	public void printNT(String s) { //without time
		this.displayArea.revalidate();
		JLabel label = new JLabel(s);
		JLabel space = new JLabel("\n");
		label.setHorizontalAlignment(JLabel.CENTER);
		space.setHorizontalAlignment(JLabel.CENTER);
		displayArea.add(label);
		displayArea.add(space);
	}
	
	public void printMem(String s) {
		this.displayArea.revalidate();
		JLabel label = new JLabel(s);
		label.setHorizontalAlignment(JLabel.CENTER);
		displayArea.add(label);
	}
	
}
