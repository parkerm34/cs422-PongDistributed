package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class Paddle extends JPanel {

	private Rectangle2D.Double paddle;
	private float height;
	
	public Paddle( float size ) {
		height = size;
		paddle = new Rectangle2D.Double(0, 0, 10, size);
	}
	
	public Dimension getPreferredSize()
    {
        return new Dimension(10, (int)height);
    }
	
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor( Color.black );
        g2.fill(paddle);
    }
}
