public class NumberPuzzle {

    private int[][] numberBoard;
    private int zeroX;
    private int zeroY;

    public NumberPuzzle(int[][] numberBoard, int zeroX, int zeroY) {
        this.numberBoard = numberBoard;
        this.zeroX = zeroX;
        this.zeroY = zeroY;
    }

    public NumberPuzzle(int sizeX, int sizeY) {
        numberBoard = new int[sizeX][sizeY];
        generateRandomBoard();
    }

    public void generateRandomBoard() {
        int size = numberBoard.length * numberBoard[0].length;
        int[] array = Larik.generateArrayUniform(size, 0, 1);
        array = Larik.randomizeArray(array);
        int index = 0;
        for(int i = 0; i < numberBoard.length; i++){
            for(int j = 0; j < numberBoard[0].length; j++) {
                numberBoard[i][j] = array[index];
                index++;
            }
        }
    }

    public void printBoard() {
        String print = "";
        for(int i = 0; i < numberBoard.length; i++) {
            for(int j = 0; j < numberBoard[i].length; j++) {
                print += numberBoard[i][j] + " ";
            }
        }
        System.out.println(print);

    }

    public int[][] getNumberBoard() {
        return numberBoard;
    }




}
