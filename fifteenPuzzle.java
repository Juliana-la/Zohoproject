import java.util.Random;//check if the initialization is ok
import java.util.Scanner;

public class fifteenPuzzle {
    private static final int DIM = 4;
    private static final int STARTINDEX= 0;
    private static final int ENDINDEX = 3;
    private static final int NUMTILES= 16;
    private int[][] board;
    private int blankX, blankY;

    public fifteenPuzzle() {
        board = new int[DIM][DIM];
        initializeBoard();
        shuffleBoard();
    }
    
    //Initialising the board in a solved state and then latter only valid shuffling happens
    private void initializeBoard() {
        int count = 1;
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                board[i][j] = count++;
            }
        }
        board[DIM-1][DIM-1] = 0; // Blank tile
        blankX = DIM-1;
        blankY = DIM-1;
    }

    //Makes sure that the puzzle is solvable
    private void shuffleBoard() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {//Number of iterations depend on the size of matrix and randomness
            int move = random.nextInt(4);
            switch (move) {
                case 0:
                    moveUp();
                    break;
                case 1:
                    moveDown();
                    break;
                case 2:
                    moveLeft();
                    break;
                case 3:
                    moveRight();
                    break;
            }
        }
    }

    private void printBoard() {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if (board[i][j] == 0) {
                    System.out.print("    "); // For the blank space
                } else {
                    System.out.printf("%3d ", board[i][j]);
                }
            }
            System.out.println();
        }
    }

    private boolean isSolved() {
        int count = 1;
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if (board[i][j] != count && count < NUMTILES) {
                    return false;
                }
                count++;
            }
        }
        return true;
    }

    private void moveUp() {
        if (blankX > STARTINDEX) {
            board[blankX][blankY] = board[blankX - 1][blankY];
            board[blankX - 1][blankY] = 0;
            blankX--;
        }
    }

    private void moveDown() {
        if (blankX < ENDINDEX) {
            board[blankX][blankY] = board[blankX + 1][blankY];
            board[blankX + 1][blankY] = 0;
            blankX++;
        }
    }

    private void moveLeft() {
        if (blankY > STARTINDEX) {
            board[blankX][blankY] = board[blankX][blankY - 1];
            board[blankX][blankY - 1] = 0;
            blankY--;
        }
    }

    private void moveRight() {
        if (blankY < ENDINDEX) {
            board[blankX][blankY] = board[blankX][blankY + 1];
            board[blankX][blankY + 1] = 0;
            blankY++;
        }
    }
    boolean option(Scanner scanner){
        while(true){
            System.out.print("Do you want to continue(c) or restart(r)? ");
            String choice=scanner.next();
            if(choice.equals("r")){
                return true;
            }
            else if(choice.equals("c")){
                return false;
            }
            else{
                System.out.println("Invalid choice...Try again");
            }
        }
        
    }

    

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean restart;
        do{// for restart
            fifteenPuzzle game = new fifteenPuzzle();
            restart=false;
            while (!game.isSolved()) {
                game.printBoard();
                String move;
                do{
                    System.out.print("Enter move (u = up, d = down, l = left, r = right, re=restart): ");
                    move = scanner.nextLine();//check validity
                    if(!move.equals("u") && !move.equals("d") && !move.equals("l") && !move.equals("r") && !move.equals("re")){
                        System.out.println("Invalid move! Use u, d, l, r or re.");
                    }
                }while(!move.equals("u") && !move.equals("d") && !move.equals("l") && !move.equals("r")  && !move.equals("re"));
                switch (move) {
                    case "u":
                        if (game.blankX <= STARTINDEX) {
                            System.out.println("Cannot move up....Try something else:)");
                        }
                        else{
                            game.moveUp();
                            break;
                        }
                    case "d":
                        if (game.blankX >=ENDINDEX){
                            System.out.println("Cannot move down....Try something else:)");
                        }
                        else{
                            game.moveDown();
                            break;
                        }
                    case "l":
                        if (game.blankY <= STARTINDEX){
                            System.out.println("Cannot move left...Try something else:)");
                        }
                        else{
                            game.moveLeft();
                            break;
                        }
                    case "r":
                        if (game.blankY >= ENDINDEX){
                            System.out.println("Cannot move right...Try something else:)");
                            break;
                        }
                        else{
                            game.moveRight();
                            break;
                        }                       
                    case "re":
                        restart=true;
                        break;
                    default:
                        System.out.println("Invalid move! Use u, d, l, r or re.");
                }
                if(restart){
                    System.out.println("Your game restarts.....:)");
                    break;
                }
            }
            if(game.isSolved()){
                System.out.println("You solved it!");
            }
        
        } while(restart) ;      
        scanner.close();
    }
    
}
