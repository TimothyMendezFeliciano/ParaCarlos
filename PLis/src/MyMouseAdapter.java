import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MyMouseAdapter extends MouseAdapter {

	public void mousePressed(MouseEvent e) {
		switch (e.getButton()) {
		case 1:		//Left mouse button
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame = (JFrame) c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			myPanel.mouseDownGridX = myPanel.getGridX(x, y);
			myPanel.mouseDownGridY = myPanel.getGridY(x, y);
			myPanel.repaint();
			break;
		case 3:		//Right mouse button
			c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			myFrame = (JFrame) c;
			myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
			myInsets = myFrame.getInsets();
			x1 = myInsets.left;
			y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			x = e.getX();
			y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			myPanel.mouseDownGridX = myPanel.getGridX(x, y);
			myPanel.mouseDownGridY = myPanel.getGridY(x, y);
			myPanel.repaint();
			break;
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}
	public void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
		case 1:		//Left mouse button
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame = (JFrame)c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			int gridX = myPanel.getGridX(x, y);
			int gridY = myPanel.getGridY(x, y);

			if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
				//Had pressed outside
				//Do nothing
			} else {
				if ((gridX == -1) || (gridY == -1)) {
					//Is releasing outside
					//Do nothing
				} else {
					if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
						//Released the mouse button on a different cell where it was pressed
						//Do nothing
					} else {
						//Released the mouse button on the same cell where it was pressed
						if ((gridX == 0) || (gridY == 0)) {
							//On the left column and on the top row... do nothing
						} else { 
							//On the grid other than on the left column and on the top row:
							if(myPanel.mineCells[myPanel.mouseDownGridX][myPanel.mouseDownGridY] == true )
								// Paints tile with bombs the color black
							{
								Color newColor = Color.BLACK;
								myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = newColor;
								for (int xValue = 1; xValue < 10; xValue++) {   //Paints all mined cells black
									for (int yValue = 1; yValue < 11; yValue++) {
										if(myPanel.mineCells[xValue][yValue] == true)
										{
											newColor = Color.BLACK;
											myPanel.colorArray[xValue][yValue] = newColor;
											myPanel.repaint();


										}
									}
								}
								if(gameLost()) { //Closes lost game
									myFrame.dispose();
								}
								else {
									myFrame.dispose();
									Main.main(null);
								}
							}
							// Paints tile with no bombs the color gray
							else {
								Color newColor = Color.LIGHT_GRAY;
								
								myPanel.revealAdjacent(myPanel.mouseDownGridX,myPanel.mouseDownGridY);
								myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = newColor;
								
								int count = 0;
								for (int xValue = 1; xValue < 10; xValue++) {   //The rest of the grid
									for (int yValue = 1; yValue < 11; yValue++) {
										if(myPanel.colorArray[xValue][yValue] == Color.LIGHT_GRAY)
										{
											count++;
											myPanel.colorArray[xValue][yValue] = Color.LIGHT_GRAY;
											myPanel.repaint();
											if(count == 71) // 71 cells do not have a mine
											{
												myPanel.colorArray[xValue][yValue] = Color.LIGHT_GRAY;
												myPanel.repaint();
												for (xValue = 1; xValue < 10; xValue++) {   //The rest of the grid
													for (
															yValue = 1; yValue < 11; yValue++) {
														if(myPanel.mineCells[xValue][yValue] == true)
														{
															newColor = Color.BLACK;
															myPanel.colorArray[xValue][yValue] = newColor;
															myPanel.repaint();
														}
													}
												}
												if(gameWon()) { // Closes game if no retry is desired
													myFrame.dispose();	
												}
												else {
													myFrame.dispose();
													Main.main(null);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			myPanel.repaint();
			break;
		case 3:		//Right mouse button
			c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			myFrame = (JFrame)c;
			myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
			myInsets = myFrame.getInsets();
			x1 = myInsets.left;
			y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			x = e.getX();
			y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			gridX = myPanel.getGridX(x, y);
			gridY = myPanel.getGridY(x, y);
			if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
				//Had pressed outside
				//Do nothing
			} else {
				if ((gridX == -1) || (gridY == -1)) {
					//Is releasing outside
					//Do nothing
				} else {
					if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
						//Released the mouse button on a different cell where it was pressed
						//Do nothing
					} else {
						//Released the mouse button on the same cell where it was pressed
						if ((gridX == 0) || (gridY == 0)) {
							//On the left column and on the top row... do nothing
						} else {
							//On the grid other than on the left column and on the top row:
							if(myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] == Color.WHITE){
								Color newColor = Color.RED;
								myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = newColor;
								myPanel.repaint();

							}else if(myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] == Color.RED){
								Color newColor = Color.WHITE;

								myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = newColor;
								myPanel.repaint();
							}

						}
					}
				}
			}
			myPanel.repaint();
			break;
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}
	public boolean gameLost() { //Says you lost the game gives option to retry or quit

		JOptionPane.showMessageDialog(null,"OOPS. GAME OVER!!!","GAME OVER",JOptionPane.PLAIN_MESSAGE);
		return (JOptionPane.showConfirmDialog(null, "Retry?", "Game Over", JOptionPane.YES_NO_OPTION) == 1);		
	}
	public boolean gameWon() {  //Says you won the game gives option to retry or quit
		JOptionPane.showMessageDialog(null,"Congratulations! You Won.","Congrats!!!",JOptionPane.PLAIN_MESSAGE);
		return(JOptionPane.showConfirmDialog(null, "Retry?", "Game Over", JOptionPane.YES_NO_OPTION) == 1);
	}
}