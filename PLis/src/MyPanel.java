import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Random;
import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 25;
	private static final int GRID_Y = 25;
	private static final int INNER_CELL_SIZE = 50;
	private static final int TOTAL_COLUMNS = 10;
	private static final int TOTAL_ROWS = 11;   //Last row has only one invisible cell
	private Random rand = new Random();
	private int mineLimit = 10; // Establishes the limit amount of mines
	public int countMines = 0; // Counts mines
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	public Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS]; // Paints every single cell of the color desired
	public boolean[][] mineCells = new boolean[TOTAL_COLUMNS][TOTAL_ROWS]; // Creates cells with mine
	public int[][] bombsAround = new int[TOTAL_COLUMNS][TOTAL_ROWS];// Counts bombs around x,y points in grid


	public MyPanel() {   //This is the constructor... this code runs first to initialize
		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}
		for (int x = 0; x < TOTAL_COLUMNS; x++)   //The rest of the grid
		{   
			for (int y = 0; y < TOTAL_ROWS-1; y++)
			{
				colorArray[x][y] = Color.WHITE;
				bombsAround[x][y]=0;
				mineCells[x][y]=false;

			}

		}
		while(countMines < mineLimit) {
			// Loop is used to create and randomly localize mines
			int mineCoordinateX=rand.nextInt(9) + 1;
			int mineCoordinateY=rand.nextInt(9) + 1;
			if(!mineCells[mineCoordinateX][mineCoordinateY]) { // Does not repeat mine location
				mineCells[mineCoordinateX][mineCoordinateY] = true;
				bombsAround(mineCoordinateX, mineCoordinateY);
				countMines++;
			}
		}
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Compute interior coordinates
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		int x2 = getWidth() - myInsets.right - 1;
		int y2 = getHeight() - myInsets.bottom - 1;
		int width = x2 - x1;
		int height = y2 - y1;

		//Paint the background
		g.setColor(Color.GRAY);
		g.fillRect(x1, y1, width + 1, height + 1);

		//By default, the grid will be 9x9 
		g.setColor(Color.GRAY);
		for (int y = 1; y <= TOTAL_ROWS - 1; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 1; x <= TOTAL_COLUMNS; x++) {
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS - 1)));
		}

		//Draw an additional cell at the bottom left
		g.drawRect(x1 + GRID_X, y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS - 1)), INNER_CELL_SIZE + 1, INNER_CELL_SIZE + 1);

		//Paint cell colors
		for (int x = 1; x < TOTAL_COLUMNS; x++) {
			for (int y = 1; y < TOTAL_ROWS-1; y++) // TOTAL_ROWS -1 to prevent coloring the last row.  {
				if ((x == 0) || (y != TOTAL_ROWS - 1)) {
					Color c = colorArray[x][y];
					g.setColor(c);
					g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);

					if(colorArray[x][y] == Color.LIGHT_GRAY && !mineCells[x][y]) //Write and show the number of mines around 
						//a cell in different colors.
					{
						int quantity = bombsAround[x][y]; // Establishes the amount of mines near cell pressed
						String text = "" + quantity; // Turns quantity into a string
						int fontSize = 20;
						g.setFont(new Font("Arial", Font.PLAIN, fontSize));
						// To set a color according to amount of mine
						switch (quantity) {
						case 1:
							g.setColor(Color.BLUE);
							break;

						case 2:
							g.setColor(Color.WHITE);
							break;

						case 3:
							g.setColor(Color.RED);
							break;

						case 4:
							g.setColor(Color.BLACK);
							break;

						case 5:
							g.setColor(Color.PINK);
							break;

						case 6:
							g.setColor(Color.ORANGE);
							break;

						case 7:
							g.setColor(Color.MAGENTA);
							break;

						case 8:
							g.setColor(Color.YELLOW);
							break;
						}
						g.drawString(text, x1 + GRID_X + (x * (INNER_CELL_SIZE)) + 35, y1 + GRID_Y + (y * (INNER_CELL_SIZE)) + 45);

					}
				}
		}
	}

	// This method helps to find the adjacent boxes that don't have a mine.
	// It is partially implemented since the verify hasn't been discussed in class
	// Verify that the coordinates in the parameters are valid.
	// Also verifies if there are any mines around the x,y coordinate
	public void revealAdjacent(int x, int y){
		if((x<0) || (y<0) || (x>9) || (y>9)) // Examines if the coordinate is out of bounds
		{
			return;
		}

		else  
		{ // Evaluates all the surrounding cells at once
			if(x>0 && x< TOTAL_COLUMNS && y>0 && y< TOTAL_ROWS ) {
				if(!mineCells[x][y]) {
					if(colorArray[x][y] == Color.WHITE && bombsAround[x][y] == 0) {
						colorArray[x][y] = Color.LIGHT_GRAY;
						revealAdjacent(x,y-1);
						revealAdjacent(x, y+1);
						revealAdjacent(x-1, y);
						revealAdjacent(x+1, y);
					}
					else if (colorArray[x][y] == Color.WHITE && bombsAround[x][y] != 0) {
						colorArray[x][y] = Color.LIGHT_GRAY;
					}
				}

			}
			else {
				return;
			}
		}
	}

	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return x;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return x;
	}
	public int getGridY(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return y;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return y;
	}
	public void bombsAround(int x, int y) {
		// Method counts the bombs around particular cell
		for(int i= x - 1; i<= x+1; i++) {

			if (i>=1 && i<=9) {

				for(int j = y-1; j<= y+1; j++) {

					if (j>=1 && j<=9) {

						if (i==x && j==y) {

							continue;
						}
						else {
							bombsAround[i][j]=bombsAround[i][j]+1;
						}
					}
				}
			}
		}			
	}
}