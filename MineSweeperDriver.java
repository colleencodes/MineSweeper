/*
 * Colleen Stock
 * Dr. Wetklow
 * Advanced Programming Topics
 * MineSweeper Game
 * February 13, 2010
 */

import javax.swing.*;

public class MineSweeperDriver
{
	public static void main (String [] args)
	{
		//sets up the frame
		OwnFrame frame = new OwnFrame ();
		
		//displays instructions to the user
		JOptionPane.showMessageDialog(null,
			    "Welcome to MineSweeper! \nIn this game, you try to identify where all the bombs \n" +
			    "are without setting any of them off. \n\nThe colors means the follwing: \n" +
			    "LIGHT BLUE means that it is a safe area \n" +
			    "YELLOW means that you are not really sure \n" +
			    "RED means that you think it is a bomb \n" +
			    "BLACK means that it is a bomb that you clicked on \n" +
			    "BLUE means those are bombs that you didn't identify \n \n" +
			    "To access the yellow and red, right click. The blue will only be displayed after \n" +
			    "the game is finished. \n" +
			    " \n " +
			    "Best of luck and have fun! ", 
			    "Instructions", JOptionPane.PLAIN_MESSAGE);
	}
}