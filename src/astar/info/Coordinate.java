package astar.info;

public class Coordinate {
	private int _row, _col;
	
	public Coordinate() {}
	
	public Coordinate(int row, int col) {
		_row = row;
		_col = col;
	}
	
	public Coordinate clone() {
		return new Coordinate(_row, _col);
	}
	
	public void setRow(int row) {
		_row = row;
	}
	
	public int getRow() {
		return _row;
	}
	
	public void setCol(int col) {
		_col = col;
	}
	
	public int getCol() {
		return _col;
	}
	
	public boolean isEqual(Coordinate coord) {
		if (coord.getRow() == _row && coord.getCol() == _col) {
			return true;
		}
		return false;
	}
}
