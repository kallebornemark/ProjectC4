package testEnviroment_Logic_V1;

import javax.swing.JOptionPane;

/**
 * @author Kalle Bornemark
 */
public class GameController {
    private GameGrid gameGrid;
    private int[] size = new int[6];
    private int player;
    private int row, col;
    private int playedTiles;

    public GameController() {
        gameGrid = new GameGrid();
        player = 1;
    }

    public void newGame() {
        playedTiles = 0;
        for (int i = 0; i < size.length; i++) {
            size[i] = 0;
        }
        gameGrid.reset();
        player = 1;
        gameGrid.print();
    }

    public void newMove(int x) {
        if(size[x] < 7) {
            row = (gameGrid.getHeight() - 1) - (size[x]);
            col = x;
            gameGrid.setElement((gameGrid.getHeight() - 1) - (size[x]++), x, player);
            playedTiles++;
            gameGrid.print();
            if (checkHorizontal() || checkVertical() || checkDiagonalRight() || checkDiagonalLeft()) {
                System.out.println("Winner player " + player );
            } else if (playedTiles == 42) {
                System.out.println("Draw");
            } else {
                changePlayer();
            }
        }
    }

    public void changePlayer() {
        if(player == 1) {
            player = 2;
        }else {
            player = 1;
        }
        System.out.println("Spelare " + player);
    }

    private boolean checkHorizontal() {
        int counter = 1;
        for (int i = col; i < gameGrid.getLength(); i++) {
            if (i == gameGrid.getLength() - 1 || gameGrid.getElement(row, i + 1) != player) {
                counter = 1;
                for (int j = i; j >= 0; j--) {
                    if (j == 0 || gameGrid.getElement(row, j - 1) != player) {
                        return false;
                    } else {
                        counter++;
                    }
                    if (counter == 4) {
                        return true;
                    }
                }
            } else {
                counter++;
            }
            if (counter == 4) {
                return true;
            }
        }
        return false;
    }

    private boolean checkVertical(){
        int counter = 1;
        for(int x = row; x < gameGrid.getHeight(); x++) {
            if(x == gameGrid.getHeight() - 1 || gameGrid.getElement(x + 1, col) != player) {
                return false;
            } else {
                counter++;
                if (counter == 4) {
                    return true;
                }
            }
        }
        return false;

    }

    private boolean checkDiagonalRight() {
        int counter = 1;
        for(int i = col,j = row; i < gameGrid.getLength() && j < gameGrid.getHeight(); i++, j++) {
            if(i == gameGrid.getLength() - 1 || j == gameGrid.getHeight() -1 || gameGrid.getElement(j + 1, i + 1) != player) {
                counter = 1;
                for(int x = i, y = j; x >= 0 && y >= 0; x--, y--) {
                    if(x == 0 || y == 0 || gameGrid.getElement(y - 1, x - 1) != player) {
                        return false;
                    }else {
                        counter++;
                    }
                    if(counter == 4) {
                        return true;
                    }
                }
            }else {
                counter++;
            }
            if(counter == 4) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonalLeft(){
        int counter = 1;
        for(int i = col,j = row; i >= 0 && j < gameGrid.getHeight(); i--, j++) {
            if(i == 0 || j == gameGrid.getHeight() -1 || gameGrid.getElement(j + 1, i - 1) != player) {
                counter = 1;
                for(int x = i, y = j; x < gameGrid.getLength() && y >= 0; x++, y--) {
                    if(x == gameGrid.getLength() - 1 || y == 0 || gameGrid.getElement(y - 1, x + 1) != player) {
                        return false;
                    }else {
                        counter++;
                    }if(counter == 4) {
                        return true;
                    }
                }
            }else {
                counter++;
            }if(counter == 4) {
                return true;
            }
        }
        return false;
    }
    
    public static void main(String[] args) {
		GameController con = new GameController();
		int siffra = 0;
		while(true) {
			siffra = Integer.parseInt(JOptionPane.showInputDialog("Vilken rad ska du l�gga?"));
			if(siffra == 0) {
				con.newGame();
			}else {
				con.newMove(siffra-1);
			}
			
		}
	}
}
