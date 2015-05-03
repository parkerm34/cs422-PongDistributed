package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class OptionGUI extends JFrame implements ActionListener {

	private JPanel buttonPanel;
	private JPanel optionPanel;
	private JButton leftButton;
	private JButton rightButton;
	public String[] args;
	private static boolean open = false;
	private static int side = 0;
	
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
	
	public static boolean isOpen()
	{
		return open;
	}
	
	public static int getSide() {
		return side;
	}

	@Override
	public void actionPerformed(ActionEvent arg) {
		if(arg.getSource() == leftButton)
			side = 1;
		else
			side = 2;
		open = false;
		System.out.println(open);
		setVisible(false);
	}
	
}
