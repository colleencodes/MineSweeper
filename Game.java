//Colleen Stock

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game extends JPanel
{
	private JPanel extrasPanel;
	private JLabel countdown;
	private JButton newGame;
	private int answer = 0;
	private boolean newGameClick = false;
	private JLabel timerLabel;
	int time = 0;
	int delay = 60;
	
	private Timer timer = new Timer(delay, new TimerListener( ));
	   
	private JPanel buttonsPanel;
	private MineButton [][] buttons;
	int i = 0;
	int j = 0;
	int count = 0;
	int row = 0;
	int col = 0;
	int numBombs = 0;
	
	boolean firstTime = true;
	boolean redraw = false;
	
	//default constructor for a game of 10 X 10
	public Game ()
	{
		row = 10;
		col = 10;
		
		extras(col);
		setUpBoard(row, col);
		
		//sets up the content to be displayed to the user
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(extrasPanel);
		add(buttonsPanel);
	}

	//overloaded constructor that allows for players to chose the size of their game
	public Game (int r, int c)
	{
		extras(c);
		setUpBoard(r, c);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(extrasPanel);
		add(buttonsPanel);
	}

	//sets up the two labels and button on the top
	public void extras(int c)
	{
		//determines the width to make the panel
		int width = c * 20;
		
		//countdown label displays the bombs and ticks off as players identify bombs
		countdown = new JLabel(" ");
		countdown.setOpaque(true);
		countdown.setBackground(Color.WHITE);
		countdown.setForeground(Color.BLACK);
		countdown.setHorizontalAlignment(SwingConstants.CENTER);
		
		//newGame button allows for the user to start a new game after losing or if they just
		//want a different size
		newGame = new JButton("New Game");
		newGame.setMargin(new Insets (1, 1, 1, 1));
		newGame.addActionListener(new NewGameListener());
		newGame.setBackground(Color.BLACK);
		newGame.setForeground(Color.WHITE);

		//timerLabel displays the time in seconds for the player
		timerLabel = new JLabel("00");
		timerLabel.setOpaque(true);
		timerLabel.setBackground(Color.WHITE);
		timerLabel.setForeground(Color.BLACK);
		timerLabel.setHorizontalAlignment(SwingConstants.CENTER);

		//sets up the actual panel and adds the elements to the panel
		extrasPanel = new JPanel();
		extrasPanel.setLayout(new GridLayout (0,3));
		
		extrasPanel.add(countdown);
		extrasPanel.add(newGame);
		extrasPanel.add(timerLabel);
		
		extrasPanel.setSize(width, 25);
	}
	
	//interacts with newGame Button
	private class NewGameListener implements ActionListener
	{
		//if the user clicks newGame it allows for them to chose if they want a new size or no
		public void actionPerformed (ActionEvent e)
		{	
			//stops and resets the timer
			timer.stop();
			time = 0;
			firstTime = true;
			
			newGameClick = true;
			
			//displays dialog box for the user to interact with
			if (e.getSource() == newGame)
				answer = JOptionPane.showConfirmDialog(null, "Do you want to choose a new size?");
			
			fixSize();
		}
	}
	
	//players choice of size
	public void fixSize()
	{
		//lets the player chose a size between 3 and 25 and checks for errors
		if (newGameClick == true)
		{
			if (answer == JOptionPane.YES_OPTION)
			{
				row = Integer.parseInt(JOptionPane.showInputDialog("You get to pick the size but remember the number of bombs is \n 10% of your board. \n Enter the number of rows between 3 and 25: "));
				
				while (row < 3 || row > 25)
				{
					row = Integer.parseInt(JOptionPane.showInputDialog("Sorry that number is not usable. Pick between 3 and 25"));
				}
				
				col = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of columns between 3 and 25: "));
				
				while (col < 3 || col > 25)
				{
					col = Integer.parseInt(JOptionPane.showInputDialog("Sorry that number is not usable. Pick between 3 and 25"));
				}
				
				extras(col);
				setUpBoard(row, col);
			}
			
			if (answer == JOptionPane.NO_OPTION)
			{
				extras(col);
				setUpBoard(row, col);
			}
			
			//fixes the board for the next display
			this.removeAll();
			
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			add(extrasPanel);
			add(buttonsPanel);
			
			repaint();
			
			redraw = true;
		}
	}
	
	private class TimerListener implements ActionListener
	{
		//allows for the timer to work correctly
		public void actionPerformed(ActionEvent event)
		{
			//displays the time in seconds
	         time++;
	         timerLabel.setText(Integer.toString((time * delay) / 1000));
	      }
	}
	
	//displays the buttons for the game
	public void setUpBoard(int r, int c)
	{
		//sets up the panel
		buttonsPanel = new JPanel();
		
		//sets up the 2D array that holds all the buttons
		buttons = new MineButton [r][c];
		
		row = r;
		col = c;
		
		buttonsPanel.setLayout(new GridLayout (r, c));
		
		//initiates all the buttons
		for (int i = 0; i < r; i++)
		{
			for (int j = 0; j < c; j++)
			{
				buttons[i][j] = new MineButton (i, j, false, 0);
			}
		}
		
		//makes specifications to the buttons
		//margin size, how large the button is, assigns a listener, background color, font, and adds 
		//to screen
		for(int i = 0; i < r; i++)
		{
			for (int j = 0; j < c; j++)
			{
				buttons[i][j].setMargin(new Insets (1, 1, 1, 1));
				buttons[i][j].setPreferredSize(new Dimension (22, 22));
				buttons[i][j].addActionListener(new ButtonListener());
				buttons[i][j].addMouseListener(new RightClick());
				buttons[i][j].setBackground(Color.LIGHT_GRAY);
				buttons[i][j].setFont(new Font (Font.SANS_SERIF, Font.BOLD, 12));
				buttonsPanel.add(buttons[i][j]);
			}
		}
		
		//specifies the size of the panel given the number of buttons
		buttonsPanel.setSize(r * 20, col * 20);
		
		generateBombs();
		detectBomb();
	}
	
	//interacts with buttons
	private class ButtonListener implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{	
			//if this is the first button clicked, start the timer
			if (firstTime)
			{
				timer.start();
				firstTime = false;
			}
			
			//get which button was clicked
            MineButton button = (MineButton)e.getSource();
            int i = button.getRow();
            int j = button.getCol();
 
            //if the number of clicks is two then the player has marked it as a bomb so do nothing
            if (buttons[i][j].getNumClicks() == 2)
            {
            	
            }
            
            //if it is a bomb turn black and display where all the other bombs were and 
            //disenable all buttons
            else if (buttons[i][j].getIsBomb())
            {
            	timer.stop();
            	buttons[i][j].setBackground(Color.BLACK);
            	
            	buttons[i][j].setEvaluated(true);
    			displayBombs(i, j);
    			
            	for (int k = 0; k < row; k++)
            	{
            		for (int l = 0; l < col; l++)
            		{
            			buttons[k][l].setEnabled(false);
            			
            		}
            	}
            }

            //if it isn't either of those two cases, color the board
            else
            	colorBoard(i, j);   
            
            //check to see if the game is over
            checkGame();
		} 
	}
	
	//deals with the right mouse button
	private class RightClick implements MouseListener
	{
		public void mousePressed(MouseEvent e ) 
		{  
			 MineButton button = (MineButton)e.getSource();
	         int i = button.getRow();
	         int j = button.getCol();
	           
	         //checks to see if the right mouse button was pushed down
			 if (e.isMetaDown()) 
			 {  
				 //if the button has already been evaluated, take no action
			 	if (buttons[i][j].isEvaluated() == true && buttons[i][j].getNumClicks() != 2)
			 	{
			 		
			 	}
				
			 	
			 	else 
			 		//if the number of right clicks is 0 then mark it yellow for questionable
			  		if (buttons[i][j].getNumClicks() == 0)
				 	{
				 		buttons[i][j].setNumClicks(1);
						buttons[i][j].setBackground(Color.YELLOW);
					}
				
			 		//if the number of right clicks is 1 then mark is red for a bomb and adjust
			 		//the countdown label
					else if (buttons[i][j].getNumClicks() == 1)
					{
						buttons[i][j].setNumClicks(2);
						buttons[i][j].setBackground(Color.RED);
						buttons[i][j].setEvaluated(true);
						countdown.setText(Integer.toString(--numBombs));
						buttons[i][j].setPossibleBomb(true);
					}
				
			 		//if the number of right clicks is 2 then change back to the original color
					else if (buttons[i][j].getNumClicks() == 2)
					{
						buttons[i][j].setNumClicks(0);
						buttons[i][j].setBackground(Color.LIGHT_GRAY);
						buttons[i][j].setEvaluated(false);
						buttons[i][j].setPossibleBomb(false);
						countdown.setText(Integer.toString(++numBombs));
					}
			} 
			
			//check the game to see if it is over
	        checkGame();
		}
		
		public void mouseClicked(MouseEvent e) {}
	    public void mouseExited(MouseEvent e){}
	    public void mouseEntered(MouseEvent e){}
	    public void mouseReleased(MouseEvent e){}
	}
	
	//generates the bombs for the game
	public void generateBombs()
	{		
		int r = 0;
		int c = 0;
		
		//determines how many bombs to produce 
		//should be approximately 10% of the board
		if ((row * col * .1) < 10)
			numBombs = (int)(row * col * .1 + 1);
		else
			numBombs = (int) (row * col * .1);
		
		for (int i = 0; i < numBombs; i++)
		{
			//randomly generates spots to put bombs
			r = (int) (Math.random() * row);
			c = (int) (Math.random() * col);
			
			//if the spot is not a bomb, then place the bomb there
			if (buttons[r][c].getIsBomb() == false)
			{
				buttons[r][c].setIsBomb(true);
			}
			
			//if the spot is already taken, decrement i so the correct number of bombs is produced
			else if (buttons[r][c].getIsBomb() == true)
				i--;
		}
		
		//set the text for countdown
		countdown.setText(Integer.toString(numBombs));
	}
	
	//before the game starts, identifies how many bombs are touching each button
	public void detectBomb()
	{
		for (int i = 0; i < row; i++)
		{
			for (int j = 0; j < col; j++)
			{
				//if it is a bomb itself then the number of bombs is set to -1
				if (buttons[i][j].getIsBomb())
				{
					buttons[i][j].setNumBombs(-1);
				}
				
				//checks in the upper left corner and increments number if needed
				else if (i == 0 && j == 0)
				{					
					if (buttons[i + 1][j].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i + 1][j + 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i][j + 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
				}
				
				//checks in the upper right corner and increments number if needed
				else if (i == 0 && j == (col - 1))
				{
					if (buttons[i][j - 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i + 1][j - 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i + 1][j].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
				}
				
				//checks in lower left corner and increments number if needed
				else if (i == (row - 1) && j == 0)
				{
					if (buttons[i - 1][j].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i - 1][j + 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i][j + 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
				}
				
				//checks in lower right corner and increments number if needed
				else if (i == (row - 1) && j == (col - 1))
				{
					if (buttons[i][j - 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i - 1][j - 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i - 1][j].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
				}
				
				//checks left side and increments number if needed
				else if (i != 0 && i != (row - 1) && j == 0)
				{
					if (buttons[i - 1][j].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i + 1][j].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i][j + 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i - 1][j + 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i + 1][j + 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
				}
				
				//checks right side and increments number if needed 
				else if (i != 0 && i != (row - 1) && j == (col - 1))
				{
					if (buttons[i - 1][j].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i + 1][j].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i][j - 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i - 1][j - 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i + 1][j - 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
				}
				
				//checks bottom and increments number if needed
				else if (i == (row - 1) && j != 0 && j != (col - 1))
				{
					if (buttons[i - 1][j].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i][j - 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i][j + 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i - 1][j - 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i - 1][j + 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
				}
				
				//checks top and increments number if needed
				else if (i == 0 && j != (col - 1) && j != 0)
				{
					if (buttons[i + 1][j].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i][j - 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i][j + 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i + 1][j - 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i + 1][j + 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
				}
				
				//checks middle and increments number if needed
				else if (i != 0 && j != 0 && i != (row - 1) && j != (col - 1))
				{
					if (buttons[i - 1][j].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i + 1][j].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i][j - 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
						
					if (buttons[i][j + 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
					
					if (buttons[i - 1][j - 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
						
					if (buttons[i - 1][j + 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
						
					if (buttons[i + 1][j - 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
						
					if (buttons[i + 1][j + 1].getIsBomb())
					{
						buttons[i][j].setNumBombs(1);
					}
				}
			}
		}
	}
	
	//if the user clicks on a bomb this displays the rest of them
	public void displayBombs(int bombI, int bombJ)
	{
		for (int i = 0; i < row; i++)
		{
			for (int j = 0; j < col; j++)
			{
			    if ( buttons[i][j].getIsBomb() && !buttons[i][j].isEvaluated())
				{
					buttons[i][j].setBackground(Color.BLUE);
				}
			}
		}
	}
	
	//displays color on the board if the user clicks on a space that is not a bomb
	public void colorBoard(int originalI, int originalJ)
	{
		int i = originalI;
		int j = originalJ; 
		
		if (!buttons[i][j].isEvaluated())
		{
			//if the number of bombs touch is not zero then it displays that number and 
			//changes the color
			if (buttons[i][j].getNumBombs() > 0)
			{
				buttons[i][j].setText(Integer.toString(buttons[i][j].getNumBombs()));
				buttons[i][j].setBackground(Color.CYAN);
				buttons[i][j].setEvaluated(true);
				buttons[i][j].setNumBombs(-1);
			}
				
			//otherwise the number of bombs is zero so it will display just color until it reaches
			//a button that has a number of bombs touch it
			
			
			else 
			{
				buttons[i][j].setBackground(Color.CYAN);
				buttons[i][j].setEvaluated(true);

				//colors upper left corner and calls method again
				if (i == 0 && j == 0)
				{
					colorBoard(i + 1, j);
				
					colorBoard(i + 1, j + 1);
	
					colorBoard(i, j + 1);
				}
				
				//colors upper right corner and calls method again
				else if (i == 0 && j == (col - 1))
				{
					colorBoard(i, j - 1);
				
					colorBoard(i + 1, j - 1);
				
					colorBoard(i + 1, j);
				}
				
				//colors lower left corner and calls method again
				else if (i == (row - 1) && j == 0)
				{
					colorBoard(i - 1, j);
				
					colorBoard(i - 1, j + 1);
				
					colorBoard(i, j + 1);
				}
				
				//colors lower right corner and calls method again
				else if (i == (row - 1) && j == (col - 1))
				{
					colorBoard(i, j - 1);
					
					colorBoard(i - 1, j - 1);
					
					colorBoard(i - 1, j);
				}
				
				//colors left side and calls method again
				else if (i != 0 && i != (row - 1) && j == 0)
				{
					colorBoard(i - 1, j);
			
					colorBoard(i + 1, j);

					colorBoard(i, j + 1);
					
					colorBoard(i - 1, j + 1);

					colorBoard(i + 1, j + 1);
				}
				
				//colors right side and calls method again 
				else if (i != 0 && i != (row - 1) && j == (col - 1))
				{
					colorBoard(i - 1, j);
					
					colorBoard(i + 1, j);

					colorBoard(i, j - 1);
				
					colorBoard(i - 1, j - 1);
				
					colorBoard(i + 1, j - 1);
				}
				
				//colors bottom and calls method again
				else if (i == (row - 1) && j != 0 && j != (col - 1))
				{
					colorBoard(i - 1, j);
				
					colorBoard(i, j - 1);
				
					colorBoard(i, j + 1);
					
					colorBoard(i - 1, j - 1);

					colorBoard(i - 1, j + 1);
				}
				
				//colors top and calls method again
				else if (i == 0 && j != (col - 1) && j != 0)
				{
					colorBoard(i + 1, j);
					
					colorBoard(i, j - 1);
	
					colorBoard(i, j + 1);
				
					colorBoard(i + 1, j - 1);
				
					colorBoard(i + 1, j + 1);
				}
				
				//colors middle and calls method again
				else if (i != 0 && j != 0 && i != (row - 1) && j != (col - 1))
				{
					colorBoard(i - 1, j);
					
					colorBoard(i + 1, j);
				
					colorBoard(i, j - 1);
				
					colorBoard(i, j + 1);
					
					colorBoard(i - 1, j - 1);
				
					colorBoard(i - 1, j + 1);
					
					colorBoard(i + 1, j - 1);
					
					colorBoard(i + 1, j + 1);
				}
			}
								
		}
								
	}
	
	//checks to see if the game is completed
	public void checkGame()
	{
		boolean allChecked = true;
		
			for (int i = 0; i < row; i++)
			{
				for (int j = 0; j < col; j++)
				{
					//if it processes a button that has not be evaluated the game is not over
					//so it breaks
					if (buttons[i][j].isEvaluated() == false)
					{
						allChecked = false;
						i = row;
						break;
					}
					
					//if it goes all the way through and everything has been evaluated then
					//the game is over
					else
					{
						allChecked = true;
					}	
				}
			}
		
			//stops the timer
			if (allChecked)
				timer.stop();
		
			boolean win = false;
			
			//checks to see if the player actually beat the game and displays it
			if (allChecked)
			{
				for (int i = 0; i < row; i++)
				{
					for (int j = 0; j < col; j++)
					{
						if (buttons[i][j].getPossibleBomb() && buttons[i][j].getIsBomb())
						{
							win = true;
						}
						
						else if (buttons[i][j].getPossibleBomb() && !buttons[i][j].getIsBomb())
						{
							buttons[i][j].setBackground(Color.BLUE);
							win = false;
						}
						
						else if (!buttons[i][j].getPossibleBomb() && !buttons[i][j].getIsBomb())
						{
							
						}
					}
				}
				
				if (win == true)
				{
					countdown.setText("You");
					timerLabel.setText("Win");
				}
				else 
				{
					countdown.setText("You");
					timerLabel.setText("Lose");
				}
			}
	}
	
	//mutator methods for if the game needs to be redrawn
	public void setRedraw (boolean b)
	{
		redraw = b;
	}
	
	public boolean getRedraw ()
	{
		return redraw; 
	}
}
	

