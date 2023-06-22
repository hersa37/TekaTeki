import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
/*
Gerardus Kristha Bayu Indraputra    (215314004)
Davino Triyono                      (215314013)
Bernardus Hersa Galih Prakoso       (215314018)
Mikael Oktavian Dwi Sukmadianto     (215314029)
Sherly Permatasari                  (215314030)
 */

public class Main2 {
	static LinkedList<NumberPuzzle> sl;    //Daftar rute node solusi
	static LinkedList<NumberPuzzle> nsl;     //Daftar node yang belum diproses
	static LinkedList<NumberPuzzle> de;     //Daftar node buntu
	static ArrayList<NumberPuzzle> childNodes;       //Daftar anak dari node yang sedang diproses
	static NumberPuzzle cs;           //Node yang sedang diproses
	static int[][] goal = {            //Array goal node
			{1, 2, 3},
			{8, 0, 4},
			{7, 6, 5}
	};
	static NumberPuzzle goalState;          //Objek goal node

	public static void main(String[] args) {

		sl = new LinkedList<>();
		nsl = new LinkedList<>();
		de = new LinkedList<>();
		childNodes = new ArrayList<>();

		//Buat objek goal
		goalState = new NumberPuzzle(goal);

		//Array awal
		int[][] startArray = {
				{1, 2, 3},
				{7, 8, 4},
				{0, 6, 5}
		};
		//Objek board awal
		NumberPuzzle startNode = new NumberPuzzle(startArray);
		startNode.score(goalState);
		//State node yang diproses = board awal
		cs = startNode;
		sl.addFirst(cs);
		nsl.addFirst(cs);

		//Panggil backtrack
		backtrack();

		//Cetak rute solusi
		StringBuilder output = new StringBuilder();
		for (NumberPuzzle solutions : sl) {
			solutions.printBoard();
			output.append(solutions).append("\n");
		}

		System.out.println(output);

		toFile("non-heuristic.txt", output.toString());


	}

	public static void backtrack() {

		while (!nsl.isEmpty()) {                    //Lakukan terus selama masih ada node yang bisa diproses
			if (isEqual(cs, goalState)) { //Cek apakah node sekarang = node goal
				System.out.println("Solution found");
				return; //Selesai jika benar
			}

			createChildNodes();     //Buat anak baru
			int newChildNodes = childNodes.size();  //Counter jumlah anak baru

			if (newChildNodes == 0) {   //Kalau tidak ada anak baru
				// Loop selama rute solusi tidak kosong dan node yang diproses sama dengan node terakhir rute solusi
				while (isEqual(cs, sl.getFirst())) {
					de.addFirst(cs);                 //Pindahkan node yang diproses ke daftar node buntu
					nsl.removeFirst(); //Hapus node pertama dari daftar node diproses
					sl.removeFirst(); //Hapus node pertama dari daftar rute solusi
					if (nsl.isEmpty()) {
						System.out.println("No Solution");
						return;
					}
					cs = nsl.getFirst(); //Proses node terakhir dari daftar yang belum diproses
				}
				sl.addFirst(cs); //Tambah node saat ini ke daftar rute solusi
			} else { //Kalau masih ada anak baru
				for (NumberPuzzle childNode : childNodes) { //Tambahkan anak yang tidak duplikat ke daftar node yang belum diproses
					nsl.addFirst(childNode);
				}
				cs = nsl.getFirst(); //Proses node terakhir dari daftar yang belum diproses
				sl.addFirst(cs); //Tambah node saat ini ke darfat rute solusi
			}
		}
	}

	public static void createChildNodes() {
		childNodes.clear();     //Kosongkan daftar anak
		NumberPuzzle down = new NumberPuzzle(cs);
		down.down();
		NumberPuzzle right = new NumberPuzzle(cs);
		right.right();
		NumberPuzzle up = new NumberPuzzle(cs);
		up.up();
		NumberPuzzle left = new NumberPuzzle(cs);
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
		NumberPuzzle down = new NumberPuzzle(cs);
		down.down();
		down.score(goalState);
		NumberPuzzle right = new NumberPuzzle(cs);
		right.right();
		right.score(goalState);
		NumberPuzzle up = new NumberPuzzle(cs);
		up.up();
		up.score(goalState);
		NumberPuzzle left = new NumberPuzzle(cs);
		left.left();
		left.score(goalState);
		if ((down.getScore() <= cs.getScore()) && isNewNode(down)) {
			childNodes.add(down);
		}
		if ((right.getScore() <= cs.getScore()) && isNewNode(right)) {
			childNodes.add(right);
		}
		if ((up.getScore() <= cs.getScore()) && isNewNode(up)) {
			childNodes.add(up);
		}
		if ((left.getScore() <= cs.getScore()) && isNewNode(left)) {
			childNodes.add(left);
		}
	}

	public static boolean isEqual(NumberPuzzle nodeA, NumberPuzzle nodeB) {
		return Arrays.deepEquals(nodeA.getNumberBoard(), nodeB.getNumberBoard());
	}

	public static boolean isNewNode(NumberPuzzle node) {

		for (NumberPuzzle pending : nsl) {
			if (isEqual(node, pending)) {
				return false;
			}
		}

		for (NumberPuzzle deadEnd : de) {
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
