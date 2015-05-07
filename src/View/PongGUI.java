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

public class PongGUI extends JFrame {

	private JPanel drawPanel;
	private PongClient client;
	private Body[] bodies;
	private CircleBody[] circles;
	private Paddle paddle;
	public OptionGUI option;
	private int paddleX;
	public int paddleTop;
	public int paddleBot;
	private int PADDLE_MOVE = 10;
	
	protected int ANIMATIONTIME = 100;
	private int SIZE = 750;
	private int offset;
	
	public PongGUI( int numBodies, PongClient client) {
		this.client = client;
		layoutGUI();
	}
	
	private void layoutGUI() {
		setTitle("Distributed Pong");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBackground(Color.BLUE);
	    
	    if(client.side == 1)
	    {
	    	paddleX = 5;
	    	offset = SIZE;
	    }
	    else
	    {
	    	paddleX = SIZE-30;
	    	offset = 0;
	    }
	    
	    drawPanel = new JPanel();
	    
	    drawPanel.setLayout(null);
	    
	    bodies = client.getBodies();
	    
	    double xCoord = 100;
        double yCoord = 100;
        
        this.addKeyListener(new KeyListener() {
        	@Override
        	public void keyPressed(KeyEvent arg0) {
        		int keyCode = arg0.getKeyCode();
        		switch(keyCode) {
        			case KeyEvent.VK_UP:
        				updatePaddle( -PADDLE_MOVE );
        				break;
        			case KeyEvent.VK_DOWN:
        				updatePaddle( PADDLE_MOVE );
        				break;
        			default:
        				break;
        		}
        		
        	}

        	@Override
        	public void keyReleased(KeyEvent arg0) {
        		// TODO Auto-generated method stub
        		
        	}

        	@Override
        	public void keyTyped(KeyEvent arg0) {
        		// TODO Auto-generated method stub
        		
        	}
        });
        
        paddle = new Paddle((float)SIZE/6);
        paddle.setLocation(paddleX, SIZE/2-SIZE/12);
        paddle.setSize(paddle.getPreferredSize());
        drawPanel.add(paddle);

        circles = new CircleBody[bodies.length];
        
	    for(int i = 0; i < bodies.length; i++)
	    {
	    	xCoord = bodies[i].getXPos()*10 + offset;
	    	yCoord = bodies[i].getYPos()*10 + SIZE/2;
	    	circles[i] = new CircleBody(bodies[i].getRadius() * 10);
	        circles[i].setLocation((int)xCoord,(int)yCoord);
	        circles[i].setSize(circles[i].getPreferredSize());
	        drawPanel.add(circles[i]);
	    }

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
        
	    for(int i = 0; i < bodies.length; i++)
	    {
	    	xCoord = bodies[i].getXPos()*10 + offset;
	    	yCoord = bodies[i].getYPos()*10 + SIZE/2;
	    	circles[i] = new CircleBody(bodies[i].getRadius() * 22);
	        circles[i].setLocation((int)xCoord,(int)yCoord);
	        circles[i].setSize(circles[i].getPreferredSize());
	        drawPanel.add(circles[i]);
	    }
	    Point loc = paddle.getLocation();
	    //System.out.println("PADDLE LOC: " + loc.x + " " + loc.y);
		paddle.setLocation(loc.x, loc.y);
		drawPanel.add(paddle);
        drawPanel.repaint();
	}
	
	public void updatePaddle( int y ) {
		Point loc = paddle.getLocation();
		if(loc.y + y > -75 && loc.y + y < 675)
		{
			paddle.setLocation(loc.x, loc.y + y);
			drawPanel.add(paddle);
			drawPanel.repaint();
			paddleTop = loc.y + y;
			paddleBot = loc.y + y + SIZE/6;
		}
	}
	
	public void updateAnimation( int time ) {
		ANIMATIONTIME = time;
	}
}
