import echa.Larik;

import java.util.Arrays;

/**
 * Kelas yang merepresentasikan papan permainan puzzle angka. Papan permainan disimpan dalam bentuk matriks 2 dimensi.
 */
public class NumberPuzzle {

    private int[][] numberBoard; //Papan permainan
    private int zeroRow; //Baris tempat angka 0 berada
    private int zeroColumn; //Kolom tempat angka 0 berada
    private Integer score; //Skor dari papan permainan. Semakin kecil berarti semakin dekat dengan goal

    /**
     * Konstruktor untuk membuat objek NumberPuzzle dengan matriks yang sudah ditentukan
     * @param numberBoard
     * @param zeroRow
     * @param zeroColumn
     */
    public NumberPuzzle(int[][] numberBoard, int zeroRow, int zeroColumn) {
        this.numberBoard = numberBoard;
        this.zeroRow = zeroRow;
        this.zeroColumn = zeroColumn;
    }

    /**
     * Konstruktor untuk membuat objek NumberPuzzle dengan ukuran tertentu dan isi papan permainan random
     * @param sizeX
     * @param sizeY
     */
    public NumberPuzzle(int sizeX, int sizeY) {
        numberBoard = new int[sizeX][sizeY];
        generateRandomBoard();
    }

    /**
     * Constructor untuk membuat objek NumberPuzzle dengan matriks yang sudah ditentukan, namun posisi angka 0 belum ditentukan
     * @param numberBoard
     */
    public NumberPuzzle(int[][] numberBoard) {
        this.numberBoard = numberBoard;
        findZero();
    }

    /**
     * Copy constructor
     * @param board
     */
    public NumberPuzzle(NumberPuzzle board) {
        numberBoard = copyArray(board.numberBoard);
        zeroRow = board.zeroRow;
        zeroColumn = board.zeroColumn;
        score = board.score;
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

    /**
     * Method untuk membuat papan permainan secara acak berdasarkan ukurannya. Angka 0 akan dicatat secara otomatis
     */
    public void generateRandomBoard() {
        int size = numberBoard.length * numberBoard[0].length;
        int[] array = Larik.generateArrayUniform(size, 0, 1);
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

    /**
     * Method untuk menggeser angka 0 ke kiri
     */
    public void left(){
        if(zeroColumn > 0) {
            numberBoard[zeroRow][zeroColumn] = numberBoard[zeroRow][zeroColumn-1];
            numberBoard[zeroRow][zeroColumn - 1] = 0;
            zeroColumn--;
        }
    }

    /**
     * Method untuk menggeser angka 0 ke atas
     */
    public void up() {
        if(zeroRow > 0) {
            numberBoard[zeroRow][zeroColumn] = numberBoard[zeroRow - 1][zeroColumn];
            numberBoard[zeroRow - 1][zeroColumn] = 0;
            zeroRow--;
        }
    }

    /**
     * Method untuk menggeser angka 0 ke kanan
     */
    public void right() {
        if(zeroColumn < numberBoard[0].length - 1) {
            numberBoard[zeroRow][zeroColumn] = numberBoard[zeroRow][zeroColumn + 1];
            numberBoard[zeroRow][zeroColumn + 1] = 0;
            zeroColumn++;
        }
    }

    /**
     * Method untuk menggeser angka 0 ke bawah
     */
    public void down() {
        if(zeroRow < numberBoard.length -1) {
            numberBoard[zeroRow][zeroColumn] = numberBoard[zeroRow + 1][zeroColumn];
            numberBoard[zeroRow + 1][zeroColumn] = 0;
            zeroRow++;
        }
    }

    /**
     * Method untuk menduplikasi papan permainan
     * @param array
     * @return
     */
    public static int[][] copyArray(int[][] array) {
        int[][] copy = new int[array.length][];
        for(int i = 0; i < array.length; i++) {
            copy[i] = Arrays.copyOf(array[i], array[i].length);
        }
        return copy;
    }

    /**
     * Method untuk mencari posisi angka 0 pada papan permainan
     */
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

    /**
     * Method untuk mencetak papan permainan
     */
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

    public String toString() {
        StringBuilder print = new StringBuilder();
        for (int[] row : numberBoard) {
            for (int column : row) {
                print.append(column).append(" ");
            }
            print.append("\n");
        }
        return print.toString();
    }

    /**
     * Method untuk menghitung nilai heuristik dari papan permainan relatif terhadap tujuan. Semakin kecil nilai heuristik, semakin dekat dengan tujuan
     * @param board
     */
    public void score(NumberPuzzle board){
        score = 0;
        for(int i = 0; i < numberBoard.length; i++) {
            for(int j = 0; j < numberBoard[i].length;j++) {
                if(numberBoard[i][j] != board.numberBoard[i][j]) {
                    score++;
                }
            }
        }
    }

    public Integer getScore(){
        return score;
    }



}
