package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Score extends JPanel {

	int score = 0;
	
	public Score( int score ) {
		this.score = score;
	}
	
	public Score() {
		score = 0;
	}
	
	public void setScore( int score ) {
		this.score = score;
	}
	
	public int getScore() {
		return score;
	}
	
	public Dimension getPreferredSize()
    {
        return new Dimension(20, 20);
    }
	
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawString(Integer.toString(score), 50, 50);
    }
}
