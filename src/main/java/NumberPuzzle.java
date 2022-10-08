import java.util.Arrays;

public class NumberPuzzle {

    private int[][] numberBoard;
    private int zeroRow;
    private int zeroColumn;

    public NumberPuzzle(int[][] numberBoard, int zeroRow, int zeroColumn) {
        this.numberBoard = numberBoard;
        this.zeroRow = zeroRow;
        this.zeroColumn = zeroColumn;
    }

    public NumberPuzzle(int sizeX, int sizeY) {
        numberBoard = new int[sizeX][sizeY];
        generateRandomBoard();
    }

    public NumberPuzzle(int[][] numberBoard) {
        this.numberBoard = numberBoard;
        findZero();
    }

    public NumberPuzzle(NumberPuzzle board) {
        numberBoard = copyArray(board.numberBoard);
        zeroRow = board.zeroRow;
        zeroColumn = board.zeroColumn;
    }
    public int[][] getNumberBoard() {
        return numberBoard;
    }

    public void setNumberBoard(int[][] numberBoard) {
        this.numberBoard = numberBoard;
    }

    public int getZeroRow() {
        return zeroRow;
    }

    public int getZeroColumn() {
        return zeroColumn;
    }
    public void generateRandomBoard() {
        int size = numberBoard.length * numberBoard[0].length;
        int[] array = Larik.generateArrayUniform(size, -1, 1);
        array = Larik.randomizeArray(array);
        int index = 0;
        for(int i = 0; i < numberBoard.length; i++){
            for(int j = 0; j < numberBoard[0].length; j++) {
                numberBoard[i][j] = array[index];
                if(array[index] == 0) {
                    zeroRow = i;
                    zeroColumn = j;
                }
                index++;
            }
        }
    }

    public void left(){
        if(zeroColumn > 0) {
            numberBoard[zeroRow][zeroColumn] = numberBoard[zeroRow][zeroColumn-1];
            numberBoard[zeroRow][zeroColumn - 1] = 0;
            zeroColumn--;
        }
    }

    public void up() {
        if(zeroRow > 0) {
            numberBoard[zeroRow][zeroColumn] = numberBoard[zeroRow - 1][zeroColumn];
            numberBoard[zeroRow - 1][zeroColumn] = 0;
            zeroRow--;
        }
    }

    public void right() {
        if(zeroColumn < numberBoard[0].length - 1) {
            numberBoard[zeroRow][zeroColumn] = numberBoard[zeroRow][zeroColumn + 1];
            numberBoard[zeroRow][zeroColumn + 1] = 0;
            zeroColumn++;
        }
    }

    public void down() {
        if(zeroRow < numberBoard[0].length -1) {
            numberBoard[zeroRow][zeroColumn] = numberBoard[zeroRow + 1][zeroColumn];
            numberBoard[zeroRow + 1][zeroColumn] = 0;
            zeroRow++;
        }
    }

    public static int[][] copyArray(int[][] array) {
        int[][] copy = new int[array.length][];
        for(int i = 0; i < array.length; i++) {
            copy[i] = Arrays.copyOf(array[i], array[i].length);
        }
        return copy;
    }

    public void findZero() {
        for(int i = 0; i < numberBoard.length; i ++) {
            for(int j = 0; j < numberBoard[0].length; j++) {
                if(numberBoard[i][j] == 0) {
                    zeroRow = i;
                    zeroColumn = j;
                    return;
                }
            }
        }
    }

    public void printBoard() {
        String print = "";
        for (int[] row : numberBoard) {
            for (int column : row) {
                print += column + " ";
            }
            print += "\n";
        }
        System.out.println(print);
    }





}
