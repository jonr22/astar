package astar.info;

/**
 * Container for grid coordinates, 0-based
 * 
 * @author Jonathan Reimels
 *
 */
public class Coordinate {
	private int _row, _col;
	
	/**
	 * Default Constructor
	 */
	public Coordinate() {}
	
	/**
	 * Constructor - set row and column
	 * @param row - The row of the coordinate
	 * @param col - The column of the coordinate
	 */
	public Coordinate(int row, int col) {
		_row = row;
		_col = col;
	}
	
	/**
	 * Clone a new instance of the object with the same values
	 */
	public Coordinate clone() {
		return new Coordinate(_row, _col);
	}
	
	/**
	 * Set the row for the coordinate
	 * @param row - The row to set
	 */
	public void setRow(int row) {
		_row = row;
	}
	
	/**
	 * Get the row for the coordinate
	 * @return Row
	 */
	public int getRow() {
		return _row;
	}
	
	/**
     * Set the column for the coordinate
     * @param col - The column to set
     */
	public void setCol(int col) {
		_col = col;
	}
	
	/**
     * Get the column for the coordinate
     * @return Column
     */
	public int getCol() {
		return _col;
	}
	
	/**
	 * Check if coordinate is equal in value to another coordinate
	 * @param coord
	 * @return
	 */
	public boolean isEqual(Coordinate coord) {
		if (coord.getRow() == _row && coord.getCol() == _col) {
			return true;
		}
		return false;
	}
}
