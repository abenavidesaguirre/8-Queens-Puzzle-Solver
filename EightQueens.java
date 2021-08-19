/**
 * @author Andrea Benavides Aguirre <a href="andrea.benavidesagui@ucalgary.ca"> andrea.benavidesagui@ucalgary.ca </a>
 * @version     1.0
 * @since       1.0
 */

import java.util.Arrays;

class EightQueens implements Cloneable{
    //Class fields
    private int queens = 8;                          //number of max queens on a board
    private char[][] board = new char[8][8];         //2d char array representing chess board
    private int badPlacement = 0;                    //indicates illegal placement of manually set queens when set to 1
    //end of class fields
   

    //default constructor: all board spaces are set to open
    public EightQueens() {
        for(char[] row:board) {
            Arrays.fill(row, 'o');
        }
    }
    //end of constructors

    // printBoard() creates a visual representation of the chess board with all active queens
    public void printBoard() {
        System.out.println("      0    1    2     3    4    5    6    7    8");
        for (int i = 0; i < 8; i++) {
            System.out.println("\n   ------------------------------------------------");
            System.out.print(i);
            for(int j = 0; j < 8; j++) {
                System.out.print(" |  " + board[i][j] + ' ');
            }            
        }
        System.out.println("\n   ------------------------------------------------\n");
    }
    //end of printBoard()


    // setQueen will place a queen on the chess board at the location indicated by the provided indices (0-7)
    public void setQueen(int row, int column) {

        if(queens == 0) {
            throw new IllegalArgumentException("8 Queens Already on Board");
        }

        if(row < 0 || row > 8 || column < 0 || column > 8) {
            throw new IndexOutOfBoundsException("Index Out of Bounds");    //exception thrown if provided indexes are out of bounds
        }

        if(board[row][column] == 'X') {                                    //if two queens are determined to conflict, badPlacement is set one
            badPlacement =1;
            }

        board[row][column] = 'Q';                                         //adds queen to the board
        queens--;                                                         //updates the number of queens left to be added
        markAttacked();                                                   //updates to board to reflect available squares
    }
    //end setQueen


    //emptySquare removes the queen indicated at the provided indexes  
    public void emptySquare(int row, int column) {  

        if(row < 0 || row > 8 || column < 0 || column > 8) {
            throw new IndexOutOfBoundsException("Index Out of Bounds");    //exception thrown if provided indexes are out of bounds
        }

        if(queens == 8) {
            throw new IllegalArgumentException("No Queens Found on Board");  //exception thrown if there are no queens on the board to remove
        }

        if(board[row][column] != 'Q') {
            throw new IllegalArgumentException("No Queen Found at " + row + "," + column);  //exception thrown if there is no queen on the indicated square
        } else {
            queens++;                                                          //number of queens increased
        }

        board[row][column] = 'o';                                             //square updated to 'available'
        markAttacked();                                                       //board updated to reflect new available squares
    }
    // end of emptySquare


    //setQueens will try to place indicated remaining queens so that no two queens are attacking each other. If successfull, will return true
    public boolean setQueens(int queensRemaining) {
        boolean maybeValid = true;

        if(queensRemaining != queens) {
            throw new IllegalArgumentException("Incorrect Number of Queens to be placed");  //exception thrown if there are more than 8 queens to be place on the board
        }

        if(queensRemaining == 0) {          //if no queens remain, all queens have been sucessfully placed
            maybeValid = true;                //maybeValid is set to be true to stop program from checking other queen placements
            return true;
        }

        if(badPlacement==1) {               //if the manually set queens have been determined to have illegal placements, the program will return false in the first iteration
            return false;
        }

        for (int i = 0; i < 8; i++) {                //these loops filter through the entire array to find the first open space
            for (int j = 0; j < 8; j++) {
                if(board[i][j] == 'o') {
                    setQueen(i,j);                  //if an open space if found, a queen is place
                // printBoard();
                    maybeValid = setQueens(queensRemaining-1);      //the function is called recursively with one less queen. It will try to set all the remaining queens. If they cannot be set (no open spaces) it will return false
                    if (maybeValid == false) {                     //if all the queens could not be place, the program will backtrack and remove the previous queens and try to place it in the next empty space
                        emptySquare(i,j);
                        maybeValid = true;
                    }
                }
            }
        }  
        if(queens == 0) {                                        //if all the queens are successfully placed, return true to avoid continued iterations
            return true;
        }
        else {                                                  //if there are no open spaces, return false, and backtrack
        return false;
        }
    }
    //end setQueens


