package astar.info;

/**
 * Representation of the Grid to use for the Astar algorithm
 * and to display with the GUI
 *
 * @author Jonathan Reimels
 *
 */
public class Grid {
    private Coordinate _startCoord = null;
    private Coordinate _endCoord = null;
    private NodeType[][] _gridPath;

    /**
     * Types of nodes within grid
     */
    public enum NodeType
    {
        START, END, BLOCK, EMPTY, PATH;
    }

    /**
     * length/height of grid
     */
    public static final int SIZE = 20;

    /**
     * cost to move up/down or left/right
     */
    public static final int MOVE_LATERAL = 2;

    /**
     * cost to move diagonally
     */
    public static final int MOVE_DIAGONAL = 3;

    /**
     * Constructor
     */
    public Grid() {
        _gridPath = new NodeType[SIZE][SIZE];
        clear();
    }

    /**
     * Get the start coordinate
     * @return start coordinate
     */
    public Coordinate getStart() {
        return _startCoord;
    }

    /**
     * Get the end coordinate
     * @return end coordinate
     */
    public Coordinate getEnd() {
        return _endCoord;
    }

    /**
     * Get the value of a coordinate
     * @param coord - The coordinate to get
     * @return NodeType of coordinate
     * @throws Exception
     */
    public NodeType getValue(Coordinate coord) throws Exception {
        // check that coordinate is within the grid
        if (coord.getRow() < 0 || coord.getRow() >= SIZE || coord.getCol() < 0 || coord.getCol() >= SIZE)
            throw new Exception("Invalid row or col number");

        // return the value at the coordinate
        return _gridPath[coord.getRow()][coord.getCol()];
    }

    /**
     * Set the value at a coordinate
     * @param coord - The coordinate to set
     * @param value - The NodeType to set
     * @throws Exception
     */
    public void setValue(Coordinate coord, NodeType value) throws Exception {
        // check that coordinate is within the grid
        if (coord.getRow() < 0 || coord.getRow() >= SIZE || coord.getCol() < 0 || coord.getCol() >= SIZE)
            throw new Exception("Invalid row or col number");

        // if start node, remove any previous start nodes and set the start node coordinate
        if (value == NodeType.START) {
            if (_startCoord != null) {
                _gridPath[_startCoord.getRow()][_startCoord.getCol()] = NodeType.EMPTY;
            }
            _startCoord = coord.clone();
        }

        // if end node, remove any previous end nodes and set the end node coordinate
        if (value == NodeType.END) {
            if (_endCoord != null) {
                _gridPath[_endCoord.getRow()][_endCoord.getCol()] = NodeType.EMPTY;
            }
            _endCoord = coord.clone();
        }

        // TODO: check current value before overwriting it
        // set value at coordinate
        _gridPath[coord.getRow()][coord.getCol()] = value;
    }

    /**
     * Clear all values in grid
     */
    public void clear() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                _gridPath[i][j] = NodeType.EMPTY;
            }
        }
    }

    /**
     * Clear the currently set path
     */
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
