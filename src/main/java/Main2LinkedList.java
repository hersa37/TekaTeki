import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class Main2LinkedList {
	static LinkedList<NumberPuzzle> solutionNodes;    //Daftar rute node solusi
	static LinkedList<NumberPuzzle> pendingNodes;     //Daftar node yang belum diproses
	static LinkedList<NumberPuzzle> deadEndNodes;     //Daftar node buntu
	static LinkedList<NumberPuzzle> childNodes;       //Daftar anak dari node yang sedang diproses
	static NumberPuzzle currentState;           //Node yang sedang diproses
	static int[][] boardSolution = {            //Array goal node
			{1, 2, 3},
			{8, 0, 4},
			{7, 6, 5}
	};
	static NumberPuzzle solutionState;          //Objek goal node

	public static void main(String[] args) {

		solutionNodes = new LinkedList<>();
		pendingNodes = new LinkedList<>();
		deadEndNodes = new LinkedList<>();
		childNodes = new LinkedList<>();

		//Buat objek goal
		solutionState = new NumberPuzzle(boardSolution);

		//Array awal
		int[][] startBoard = {
				{1, 2, 3},
				{7, 8, 4},
				{0, 6, 5}
		};
		//Objek board awal
		NumberPuzzle startNode = new NumberPuzzle(startBoard);
		startNode.score(solutionState);
		//State node yang diproses = board awal
		currentState = startNode;
		solutionNodes.addFirst(currentState);
		pendingNodes.addFirst(currentState);

		//Panggil backtrack
		try {
			backtrack();
		} catch (ArrayIndexOutOfBoundsException ar) {
			System.out.println("No solution");
		}
		//Cetak rute solusi
		StringBuilder output = new StringBuilder();
		for (NumberPuzzle solutions : solutionNodes) {
			solutions.printBoard();
			output.append(solutions.toString() + "\n");
		}


		toFile("non-heuristic.txt", output.toString());


	}

	public static void backtrack() {

		while (!pendingNodes.isEmpty()) {                    //Lakukan terus selama masih ada node yang bisa diproses
			if (isEqual(currentState, solutionState)) { //Cek apakah node sekarang = node goal
				System.out.println("Solution found");
				return; //Selesai jika benar
			}
			childNodes.clear();     //Kosongkan daftar anak
			createChildNodes();     //Buat anak baru
			int newChildNodes = childNodes.size();  //Counter jumlah anak baru
			for (NumberPuzzle childNode : childNodes) {
				if (!isNewNode(childNode)) { //Cek apakah anak sudah ada di daftar lain atau belum
					newChildNodes--; //Kurangi counter anak baru
				} else {
					break;  //Keluar dari loop karena masih ada anak baru
				}
			}
			if (newChildNodes == 0) {   //Kalau tidak ada anak baru
				// Loop selama rute solusi tidak kosong dan node yang diproses sama dengan node terakhir rute solusi
				while (isEqual(currentState, solutionNodes.getFirst())) {
					deadEndNodes.addFirst(currentState);                 //Pindahkan node yang diproses ke daftar node buntu
					pendingNodes.removeFirst(); //Hapus node pertama di daftar node dibuka
					solutionNodes.removeFirst(); //Hapus node pertama di daftar rute solusi
					currentState = pendingNodes.getFirst(); //Proses node pertama dari daftar yang belum diproses
				}
				solutionNodes.addFirst(currentState); //Tambah node saat ini ke daftar rute solusi
			} else { //Kalau masih ada anak baru
				for (NumberPuzzle childNode : childNodes) { //Tambahkan anak yang tidak duplikat ke daftar node yang belum diproses
					if (isNewNode(childNode)) {
						pendingNodes.addFirst(childNode);
					}
				}
				currentState = pendingNodes.getFirst(); //Proses node pertama dari daftar yang belum diproses
				solutionNodes.addFirst(currentState); //Tambah node saat ini ke darfat rute solusi
			}
		}
	}

	public static void createChildNodes() {
		NumberPuzzle down = new NumberPuzzle(currentState);
		down.down();
		NumberPuzzle right = new NumberPuzzle(currentState);
		right.right();
		NumberPuzzle up = new NumberPuzzle(currentState);
		up.up();
		NumberPuzzle left = new NumberPuzzle(currentState);
		left.left();
		childNodes.addFirst(down);
		childNodes.addFirst(right);
		childNodes.addFirst(up);
		childNodes.addFirst(left);
	}

	public static void createChildNodesScored() {
		NumberPuzzle down = new NumberPuzzle(currentState);
		down.down();
		down.score(solutionState);
		NumberPuzzle right = new NumberPuzzle(currentState);
		right.right();
		right.score(solutionState);
		NumberPuzzle up = new NumberPuzzle(currentState);
		up.up();
		up.score(solutionState);
		NumberPuzzle left = new NumberPuzzle(currentState);
		left.left();
		left.score(solutionState);
		if (down.getScore() <= currentState.getScore()) {
			childNodes.addFirst(down);
		}
		if (right.getScore() <= currentState.getScore()) {
			childNodes.addFirst(right);
		}
		if (up.getScore() <= currentState.getScore()) {
			childNodes.addFirst(up);
		}
		if (left.getScore() <= currentState.getScore()) {
			childNodes.addFirst(left);
		}
	}


	public static boolean isEqual(NumberPuzzle nodeA, NumberPuzzle nodeB) {
		return Arrays.deepEquals(nodeA.getNumberBoard(), nodeB.getNumberBoard());
	}

	public static boolean isNewNode(NumberPuzzle node) {

		for (NumberPuzzle pending : pendingNodes) {
			if (isEqual(node, pending)) {
				return false;
			}
		}

		for (NumberPuzzle deadEnd : deadEndNodes) {
			if (isEqual(node, deadEnd)) {
				return false;
			}
		}
		return true;
	}

	public static void toFile(String name, String content) {
		try {
			FileWriter writer = new FileWriter(name);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			System.out.println("Error");
			e.printStackTrace();
		}
	}


}