    //cleanUp will remove all 'X's from the board and replace them with 'o'
    public void cleanUp() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(board[i][j] == 'X') {
                    board[i][j] = 'o';
                }
            }
        }
    }
    //end of cleanUp


    //getBoard returns the current state of the 2D array and calls cleanUp to remove any 'X's
    public char[][] getBoard() {
        cleanUp();
        return board;
    }
    //end of getBoard


    //markAttacked() is called when a queen is placed. It will mark the column, row, and diagonals with a X with respect to the queen to mark the squares as threatened
    public void markAttacked() {
        for(int row = 0; row < 8; row++) {    //this nested loop resets the board, only leaving the existing Q's (even if illegal)
            for(int col = 0; col < 8; col++){
                if(board[row][col] != 'Q'){   
                    board[row][col] = 'o';
                }
            }
        }

        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++){
    
                if(board[row][col] == 'Q'){                     //finds all queens on the board and respectivly marks attacked squares

                    for(int i = col-1, j = 1; i > -1; i--) {   //marks attacked squares to the west and southwest
                        if(board[row][i]!='Q') {
                            board[row][i] = 'X';
                        }
                        if(row - j > -1) {
                            if(board[row-j][i] != 'Q') {
                                board[row - j][i] = 'X';
                            }
                            j++;
                        }
                    }
                    for(int i = col + 1, j=1; i < 8; i++) {     //marks attacked squares to the east and northeast
                        if (board[row][i]!='Q'){
                            board[row][i] = 'X';
                        }
                        if(row+j < 8) {
                            if(board[row+j][i] != 'Q') {
                                board[row+j][i] = 'X';
                            }
                            j++;
                        }
                    }
                    for(int i = row -1, j = 1; i > -1; i--) {       //marks attacked squares to the south and southeast
                        if(board[i][col] != 'Q') {
                            board[i][col] = 'X';
                        }
                        if(col + j < 8) {
                            if(board[i][col+j]!='Q') {
                                board[i][col + j] = 'X';
                            }
                            j++;
                        }
                    }
                    for(int i = row + 1, j = 1; i < 8; i++) {       //marks attacked squares to the north and northwest
                        if(board[i][col] != 'Q') {
                            board[i][col] = 'X';
                        }
                        if(col - j > -1) {
                            if(board[i][col-j] != 'Q') {
                                board[i][col - j] = 'X';
                            }
                            j++;
                        }
                    }
               }
            }
        }
    }


    //clone creates a deep copy of the object
    public Object clone() throws CloneNotSupportedException{
        EightQueens theCopy = (EightQueens)super.clone();
        char[][] cloneBoard = new char[8][];            //clone only supported for 1D
            for(int i = 0; i < 8; i++) {
                cloneBoard[i] = board[i].clone();
            }
        theCopy.board = cloneBoard;
        return theCopy;
    }
    //end of clone


    
    public static void main (String[] args) throws CloneNotSupportedException {
      
        //tests - comment main at submission
        
        //valid tests
        //Creation of object
        /*
        EightQueens test1 = new EightQueens();
        System.out.println("Test1 should print empty board");
        test1.printBoard();
        test1.setQueen(5,5);
        test1.setQueen(2,2);
        test1.printBoard();

        //cloning tests
        EightQueens test2 = (EightQueens)test1.clone();
        test2.setQueen(1,0);
        test2.emptySquare(5, 5);
        test2.setQueen(0, 0);
        System.out.println("Should print test2 board with extra queen");
        test2.printBoard();
        System.out.println("Should print test1 original board");
        test1.getBoard();
        test1.printBoard();
        System.out.println("should print if there is a solution for test1");
        System.out.println(test1.setQueens(test1.queens));
        System.out.println("Should print if there is a solution for test2");
        System.out.println(test2.setQueens(test2.queens));
        */

        //empty test
        /*
        EightQueens test3 = new EightQueens();
        System.out.println("Should print the first solution");
        System.out.println(test3.setQueens(test3.queens));
        char[][] test3Board = test3.getBoard();
        test3.printBoard();
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                System.out.println(test3Board[i][j]);
            }
        }
        */
        
        //invalid inputs
        /*
        EightQueens test4 = new EightQueens();
        test4.setQueen(0,0);
        System.out.println(test4.setQueens(7));
        test4.getBoard();
        test4.printBoard();
      //  test4.setQueen(6,6);  //should throw exception. Max number of queens reached

        EightQueens test5 = new EightQueens();
      //  test5.emptySquare(0,0); //should throw exception. no queens to remove
      // test5.setQueen(-1,4);     //should thrown exception. out of bounds
        test5.setQueen(0,0);
        test5.emptySquare(1, 1);    //should throw exception. no queen at indicated square
        */
    } 
    //end of main
}
//end of class