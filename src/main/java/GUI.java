import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class GUI extends JFrame {
	NumberBoardList solutionNodes;    //Daftar rute node solusi
	NumberBoardList pendingNodes;     //Daftar node yang belum diproses
	NumberBoardList deadEndNodes;     //Daftar node buntu
	NumberBoardList childNodes;       //Daftar anak dari node yang sedang diproses
	NumberPuzzle currentState;           //Node yang sedang diproses
	int[][] boardSolution = {            //Array goal node
			{1, 2, 3},
			{8, 0, 4},
			{7, 6, 5}
	};
	NumberPuzzle goalState;          //Objek goal node
	JLabel goalLabel, currentLabel, pendingLabel, solutionLabel, deadEndLabel, childLabel, currentDeadLabel, iterationLabel;
	JTextArea goalTA, currentTA, pendingTA, solutionTA, deadEndTA, childTA;
	JScrollPane pendingSP, solutionSP, deadEndSP;
	int iteration = 0;

	public GUI() {
		setTitle("Teka Teki");
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(true);
		setLocation(500, 200);
		setSize(850, 500);

		iterationLabel = new JLabel("Iteration");
		iterationLabel.setBounds(50, 125, 100, 50);
		add(iterationLabel);

		goalLabel = new JLabel("Goal State");
		goalLabel.setBounds(50, 150, 100, 50);
		add(goalLabel);
		goalTA = new JTextArea(3, 5);
		goalTA.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		goalTA.setBounds(50, 200, 65, 85);
		goalTA.setEditable(false);
		add(goalTA);


		currentLabel = new JLabel("Current State");
		currentLabel.setBounds(150, 150, 100, 50);
		add(currentLabel);
		currentTA = new JTextArea(3, 5);
		currentTA.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		currentTA.setBounds(155, 200, 65, 85);
		currentTA.setEditable(false);
		add(currentTA);
		currentDeadLabel = new JLabel();
		currentDeadLabel.setBounds(155, 275, 100, 50);
		currentDeadLabel.setForeground(Color.red);
		add(currentDeadLabel);

		childLabel = new JLabel("Child Nodes");
		childLabel.setBounds(275, 0, 100, 50);
		add(childLabel);
		childTA = new JTextArea(5, 5);
		childTA.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		childTA.setBounds(280, 35, 65, 420);
		childTA.setEditable(false);
		add(childTA);

		pendingLabel = new JLabel("Pending Nodes");
		pendingLabel.setBounds(450, 0, 100, 50);
		add(pendingLabel);
		pendingTA = new JTextArea(15, 6);
		pendingTA.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		pendingTA.setEditable(false);
		pendingSP = new JScrollPane(pendingTA);
		pendingSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pendingSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pendingSP.setBounds(455, 35, 85, 420);
		add(pendingSP);

		solutionLabel = new JLabel("Solution Nodes");
		solutionLabel.setBounds(575, 0, 100, 50);
		add(solutionLabel);
		solutionTA = new JTextArea(15, 6);
		solutionTA.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		solutionTA.setEditable(false);
		solutionSP = new JScrollPane(solutionTA);
		solutionSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		solutionSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		solutionSP.setBounds(580, 35, 85, 420);
		add(solutionSP);

		deadEndLabel = new JLabel("Dead End Nodes");
		deadEndLabel.setBounds(700, 0, 100, 50);
		add(deadEndLabel);
		deadEndTA = new JTextArea(15, 6);
		deadEndTA.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		deadEndTA.setEditable(false);
		deadEndSP = new JScrollPane(deadEndTA);
		deadEndSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		deadEndSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		deadEndSP.setBounds(705, 35, 85, 420);
		add(deadEndSP);


		setVisible(true);
		tekaTeki();
	}

	public void tekaTeki() {
		solutionNodes = new NumberBoardList();
		pendingNodes = new NumberBoardList();
		deadEndNodes = new NumberBoardList();
		childNodes = new NumberBoardList();

		//Buat objek goal
		goalState = new NumberPuzzle(boardSolution);

		//Array awal
		int[][] startBoard = {
				{0, 1, 2},
				{7, 8, 3},
				{6, 5, 4}
		};
		//Objek board awal
		NumberPuzzle startNode = new NumberPuzzle(startBoard);
		startNode.score(goalState);
		//State node yang diproses = board awal
		currentState = startNode;

		solutionNodes.addFirst(currentState);
		pendingNodes.addFirst(currentState);

		goalTA.setText(printBoard(goalState));
		currentTA.setText(printBoard(currentState));
		pendingTA.setText(printBoardList(pendingNodes));
		pendingLabel.setText("Pending " + pendingNodes.size());
		solutionTA.setText(printBoardList(solutionNodes));
		solutionLabel.setText("Solution " + solutionNodes.size());

		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		//Panggil backtrack
		backtrack();
		//Cetak rute solusi
//		StringBuilder output = new StringBuilder();
//		for (NumberPuzzle solutions : solutionNodes) {
//			solutions.printBoard();
//			output.append(solutions.toString() + "\n");
//		}
//		toFile("non-heuristic.txt", output.toString());
	}

	public void backtrack() {

		while (!pendingNodes.isEmpty()) {                    //Lakukan terus selama masih ada node yang bisa diproses
			iterationLabel.setText("Iteration " + iteration);
			iteration++;
			childNodes.clear();     //Kosongkan daftar anak
			if (isEqual(currentState, goalState)) { //Cek apakah node sekarang = node goal
				System.out.println("Solution found");
				currentDeadLabel.setText("Goal Found");
				currentDeadLabel.setForeground(Color.black);
				childTA.setText("");
				solutionTA.setBackground(Color.green);
				return; //Selesai jika benar
			}

			createChildNodesScored();     //Buat anak baru

			int newChildNodes = childNodes.size();  //Counter jumlah anak baru

			if (newChildNodes == 0) {   //Kalau tidak ada anak baru
				// Loop selama rute solusi tidak kosong dan node yang diproses sama dengan node terakhir rute solusi
				while (isEqual(currentState, solutionNodes.getFirst())) {
					deadEndNodes.addFirst(currentState);                 //Pindahkan node yang diproses ke daftar node buntu
					deadEndTA.setText(printBoardList(deadEndNodes));
					currentDeadLabel.setText("DEAD END");
					deadEndLabel.setText("Dead end " + deadEndNodes.size());

					pendingNodes.removeFirst(); //Hapus node pertama di daftar node dibuka
					pendingLabel.setText("Pending " + pendingNodes.size());
					pendingTA.setText(printBoardList(pendingNodes));
					if (pendingNodes.isEmpty()) {
						currentDeadLabel.setText("No Solution");
						return;
					}

					solutionNodes.removeFirst(); //Hapus node pertama di daftar rute solusi
					solutionTA.setText(printBoardList(solutionNodes));
					solutionLabel.setText("Solution " + solutionNodes.size());

					currentState = pendingNodes.getFirst(); //Proses node terakhir dari daftar yang belum diproses
					currentTA.setText(printBoard(currentState));
				}
				currentDeadLabel.setText("");

				solutionNodes.addFirst(currentState); //Tambah node saat ini ke daftar rute solusi
				solutionTA.setText(printBoardList(solutionNodes));
			} else { //Kalau masih ada anak baru
				childTA.setText(printBoardList(childNodes));

				for (NumberPuzzle childNode : childNodes) { //Tambahkan anak yang tidak duplikat ke daftar node yang belum diproses
					pendingNodes.addFirst(childNode);
				}

				pendingTA.setText(printBoardList(pendingNodes));
				pendingLabel.setText("Pending " + pendingNodes.size());

				currentState = pendingNodes.getFirst(); //Proses node terakhir dari daftar yang belum diproses
				currentTA.setText(printBoard(currentState));

				solutionNodes.addFirst(currentState); //Tambah node saat ini ke darfat rute solusi
				solutionTA.setText(printBoardList(solutionNodes));
				solutionLabel.setText("Solution " + solutionNodes.size());
			}
		}

	}

	public void createChildNodes() {
		NumberPuzzle down = new NumberPuzzle(currentState);
		down.down();
		NumberPuzzle right = new NumberPuzzle(currentState);
		right.right();
		NumberPuzzle up = new NumberPuzzle(currentState);
		up.up();
		NumberPuzzle left = new NumberPuzzle(currentState);
		left.left();
		if (isNewNode(down)) {
			childNodes.addFirst(down);
		}
		if (isNewNode(right)) {
			childNodes.addFirst(right);
		}
		if (isNewNode(up)) {
			childNodes.addFirst(up);
		}
		if (isNewNode(left)) {
			childNodes.addFirst(left);
		}
	}

	public void createChildNodesScored() {
		NumberPuzzle down = new NumberPuzzle(currentState);
		down.down();
		down.score(goalState);
		NumberPuzzle right = new NumberPuzzle(currentState);
		right.right();
		right.score(goalState);
		NumberPuzzle up = new NumberPuzzle(currentState);
		up.up();
		up.score(goalState);
		NumberPuzzle left = new NumberPuzzle(currentState);
		left.left();
		left.score(goalState);
		if (down.getScore() <= currentState.getScore() && isNewNode(down)) {
			childNodes.addFirst(down);
		}
		if (right.getScore() <= currentState.getScore() && isNewNode(right)) {
			childNodes.addFirst(right);
		}
		if (up.getScore() <= currentState.getScore() && isNewNode(up)) {
			childNodes.addFirst(up);
		}
		if (left.getScore() <= currentState.getScore() && isNewNode(left)) {
			childNodes.addFirst(left);
		}
	}


	public boolean isEqual(NumberPuzzle nodeA, NumberPuzzle nodeB) {
		return Arrays.deepEquals(nodeA.getNumberBoard(), nodeB.getNumberBoard());
	}

	public boolean isNewNode(NumberPuzzle node) {

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


	public void toFile(String name, String content) {
		try {
			FileWriter writer = new FileWriter(name);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			System.out.println("Error");
			e.printStackTrace();
		}
	}

	public String printBoardList(NumberBoardList list) {
		StringBuilder x = new StringBuilder();
		for (NumberPuzzle numberPuzzle : list) {
			x.append(printBoard(numberPuzzle));
			x.append("\n\n");
		}
		x.deleteCharAt(x.length() - 1);
		x.deleteCharAt(x.length() - 1);
		return x.toString();
	}

	public String printBoard(NumberPuzzle numberPuzzle) {
		StringBuilder x = new StringBuilder();
		for (int i = 0; i < numberPuzzle.getNumberBoard().length; i++) {
			for (int j = 0; j < numberPuzzle.getNumberBoard()[i].length; j++) {
				x.append(numberPuzzle.getNumberBoard()[i][j]);
				if (j < numberPuzzle.getNumberBoard()[i].length - 1) x.append(" ");
			}
			x.append("\n");
		}
		x.deleteCharAt(x.length() - 1);
		return x.toString();
	}

	public static void main(String[] args) {
		new GUI();
	}
}
