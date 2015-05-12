package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.PongClient;
import model.Body;
import model.Collision;

public class PongGUI extends JFrame {
	protected int ANIMATIONTIME = 100;
	public final static int SIZE = 750;
	public final static int NO_KEY = 0;
	public final static int UP_KEY = 1;
	public final static int DOWN_KEY = 2;
	
	private JPanel drawPanel;
	private PongClient client;
	private CircleBody[] circles;
	private Paddle paddle;
	public OptionGUI option;
	private int paddleX;
	private int offset;
	private int radius;
	
	public PongGUI(PongClient client) {
		this.client = client;
		layoutGUI();
	}
	
	private void layoutGUI() {
		this.addKeyListener(new KeyListener() {
        	@Override
        	public void keyPressed(KeyEvent arg0) {
        		int keyCode = arg0.getKeyCode();
        		switch(keyCode) {
        			case KeyEvent.VK_UP:
        				client.keyPressed = UP_KEY;
        				break;
        			case KeyEvent.VK_DOWN:
        				client.keyPressed = DOWN_KEY;
        				break;
        			default:
        				break;
        		}
        		
        	}

        	@Override
        	public void keyReleased(KeyEvent arg0) {
        		int keyCode = arg0.getKeyCode();
        		switch(keyCode) {
        			case KeyEvent.VK_UP:
        				if(client.keyPressed == UP_KEY)
        					client.keyPressed = NO_KEY;
        				break;
        			case KeyEvent.VK_DOWN:
        				if(client.keyPressed == DOWN_KEY)
        					client.keyPressed = NO_KEY;
        				break;
        			default:
        				break;
        		}
        	}

        	@Override
        	public void keyTyped(KeyEvent arg0) {
        		// TODO Auto-generated method stub
        		
        	}
        });
		
		setTitle("Distributed Pong");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBackground(Color.BLUE);
	    
	    if(client.getSide() == OptionGUI.LEFT_SIDE)
	    {
	    	paddleX = 5;
	    	offset = SIZE;
	    }
	    else
	    {
	    	paddleX = SIZE-30;
	    	offset = 0;
	    }
	    paddle = new Paddle((float)SIZE/6);
        circles = new CircleBody[client.numBalls];
	    
	    drawPanel = new JPanel();
	    drawPanel.setLayout(null);
	    drawPanel.repaint();
        this.add(drawPanel);
        
		setMinimumSize(new Dimension(SIZE, SIZE));
		setVisible(true);
	}
	
	public void updateCircles() {
		if(ANIMATIONTIME > 0)
		{
			try {
				Thread.sleep(ANIMATIONTIME/10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	    double xCoord = 100;
        double yCoord = 100;
        
        drawPanel.removeAll();
        drawPanel.revalidate();   
        
	    for(int i = 0; i < client.pointPos.length; i++)
	    {
	    	xCoord = client.pointPos[i].x * 10 + offset;
	    	yCoord = client.pointPos[i].y * 10 + SIZE / 2;
	    	//System.out.println((client.pointPos[i].x * 10 + offset) + " " + (yCoord = client.pointPos[i].y * 10 + SIZE / 2));
	    	
	    	circles[i] = new CircleBody(1.0 * 10);
	        circles[i].setLocation((int)xCoord,(int)yCoord);
	        circles[i].setSize(circles[i].getPreferredSize());
	        drawPanel.add(circles[i]);
	    }
        drawPanel.repaint();
        //System.out.println("printed");
	}
	
	public void updatePaddle()
	{
		//paddle.setLocation(paddleX, client.paddleYPos);
		//drawPanel.add(paddle);
		//drawPanel.repaint();
	}
	
	public void updateAnimation( int time )
	{
		ANIMATIONTIME = time;
	}
}
