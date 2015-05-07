package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import model.Body;

public class ClientHelper {

	// This code would be repeated in the constructors, so we pulled it out to make
		// the code more modular. This code reads an input file and initializes points
		// from the file, giving their initial position and velocity in terms of x and y.
		@SuppressWarnings("resource")
		public static Body[] readPoints() {
			Body[] bodies;
			
			//TODO: Change to argument
			int numBodies = 1;
			int bodySize = 1;
			
			int count;
			BufferedReader readBuffer;
			String currLine;
			String[] tokens;
			
			count = 0;
			
			bodies = new Body[numBodies];
			for(int i = 0; i < numBodies; i++)
				bodies[i] = new Body();
			
			try {
				readBuffer = new BufferedReader(new FileReader("points.dat"));
				
				while((currLine = readBuffer.readLine()) != null && count < numBodies)
				{
					tokens = currLine.split(" ");
					bodies[count].setXPos(Double.valueOf(tokens[0]));
					bodies[count].setYPos(Double.valueOf(tokens[1]));
					bodies[count].setXVel(Double.valueOf(tokens[2]));
					bodies[count].setYVel(Double.valueOf(tokens[3]));
					bodies[count].setRadius(bodySize);
					count++;
				}
				
			} catch (FileNotFoundException e1) {
				System.out.println("points.dat couldnt be opened.");
				System.exit(1);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			return bodies;
		}
	
}
