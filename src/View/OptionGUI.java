package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OptionGUI extends JFrame implements ActionListener {

	public final static int LEFT_SIDE = 0;
	public final static int RIGHT_SIDE = 1;
	
	private JPanel buttonPanel;
	private JPanel optionPanel;
	private JButton leftButton;
	private JButton rightButton;
	public String[] args;
	private static boolean open = false;
	private static int side = LEFT_SIDE;
	
	public OptionGUI(  ) {
		layoutGUI();
	}
	
	private void layoutGUI() {
		open = true;
		setTitle("Pong Options");
	    setBackground(Color.BLUE);
	    setLocation(750, 0);
	    setMinimumSize(new Dimension(200, 100));
	    
	    buttonPanel = new JPanel();
	    
	    leftButton = new JButton("Left Screen");
	    rightButton = new JButton("Right Screen");
	    
	    leftButton.addActionListener(this);
	    rightButton.addActionListener(this);
	    
	    buttonPanel.add(leftButton);
	    buttonPanel.add(rightButton);
	    
	    optionPanel = new JPanel();
	    optionPanel.add(buttonPanel);
	    
	    this.add(optionPanel);
	    
	    setVisible(true);
	}
	
	public boolean isOpen()
	{
		return open;
	}
	
	public int getSide() {
		return side;
	}

	@Override
	public void actionPerformed(ActionEvent arg) {
		if(arg.getSource() == leftButton)
			side = LEFT_SIDE;
		else
			side = RIGHT_SIDE;
		open = false;
		System.out.println(open);
		setVisible(false);
	}
	
}
