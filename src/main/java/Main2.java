import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main2 {
	static List<NumberPuzzle> solutionNodes;    //Daftar rute node solusi
	static List<NumberPuzzle> pendingNodes;     //Daftar node yang belum diproses
	static List<NumberPuzzle> deadEndNodes;     //Daftar node buntu
	static List<NumberPuzzle> childNodes;       //Daftar anak dari node yang sedang diproses
	static NumberPuzzle currentState;           //Node yang sedang diproses
	static int[][] boardSolution = {            //Array goal node
			{1, 2, 3},
			{8, 0, 4},
			{7, 6, 5}
	};
	static NumberPuzzle solutionState;          //Objek goal node

	public static void main(String[] args) {

		solutionNodes = new ArrayList<>();
		pendingNodes = new ArrayList<>();
		deadEndNodes = new ArrayList<>();
		childNodes = new ArrayList<>();

		//Buat objek goal
		solutionState = new NumberPuzzle(boardSolution);

		//Array awal
		int[][] startBoard = {
				{1, 2, 3},
				{7, 8, 4},
				{6, 5, 0}
		};
		//Objek board awal
		NumberPuzzle startNode = new NumberPuzzle(startBoard);
		startNode.score(solutionState);
		//State node yang diproses = board awal
		currentState = startNode;
		solutionNodes.add(currentState);
		pendingNodes.add(currentState);

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
		toFile("output.txt", output.toString());


	}

	public static void backtrack() {

		while (!pendingNodes.isEmpty()) {                    //Lakukan terus selama masih ada node yang bisa diproses
			if (isEqual(currentState, solutionState)) { //Cek apakah node sekarang = node goal
				System.out.println("Solution found");
				return; //Selesai jika benar
			}
			childNodes.clear();     //Kosongkan daftar anak
			createChildNodesScored();     //Buat anak baru
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
				while (solutionNodes.size() > 1 && isEqual(currentState, solutionNodes.get(solutionNodes.size() - 1))) {
					deadEndNodes.add(currentState);                 //Pindahkan node yang diproses ke daftar node buntu
					pendingNodes.remove(pendingNodes.size() - 1); //Hapus node terakhir dari daftar node diproses
					solutionNodes.remove(solutionNodes.size() - 1); //Hapus node terakhir dari daftar rute solusi
					currentState = pendingNodes.get(pendingNodes.size() - 1); //Proses node terakhir dari daftar yang belum diproses
				}

				solutionNodes.add(currentState); //Tambah node saat ini ke daftar rute solusi
			} else { //Kalau masih ada anak baru
				for (NumberPuzzle childNode : childNodes) { //Tambahkan anak yang tidak duplikat ke daftar node yang belum diproses
					if (isNewNode(childNode)) {
						pendingNodes.add(childNode);
					}
				}
				currentState = pendingNodes.get(pendingNodes.size() - 1); //Proses node terakhir dari daftar yang belum diproses
				solutionNodes.add(currentState); //Tambah node saat ini ke darfat rute solusi
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
		childNodes.add(down);
		childNodes.add(right);
		childNodes.add(up);
		childNodes.add(left);
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
			childNodes.add(down);
		}
		if (right.getScore() <= currentState.getScore()) {
			childNodes.add(right);
		}
		if (up.getScore() <= currentState.getScore()) {
			childNodes.add(up);
		}
		if (left.getScore() <= currentState.getScore()) {
			childNodes.add(left);
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
