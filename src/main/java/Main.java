import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
	static List<NumberPuzzle> solutionNodes;
	static List<NumberPuzzle> pendingNodes;
	static List<NumberPuzzle> deadEndNodes;
	static List<NumberPuzzle> childNodes;
	static NumberPuzzle currentState;
	static int[][] boardSolution = {
			{1, 2},
			{3, 0},
			};
	static NumberPuzzle solutionState;

	public static void main(String[] args) {

		solutionNodes = new ArrayList<>();
		pendingNodes = new ArrayList<>();
		deadEndNodes = new ArrayList<>();
		childNodes = new ArrayList<>();

		solutionState = new NumberPuzzle(boardSolution, 1, 1);
		solutionState.printBoard();
		int[][] defaultBoard = {
				{0,2},
				{1,3},
		};
		currentState = new NumberPuzzle(defaultBoard, 1,0);
		currentState.printBoard();
		pendingNodes.add(currentState);
		solutionNodes.add(currentState);
		backtrack();
		System.out.println("\n\nSolution Nodes");
		for (NumberPuzzle solutions : solutionNodes) {
			solutions.printBoard();
		}



	}


	public static void backtrack() {
		while (!pendingNodes.isEmpty()) {
			if ((isEqual(currentState, solutionState))) {
				return;
			}
			childNodes.clear();
			createChildNodes();
			int t = 0;
			for (int i = 0; i < 4; i++) {
				NumberPuzzle child = childNodes.get(i);
				if (isInPending(child) || isInDeadEnd(child) || isInSolutionNodes(child)) {
					t++;
				}
//					pendingNodes.add(child);
//					childNodes.remove(0);
			}

//			solutionNodes.add(pendingNodes.get(pendingNodes.size() - 1));
//			currentState = solutionNodes.get(solutionNodes.size() - 1);

			if (t > 3) {
				while (!solutionNodes.isEmpty() && isEqual(currentState, solutionNodes.get(solutionNodes.size() - 1))) {
					deadEndNodes.add(currentState);
					pendingNodes.remove(pendingNodes.size() - 1);
					solutionNodes.remove(solutionNodes.size() - 1);
					currentState = pendingNodes.get(pendingNodes.size() - 1);
				}
				solutionNodes.add(currentState);
			} else {
				for (int i = 0; i < 4; i++) {
					NumberPuzzle child = childNodes.get(i);
					if (!isInPending(child) && !isInDeadEnd(child) && !isInSolutionNodes(child)) {
						pendingNodes.add(child);
					}
				}
				currentState = pendingNodes.get(pendingNodes.size() - 1);
				solutionNodes.add(currentState);
			}
		}
	}


	public static boolean isEqual(NumberPuzzle nodeA, NumberPuzzle nodeB) {
		return Arrays.deepEquals(nodeA.getNumberBoard(), nodeB.getNumberBoard());
	}

	public static boolean isInPending(NumberPuzzle child) {
		boolean check = false;
		for (NumberPuzzle pending : pendingNodes) {
			if (isEqual(child, pending)) {
				check = true;
				break;
			}
		}
		return check;
	}

	public static boolean isInDeadEnd(NumberPuzzle child) {
		boolean check = false;
		for (NumberPuzzle discard : deadEndNodes) {
			if (isEqual(child, discard)) {
				check = true;
				break;
			}
		}
		return check;
	}

	public static boolean isInSolutionNodes(NumberPuzzle child) {
		boolean check = false;
		for (NumberPuzzle solutionNode : solutionNodes) {
			if (isEqual(child, solutionNode)) {
				check = true;
				break;
			}
		}
		return check;
	}

	public static void createChildNodes() {
		NumberPuzzle left = new NumberPuzzle(currentState);
		left.left();
		childNodes.add(left);NumberPuzzle up = new NumberPuzzle(currentState);
		up.up();
		childNodes.add(up);NumberPuzzle right = new NumberPuzzle(currentState);
		right.right();
		childNodes.add(right);NumberPuzzle down = new NumberPuzzle(currentState);
		down.down();
		childNodes.add(down);
	}
}
