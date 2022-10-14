import java.util.ArrayList;

public class NumberBoardList extends ArrayList<NumberPuzzle> {

	public NumberBoardList(){
		super();
	}

	public void addFirst(NumberPuzzle obj) {
		add(0,obj);
	}

	public NumberPuzzle getFirst() {
		return get(0);
	}

	public void removeFirst() {
		remove(0);
	}
}
