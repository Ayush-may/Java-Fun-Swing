import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class ClickCount implements ActionListener{
	
	int cnt=0;
	JFrame frame=new JFrame("Click Count");
	JButton click=new JButton("Click");
	JButton reset= new JButton("Reset");
	JLabel countLabel=new JLabel("Count :");
	JTextField countText= new JTextField("0");
	Random r=new Random();
	
	ClickCount(){
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(300,300);
		frame.setSize(400,100);
		frame.add(countLabel);
		frame.add(click);
		frame.add(countText);
		frame.add(reset);
		countLabel.setBounds(20,0,100,50);
		countText.setBounds(70,15,100,20);
		click.setBounds(190,15,80,20);
		reset.setBounds(280,15,80,20);
		frame.setVisible(true);
		
		countText.setEditable(false);
		
		click.addActionListener(this);
		// reset.addActionListener(this);
		reset.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e){
				int a = r.nextInt(300);
				int b = r.nextInt(70);
				reset.setLocation(a,b);
			}
		
		});
	}	
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==click){
			cnt++;
			countText.setText(cnt+"");
		}
		if(e.getSource()==reset){
			cnt=0;
			countText.setText(0+"");
		}
	
	}
	
	public static void main(String arg[]){
		new ClickCount();
	}
}