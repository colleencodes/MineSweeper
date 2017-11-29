//Colleen Stock 

import javax.swing.*;

//the class for all the buttons
public class MineButton extends JButton
{
	private int row;
	private int col;
	private int numBombs;
	private boolean isBomb;
	private boolean evaluated = false;
	private int numClicks;
	private boolean possibleBomb = false;
	
	//constructor that sets its number in the array and initially says that it is not a bomb,
	//it has no bombs touching it, and it has never been right clicked
	public MineButton(int i, int j, boolean explodes, int nB)
	{
		row = i;
		col = j;
		isBomb = explodes;
		numBombs = nB;
		numClicks = 0;
	}
	
	//mutator methods for the row
	public void setRow(int r)
	{
		row = r;
	}
	
	public int getRow()
	{
		return row;
	}

	//mutator methods for the column
	public void setCol(int c)
	{
		col = c;
	}
	
	public int getCol()
	{
		return col;
	}
	
	//mutator methods for how many bombs are touching a button
	public void setNumBombs(int b)
	{
		numBombs += b;
	}
	
	public int getNumBombs()
	{
		return numBombs;
	}
	
	//mutator methods for if a button is a bomb
	public void setIsBomb(boolean b)
	{
		isBomb = b;
	}

	public boolean getIsBomb()
	{
		return isBomb;
	}
	
	//mutator methods for if the button has been evaluated yet
	public boolean isEvaluated()
	{
		return evaluated;
	}

	public void setEvaluated(boolean evaluated)
	{
		this.evaluated = evaluated;
	}

	//mutator methods for the number of times the right mouse button has been clicked for a 
	//certain button
	public void setNumClicks(int i)
	{
		numClicks = i;
	}
	
	public int getNumClicks()
	{
		return numClicks;
	}
	
	//mutator methods for if the user thinks a button might be a potential bomb
	public void setPossibleBomb(boolean b)
	{
		possibleBomb = b;
	}
	
	public boolean getPossibleBomb()
	{
		return possibleBomb;
	}
}


