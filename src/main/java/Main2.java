import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Main2 {
	static NumberBoardList solutionNodes;    //Daftar rute node solusi
	static NumberBoardList pendingNodes;     //Daftar node yang belum diproses
	static NumberBoardList deadEndNodes;     //Daftar node buntu
	static NumberBoardList childNodes;       //Daftar anak dari node yang sedang diproses
	static NumberPuzzle currentState;           //Node yang sedang diproses
	static int[][] boardSolution = {            //Array goal node
			{1, 2, 3},
			{8, 0, 4},
			{7, 6, 5}
	};
	static NumberPuzzle solutionState;          //Objek goal node

	public static void main(String[] args) {

		solutionNodes = new NumberBoardList();
		pendingNodes = new NumberBoardList();
		deadEndNodes = new NumberBoardList();
		childNodes = new NumberBoardList();

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
		backtrack();

		//Cetak rute solusi
		StringBuilder output = new StringBuilder();
		for (NumberPuzzle solutions : solutionNodes) {
			solutions.printBoard();
			output.append(solutions).append("\n");
		}

		System.out.println(output);

		toFile("non-heuristic.txt", output.toString());


	}

	public static void backtrack() {

		while (!pendingNodes.isEmpty()) {                    //Lakukan terus selama masih ada node yang bisa diproses
			if (isEqual(currentState, solutionState)) { //Cek apakah node sekarang = node goal
				System.out.println("Solution found");
				return; //Selesai jika benar
			}

			createChildNodes();     //Buat anak baru
			int newChildNodes = childNodes.size();  //Counter jumlah anak baru

			if (newChildNodes == 0) {   //Kalau tidak ada anak baru
				// Loop selama rute solusi tidak kosong dan node yang diproses sama dengan node terakhir rute solusi
				while (isEqual(currentState, solutionNodes.getFirst())) {
					deadEndNodes.addFirst(currentState);                 //Pindahkan node yang diproses ke daftar node buntu
					pendingNodes.removeFirst(); //Hapus node pertama dari daftar node diproses
					solutionNodes.removeFirst(); //Hapus node pertama dari daftar rute solusi
					if (pendingNodes.isEmpty()) {
						System.out.println("No Solution");
						return;
					}
					currentState = pendingNodes.getFirst(); //Proses node terakhir dari daftar yang belum diproses
				}
				solutionNodes.addFirst(currentState); //Tambah node saat ini ke daftar rute solusi
			} else { //Kalau masih ada anak baru
				for (NumberPuzzle childNode : childNodes) { //Tambahkan anak yang tidak duplikat ke daftar node yang belum diproses
					pendingNodes.addFirst(childNode);
				}
				currentState = pendingNodes.getFirst(); //Proses node terakhir dari daftar yang belum diproses
				solutionNodes.addFirst(currentState); //Tambah node saat ini ke darfat rute solusi
			}
		}
	}

	public static void createChildNodes() {
		childNodes.clear();     //Kosongkan daftar anak
		NumberPuzzle down = new NumberPuzzle(currentState);
		down.down();
		NumberPuzzle right = new NumberPuzzle(currentState);
		right.right();
		NumberPuzzle up = new NumberPuzzle(currentState);
		up.up();
		NumberPuzzle left = new NumberPuzzle(currentState);
		left.left();

		if (isNewNode(down)) {
			childNodes.add(down);
		}
		if (isNewNode(right)) {
			childNodes.add(right);
		}
		if (isNewNode(up)) {
			childNodes.add(up);
		}
		if (isNewNode(left)) {
			childNodes.add(left);
		}
	}

	public static void createChildNodesScored() {
		childNodes.clear();     //Kosongkan daftar anak
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
		if ((down.getScore() <= currentState.getScore()) && isNewNode(down)) {
			childNodes.add(down);
		}
		if ((right.getScore() <= currentState.getScore()) && isNewNode(right)) {
			childNodes.add(right);
		}
		if ((up.getScore() <= currentState.getScore()) && isNewNode(up)) {
			childNodes.add(up);
		}
		if ((left.getScore() <= currentState.getScore()) && isNewNode(left)) {
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
