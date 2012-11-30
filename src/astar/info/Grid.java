package astar.info;

public class Grid {
	private Coordinate _startCoord = null;
	private Coordinate _endCoord = null;
	
	public enum NodeType
	{
		START, END, BLOCK, EMPTY, PATH;
	}
	
	public static final int SIZE = 20;
	public static final int MOVE_LATERAL = 2;
	public static final int MOVE_DIAGONAL = 3;
	
	private NodeType[][] _gridPath;
	
	public Grid() {
		_gridPath = new NodeType[SIZE][SIZE];
		clear();
	}
	
	public Coordinate getStart() {
		return _startCoord;
	}
	
	public Coordinate getEnd() {
		return _endCoord;
	}
	
	public NodeType getValue(Coordinate coord) throws Exception {
		if (coord.getRow() < 0 || coord.getRow() >= SIZE || coord.getCol() < 0 || coord.getCol() >= SIZE)
			throw new Exception("Invalid row or col number");
		
		return _gridPath[coord.getRow()][coord.getCol()];
	}
	
	public void setValue(Coordinate coord, NodeType value) throws Exception {
		if (coord.getRow() < 0 || coord.getRow() >= SIZE || coord.getCol() < 0 || coord.getCol() >= SIZE)
			throw new Exception("Invalid row or col number");
		
		if (value == NodeType.START) {
			if (_startCoord != null) {
				_gridPath[_startCoord.getRow()][_startCoord.getCol()] = NodeType.EMPTY;
			}
			_startCoord = coord.clone();
		}
		
		if (value == NodeType.END) {
			if (_endCoord != null) {
				_gridPath[_endCoord.getRow()][_endCoord.getCol()] = NodeType.EMPTY;
			}
			_endCoord = coord.clone();
		}
		
		_gridPath[coord.getRow()][coord.getCol()] = value;
	}
	
	public void clear() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				_gridPath[i][j] = NodeType.EMPTY;
			}
		}
	}
	
	public void clearPath() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (_gridPath[i][j] == NodeType.PATH) {
					_gridPath[i][j] = NodeType.EMPTY;
				}
			}
		}
	}
}
