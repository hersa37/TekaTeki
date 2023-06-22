import java.util.LinkedList;

/**
 * Kelas yang merepresentasikan list dari papan permainan puzzle angka
 */
public class NumberBoardList extends LinkedList<NumberPuzzle> {

	public NumberBoardList(){
		super();
	}

	public void addFirst(NumberPuzzle obj) {
		add(0,obj);
	}

	public NumberPuzzle getFirst() {
		return get(0);
	}

	public NumberPuzzle removeFirst() {
		remove(0);
		return null;
	}
}
