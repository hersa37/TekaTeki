import echa.Larik;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

public class GUI extends JFrame {

	JLabel goalLabel, currentLabel, pendingLabel, solutionLabel, deadEndLabel, childLabel, currentDeadLabel, iterationLabel;
	JTextArea goalTA, currentTA, pendingTA, solutionTA, deadEndTA, childTA;
	JScrollPane pendingSP, solutionSP, deadEndSP;
	JTable goalTable, startTable;
	JButton resetButton, startButton;
	Backtrack trackBack;
	Thread t;
	int iteration = 1;
	long time = 50;
	boolean started = false;
	boolean isHeuristic = false;

	public GUI() {
		//Pengaturan frame
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
		         UnsupportedLookAndFeelException e) {
			throw new RuntimeException(e);
		}

		setTitle("Teka Teki");
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(true);
		setLocation(500, 200);
		setSize(700, 500);

		//Label indikator iterasi pencarian
		iterationLabel = new JLabel("Iteration");
		iterationLabel.setBounds(50, 0, 100, 50);
		add(iterationLabel);

		//Menampilkan goal state yang sedang dijalankan
		goalLabel = new JLabel("Goal State");
		goalLabel.setBounds(50, 20, 100, 50);
		add(goalLabel);
		goalTA = new JTextArea(3, 5);
		goalTA.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		goalTA.setBounds(50, 60, 65, 85);
		goalTA.setEditable(false);
		add(goalTA);

		//Prompt untuk masukkan goal state
		JLabel goalTableLabel = new JLabel("Insert goal");
		goalTableLabel.setBounds(40, 150, 100, 50);
		add(goalTableLabel);
		goalTable = new JTable(3, 3);
		goalTable.setBounds(50, 190, 50, 50);
		add(goalTable);

		//Menampilkan current state saat dijalankan
		currentLabel = new JLabel("Current State");
		currentLabel.setBounds(150, 20, 100, 50);
		add(currentLabel);
		currentTA = new JTextArea(3, 5);
		currentTA.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		currentTA.setBounds(155, 60, 65, 85);
		currentTA.setEditable(false);
		add(currentTA);
		//Penanda current state adalah dead end
		currentDeadLabel = new JLabel("DEAD END");
		currentDeadLabel.setBounds(155, 130, 100, 50);
		currentDeadLabel.setForeground(Color.red);
		currentDeadLabel.setVisible(false);
		add(currentDeadLabel);

		//Prompt untuk memasukkan nilai awal
		JLabel startTableLabel = new JLabel("Insert start");
		startTableLabel.setBounds(145, 150, 100, 50);
		add(startTableLabel);
		startTable = new JTable(3, 3);
		startTable.setBounds(155, 190, 50, 50);
		add(startTable);

