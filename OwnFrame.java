//Colleen Stock

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;

public class OwnFrame extends JFrame
{
	private Game panel = new Game();

	int delay = 500;
	
	private Timer timer = new Timer(delay, new TimerListener( ));
	
	//creates a frame for all the panels to be put in
	public OwnFrame()
	{
		timer.start();
		
		setTitle("MineSweeper");
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new BorderLayout());
		
		getContentPane().add(panel);
		pack();

		setVisible(true);
	}
	
	//has a constantly running timer that checks to see if the board needs to be redrawn
	private class TimerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
	         if (panel.getRedraw())
	         {
	        	 pack();

	        	 panel.setRedraw(false);
	        	 
	        	 repaint();
	        	 
	        	 setVisible(true);
	         }
	    }
	}
}
