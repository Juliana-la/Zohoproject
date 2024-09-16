import java.util.Scanner;
import java.util.Random;

public class mineSweeper {
    static int BOARD_SIZE = 10;
    static int NUM_MINES = 10;
    static char MINE = '*';
    static char EMPTY = '.';
    static char UNEXPLORED = '-';

    boolean gameOver;
    char[][] printmat;
    char[][] solmat;

    mineSweeper(){
        gameOver=false;
        printmat=new char[BOARD_SIZE][BOARD_SIZE];
        solmat=new char[BOARD_SIZE][BOARD_SIZE];
        PrintMatrix();
        SolutionMatrix();
    }

    void PrintMatrix(){
        for(int i=0;i<BOARD_SIZE;i++){
            for(int j=0;j<BOARD_SIZE;j++){
                printmat[i][j]=UNEXPLORED;
            }
        }
    }

    void SolutionMatrix(){
        placeMines();
        calculateAdjacentMines();
    }

    void placeMines(){
        Random rand = new Random();
        int placedmines=0;
        while(placedmines<NUM_MINES){
            int row=rand.nextInt(BOARD_SIZE);
            int col=rand.nextInt(BOARD_SIZE);
            if(solmat[row][col]!=MINE){
                solmat[row][col]=MINE;
                placedmines++;
            }
        }
    }

    void calculateAdjacentMines(){
        for(int i=0;i<BOARD_SIZE;i++){
            for(int j=0;j<BOARD_SIZE;j++){
                if(solmat[i][j]!=MINE){
                    int count=countAdjacentMines(i,j);
                    solmat[i][j]= (count>0)? (char)('0'+ count): EMPTY;
                }
            }
        }
    }

    int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (newRow >= 0 && newRow < BOARD_SIZE && newCol >= 0 && newCol < BOARD_SIZE) {
                    if (solmat[newRow][newCol] == MINE) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    void displayBoard(){
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        for(int i=0;i<BOARD_SIZE;i++){
            System.out.print(i + " ");
            for(int j=0;j<BOARD_SIZE;j++){
                System.out.print(printmat[i][j] + " ");
            }
            System.out.println();
        }
    }

    void reveal(int row, int col) {
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            System.out.println("Invalid cell... Try again :(");
            return;
        }

        if (printmat[row][col]!=UNEXPLORED) {
            System.out.println("Cell already revealed :)");
            return;
        }

        printmat[row][col] = solmat[row][col];

        if (printmat[row][col] == MINE) {
            gameOver = true;
            revealAllCells();  // Reveal all cells, not just mines
            System.out.println("Game Over! You hit a mine!");
        } else if (printmat[row][col] == EMPTY) {
            revealAdjacent(row, col);  // Reveal adjacent cells if current is empty
        }
    }

    void revealAllCells(){
        for(int i=0;i<BOARD_SIZE;i++){
            for(int j=0;j<BOARD_SIZE;j++){
                printmat[i][j]=solmat[i][j];
            }
        }
    }

    private void revealAdjacent(int row, int col) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (newRow >= 0 && newRow < BOARD_SIZE && newCol >= 0 && newCol < BOARD_SIZE && printmat[newRow][newCol]==UNEXPLORED) {
                    printmat[newRow][newCol] = solmat[newRow][newCol];  // Mark as revealed before recursion
                    if (printmat[newRow][newCol] == EMPTY) {
                        revealAdjacent(newRow, newCol);  // Recursively reveal adjacent empty cells
                    }
                }
            }
        }
    }

    boolean hasWon() {
        if (gameOver) {
            return false;  // Can't win if the game is already over
        }
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (solmat[i][j] != MINE && printmat[i][j]==UNEXPLORED) {
                    return false;  // If any safe cell is not revealed, the player hasn't won
                }
            }
        }
        return true;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public static int[] getValidRowColInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            String[] tokens = input.trim().split("\\s+");  // Split input by spaces
            
            if (tokens.length == 2) {  // Ensure we have exactly two inputs
                try {
                    int row = Integer.parseInt(tokens[0]);
                    int col = Integer.parseInt(tokens[1]);
                    return new int[]{row, col};
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input... Please enter two numbers.");
                }
            } else {
                System.out.println("Please enter exactly two numbers separated by space.");
            }
        }
    }

    // Method to ask if the user wants to replay or start over
    public static boolean replay(Scanner scanner) {
        while(true){
            System.out.print("Do you want to start over (enter 'yes' or 'no')? ");
            String response = scanner.next().toLowerCase();
            scanner.nextLine();
            if(yesNo(response)){
                return response.equals("yes");
            }
            else{
                System.out.println("Invalid input......Try again:(");
            }
        }           
    }

    public static boolean yesNo(String response){
        if(response.equals("yes") || response.equals("no")){
            return true;
        }
        else{
            return false;
        }
    }

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);

        do{
            mineSweeper game = new mineSweeper();
            while (!game.isGameOver()) {
                game.displayBoard();
                int row,col;
    
                do{
                    int[] rowCol = getValidRowColInput(sc, "Enter row and column to reveal (separated by space): ");
                    row = rowCol[0];
                    col = rowCol[1];
                    if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
                        System.out.println("Invalid cell... Try again :(");
                    }
            
                    if (game.printmat[row][col]!=UNEXPLORED) {
                        System.out.println("Cell already revealed :)");
                    }
                }while(row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE || game.printmat[row][col]!=UNEXPLORED);
    
                game.reveal(row, col);
    
                // Check for winning condition only if the game is not over
                if (game.hasWon()) {
                    System.out.println("Congratulations! You've cleared the minefield!");
                    game.revealAllCells();  // Reveal all cells if the player wins 
                    game.displayBoard(); // Display the board after winning
                    break;  // Exit the loop since the game is won
                    
                }
            }
    
            if (game.isGameOver()) {
                game.displayBoard();  // Display the board if the game is over.....replay?
            }
        }while(replay(sc));
        
        sc.close();
    }
    
}