		//Button untuk memasukkan nilai default sebagai goal dan start
		JButton defaultValue = new JButton("Default value");
		defaultValue.setBounds(140, 250, 100, 20);
		defaultValue.setMargin(new Insets(0, 0, 0, 0));
		//Langsung buat objek dari interface ActionListener sebagai parameter
		defaultValue.addActionListener(actionEvent -> {
			int[][] defaultGoal = {
					{1, 2, 3},
					{8, 0, 4},
					{7, 6, 5}
			};
			int[][] defaultStart = {
					{1, 2, 3},
					{0, 8, 4},
					{7, 6, 5}
			};
			//Masukkan nilai array ke table
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					goalTable.setValueAt(defaultGoal[i][j], i, j);
					startTable.setValueAt(defaultStart[i][j], i, j);
				}
			}
		});
		add(defaultValue);

		//Tentukan delay operasi
		JLabel delayLabel = new JLabel("Delay (ms)");
		delayLabel.setBounds(30, 290, 100, 20);
		add(delayLabel);
		JTextField delayTF = new JTextField("1000");
		delayTF.setBounds(30, 310, 100, 20);
		delayTF.addActionListener(actionEvent -> {
			try {
				time = Long.parseLong(delayTF.getText());
				if (time < 0) {
					time = 0;
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Incorrect type", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		add(delayTF);

		JButton randomStartB = new JButton("Random Start");
		randomStartB.setBounds(70, 340, 120, 20);
		randomStartB.addActionListener(actionEvent -> {
			int[] array = Larik.generateArrayUniform(9, 0, 1);
			array = Larik.randomizeArray(array);
			int index = 0;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					startTable.setValueAt(array[index], i, j);
					index++;
				}
			}
		});
		add(randomStartB);

		//Tentukan backtrack heuristic atau bukan
		JLabel isHeuristicLabel = new JLabel("Heuristic");
		isHeuristicLabel.setBounds(30, 250, 100, 20);
		add(isHeuristicLabel);
		JCheckBox isHeuristicCB = new JCheckBox();
		isHeuristicCB.setBounds(90, 250, 20, 20);
		isHeuristicCB.setOpaque(false);
		add(isHeuristicCB);

		//Tombol untuk mulai. Berubah tergantung kondisi operasi
		startButton = new JButton("Start");
		startButton.setBounds(155, 290, 50, 20);
		startButton.setMargin(new Insets(0, 0, 0, 0));
		startButton.addActionListener(actionEvent -> {
			if (!started) {
				int[][] goal = new int[3][3];
				int[][] start = new int[3][3];
				try {
					for (int i = 0; i < 3; i++) {
						for (int j = 0; j < 3; j++) {
							goal[i][j] = (int) goalTable.getValueAt(i, j);
							start[i][j] = (int) startTable.getValueAt(i, j);
						}
					}
					//Sembunyikan table untuk masukkan goal dan start
					goalTableLabel.setVisible(false);
					goalTable.setVisible(false);
					startTableLabel.setVisible(false);
					startTable.setVisible(false);
					defaultValue.setVisible(false);
					randomStartB.setVisible(false);
					//Set text untuk tombol menjadi "Pause" agar bisa digunakan untuk pause
					startButton.setText("Pause");
					startButton.setBounds(155, 290, 70, 20);
					//Tampilkan tombol reset
					resetButton.setVisible(true);
					//Cek heuristic atau bukan
					if (isHeuristicCB.isSelected()) {
						isHeuristic = true;
					}
					isHeuristicCB.setEnabled(false);
					//Parse delay
					time = Long.parseLong(delayTF.getText());
					if (time < 0) {
						time = 0;
					}
					//Set flag menandakan sudah dimulai
					started = true;
					//Buat objek backtrack runnable
					trackBack = new Backtrack(goal, start);

					//Buat thread baru dengan objek runnable
					t = new Thread(trackBack);
					t.start();
				} catch (InputMismatchException e) {
					JOptionPane.showMessageDialog(null, "Masukkan integer saja", "Error", JOptionPane.ERROR_MESSAGE);
				} catch (NullPointerException e) {
					JOptionPane.showMessageDialog(null, "Masukkan semua angka", "Error", JOptionPane.ERROR_MESSAGE);

				}
				//Pause operasi
			} else if (startButton.getText().equals("Pause")) {
				startButton.setText("Resume");
				trackBack.pause();
				//Lanjutkan operasi
			} else {
				startButton.setText("Pause");
				trackBack.resume();
			}
		});
		add(startButton);

		//Tombol reset. Tidak muncul saat belum mulai
		resetButton = new JButton("Reset");
		resetButton.setBounds(155, 320, 70, 20);
		resetButton.setMargin(new Insets(0, 0, 0, 0));
		resetButton.setVisible(false);
		resetButton.addActionListener(actionEvent -> {
			//Berhentikan runnable dan tunggu thread berhenti
			trackBack.stop();
			try {
				t.join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

			//Reset semuanya
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					goalTable.setValueAt("", i, j);
					startTable.setValueAt("", i, j);

				}
			}

			started = false;

			iterationLabel.setText("Iteration ");
			iteration = 0;

			goalTable.setVisible(true);
			goalTableLabel.setVisible(true);

			startTable.setVisible(true);
			startTableLabel.setVisible(true);

			defaultValue.setVisible(true);

			startButton.setText("Start");

			goalTA.setText("");
			currentTA.setText("");
			childTA.setText("");

			pendingTA.setText("");
			pendingLabel.setText("Pending Nodes");

			solutionTA.setText("");
			solutionTA.setBackground(Color.white);
			solutionLabel.setText("Solution Nodes");

			deadEndTA.setText("");
			deadEndLabel.setText("Dead End Nodes");

			currentDeadLabel.setVisible(false);
			currentDeadLabel.setForeground(Color.red);
			currentDeadLabel.setText("DEAD END");

			isHeuristicCB.setSelected(false);
			isHeuristicCB.setEnabled(true);
			isHeuristic = false;

			delayTF.setText("1000");
			randomStartB.setVisible(true);

			resetButton.setVisible(false);

		});
		add(resetButton);


		//Child nodes
		childLabel = new JLabel("Child Nodes");
		childLabel.setBounds(275, 0, 100, 50);
		add(childLabel);
		childTA = new JTextArea(5, 5);
		childTA.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		childTA.setBounds(280, 35, 65, 420);
		childTA.setEditable(false);
		add(childTA);

		//Pending nodes
		pendingLabel = new JLabel("Pending Nodes");
		pendingLabel.setBounds(370, 0, 100, 50);
		add(pendingLabel);
		pendingTA = new JTextArea(15, 6);
		pendingTA.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		pendingTA.setEditable(false);
		pendingSP = new JScrollPane(pendingTA);
		pendingSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pendingSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pendingSP.setBounds(375, 35, 85, 420);
		add(pendingSP);

		//Solution nodes
		solutionLabel = new JLabel("Solution Nodes");
		solutionLabel.setBounds(465, 0, 100, 50);
		add(solutionLabel);
		solutionTA = new JTextArea(15, 6);
		solutionTA.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		solutionTA.setEditable(false);
		solutionSP = new JScrollPane(solutionTA);
		solutionSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		solutionSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		solutionSP.setBounds(470, 35, 85, 420);
		add(solutionSP);

		//Dead end nodes
		deadEndLabel = new JLabel("Dead End Nodes");
		deadEndLabel.setBounds(560, 0, 100, 50);
		add(deadEndLabel);
		deadEndTA = new JTextArea(15, 6);
		deadEndTA.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		deadEndTA.setEditable(false);
		deadEndSP = new JScrollPane(deadEndTA);
		deadEndSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		deadEndSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		deadEndSP.setBounds(565, 35, 85, 420);
		add(deadEndSP);


		setVisible(true);
	}

	public static void main(String[] args) {
		new GUI();
	}

	//Inner class untuk backtrack. Implement runnable agar bisa dijalankan dalam thread
	@SuppressWarnings("DuplicatedCode")
	class Backtrack implements Runnable {

		private volatile boolean running = true;  //Flag untuk program jalan
		private volatile boolean paused = false; //Flag untuk program pause
		private final Object pauseLock = new Object();  //Objek untuk sinkronisasi
		private int[][] goal, start;

		public Backtrack(int[][] goal, int[][] start) {
			this.goal = goal;
			this.start = start;
		}

		//Method yang dijalankan saat runnable di-start
		@Override
		public void run() {
			NumberBoardList solutionNodes = new NumberBoardList(); // Menyimpan rute penyelesaian
			NumberBoardList pendingNodes = new NumberBoardList(); // Menyimpan daftar node yang sedang/akan diproses

			NumberPuzzle goalState = new NumberPuzzle(goal); // Kondisi tujuan

			NumberPuzzle startNode = new NumberPuzzle(start); // Kondisi awal
			startNode.score(goalState); // Menentukan skor start node untuk digunakan saat best first search


			NumberPuzzle currentState = startNode; // Menentukan node awal sebagai node sekarang

			solutionNodes.addFirst(currentState); // Tambahkan node sekarang ke daftar solusi
			pendingNodes.addFirst(currentState); // Tambahkan node sekarang ke daftar node untuk dikerjakan

			NumberBoardList deadEndNodes = new NumberBoardList(); // List untuk node yang terbukti tidak bisa mencapai goal
			PriorityQueue<NumberPuzzle> childNodes = new PriorityQueue<>(4, Comparator.comparingInt(NumberPuzzle::getScore) ); // List untuk node anak dari current state

			goalTA.setText(printBoard(goalState));
			currentTA.setText(printBoard(currentState));
			pendingTA.setText(printBoardList(pendingNodes));
			pendingLabel.setText("Pending " + pendingNodes.size());
			solutionTA.setText(printBoardList(solutionNodes));
			solutionLabel.setText("Solution " + solutionNodes.size());
			System.out.println("Here");

			// Start backtrack
			while (running && !pendingNodes.isEmpty()) { // Selama masih ada node untuk dioperasikan dan masih diatur untuk tetap jalan, telusuri terus
				synchronized (pauseLock) { // Digunakan untuk memberhentikan kode saat diminta
					if (!running) {
						break;
					}
					if (paused) {
						try {
							pauseLock.wait();
						} catch (InterruptedException e) {
							break;
						}
						if (!running) {
							break;
						}
					}
				}

				iterationLabel.setText("Iteration " + iteration); // Menghitung iterasi
				iteration++;
				childNodes.clear();     //Kosongkan daftar anak
				childTA.setText("");    //Kosongkan text area
				currentTA.setText(printBoard(currentState));

				if (isEqual(currentState, goalState)) { // Cek apakah kondisi sekarang adalah goal. Jika iya, maka berhentikan program dan tunjukkan kalau solusi ditemukan
					System.out.println("Solution found");
					currentDeadLabel.setText("Goal Found");
					currentDeadLabel.setForeground(Color.black);
					currentDeadLabel.setVisible(true);
					childTA.setText("");
					solutionTA.setBackground(Color.green);
					return; //Selesai jika benar
				}

				// Cek apakah heuristic atau tidak. Heuristic menggunakan best first search. Jika tidak, maka akan menelusuri secaara urut.
				if (isHeuristic) {
					createChildNodesScored(currentState, goalState, childNodes, pendingNodes, deadEndNodes); //Menggunakan best first search
				} else {
					createChildNodes(currentState, childNodes, pendingNodes, deadEndNodes);
				}

				if (childNodes.isEmpty()) {   //Kalau tidak ada anak baru, berarti node merupakan dead end. Masukkan node-node yang menuju node ini ke dalam dead end
					currentDeadLabel.setVisible(true);
					// Loop selama rute solusi tidak kosong dan node yang diproses sama dengan node terakhir rute solusi
					while (isEqual(currentState, solutionNodes.getFirst())) {
						deadEndNodes.addFirst(currentState);                 //Pindahkan node yang diproses ke daftar node buntu
						deadEndTA.setText(printBoardList(deadEndNodes));
						deadEndLabel.setText("Dead end " + deadEndNodes.size());

						//Delay
						try {
							TimeUnit.MILLISECONDS.sleep(time);
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}

						//Cek pause atau tidak
						synchronized (pauseLock) {
							if (!running) {
								break;
							}
							if (paused) {
								try {
									pauseLock.wait();
								} catch (InterruptedException e) {
									break;
								}
								if (!running) {
									break;
								}
							}
						}

						pendingNodes.removeFirst(); //Hapus node pertama di daftar node dibuka
						solutionNodes.removeFirst(); //Hapus node pertama di daftar rute solusi
						pendingLabel.setText("Pending " + pendingNodes.size());
						solutionLabel.setText("Solution " + solutionNodes.size());
						//Keluar kalau root dead end

						if (pendingNodes.isEmpty()) {
							currentDeadLabel.setText("No Solution");
							pendingTA.setText("");
							solutionTA.setText("");
							return;
						}

						pendingTA.setText(printBoardList(pendingNodes));
						solutionTA.setText(printBoardList(solutionNodes));


						currentState = pendingNodes.getFirst(); //Proses node terakhir dari daftar yang belum diproses
						currentTA.setText(printBoard(currentState));
					}

					currentDeadLabel.setVisible(false);         //Sembunyikan tulisan "dead end"
					solutionNodes.addFirst(currentState);   //Tambah node saat ini ke daftar rute solusi
					solutionTA.setText(printBoardList(solutionNodes));
				} else { //Kalau masih ada anak baru
					childTA.setText(printBoardList(childNodes));

					for (NumberPuzzle childNode : childNodes) { //Tambahkan anak
						pendingNodes.addFirst(childNode);
					}

					//Delay
					try {
						TimeUnit.MILLISECONDS.sleep(time);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}

					pendingTA.setText(printBoardList(pendingNodes));
					pendingLabel.setText("Pending " + pendingNodes.size());

					currentState = pendingNodes.getFirst(); //Proses node pertama dari daftar yang belum diproses
					currentTA.setText(printBoard(currentState));

					solutionNodes.addFirst(currentState); //Tambah node saat ini ke darfat rute solusi
					solutionTA.setText(printBoardList(solutionNodes));
					solutionLabel.setText("Solution " + solutionNodes.size());
				}
			}
		}

		/**
		 * Method untuk set pause
		 */
		public void pause() {
			paused = true;
		}

		/**
		 * Method untuk melanjutkan resume
		 */
		public void resume() {
			synchronized (pauseLock) {
				paused = false;
				pauseLock.notifyAll();
			}
		}

		//Method untuk berhentikan jalan program
		public void stop() {
			running = false;
			resume();
		}

		//Buat anak dari node, bandingkan dengan daftar lain untuk cari tahu unik atau tidak
		public void createChildNodes(NumberPuzzle currentState, PriorityQueue<NumberPuzzle> childNodes, NumberBoardList pendingNodes, NumberBoardList deadEndNodes) {
			NumberPuzzle down = new NumberPuzzle(currentState);
			down.down();
			NumberPuzzle right = new NumberPuzzle(currentState);
			right.right();
			NumberPuzzle up = new NumberPuzzle(currentState);
			up.up();
			NumberPuzzle left = new NumberPuzzle(currentState);
			left.left();
			if (isNewNode(down, pendingNodes, deadEndNodes)) {
				childNodes.add(down);
			}
			if (isNewNode(right, pendingNodes, deadEndNodes)) {
				childNodes.add(right);
			}
			if (isNewNode(up, pendingNodes, deadEndNodes)) {
				childNodes.add(up);
			}
			if (isNewNode(left, pendingNodes, deadEndNodes)) {
				childNodes.add(left);
			}
		}

		//Buat anak dari node, bandingkan dengan daftar untuk cari tahu unik atau tidak. Masukkan yang unik dan nilainya lebih kecil daripada node parent
		public void createChildNodesScored(NumberPuzzle currentState, NumberPuzzle goalState, PriorityQueue<NumberPuzzle> childNodes, NumberBoardList pendingNodes, NumberBoardList deadEndNodes) {
			@SuppressWarnings("DuplicatedCode") NumberPuzzle down = new NumberPuzzle(currentState);
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
			if (down.getScore() <= currentState.getScore() && isNewNode(down, pendingNodes, deadEndNodes)) {
				childNodes.add(down);
			}
			if (right.getScore() <= currentState.getScore() && isNewNode(right, pendingNodes, deadEndNodes)) {
				childNodes.add(right);
			}
			if (up.getScore() <= currentState.getScore() && isNewNode(up, pendingNodes, deadEndNodes)) {
				childNodes.add(up);
			}
			if (left.getScore() <= currentState.getScore() && isNewNode(left, pendingNodes, deadEndNodes)) {
				childNodes.add(left);
			}
		}

		//Cari tau apakah 2 node sama
		public boolean isEqual(NumberPuzzle nodeA, NumberPuzzle nodeB) {
			return Arrays.deepEquals(nodeA.getNumberBoard(), nodeB.getNumberBoard());
		}

		//cari tahu apakah node baru
		public boolean isNewNode(NumberPuzzle node, NumberBoardList pendingNodes, NumberBoardList deadEndNodes) {

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

		public String printBoardList(PriorityQueue<NumberPuzzle> list) {
			StringBuilder puzzle = new StringBuilder();
			for (NumberPuzzle numberPuzzle : list) {
				puzzle.append(printBoard(numberPuzzle));
				puzzle.append("\n\n");
			}
			puzzle.deleteCharAt(puzzle.length() - 1);
			puzzle.deleteCharAt(puzzle.length() - 1);
			return puzzle.toString();
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
	}
}
