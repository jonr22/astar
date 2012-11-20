package astar.pathfinder;

import astar.info.Coordinate;
import astar.info.Grid;

public class ManhattanMethod extends Astar{
	
	protected int estimateDistance(Coordinate coord) {
		return (Math.abs(coord.getRow() - _grid.getEnd().getRow()) + Math.abs(coord.getCol() - _grid.getEnd().getCol())) * Grid.MOVE_LATERAL;
	}
	
	public String toString() {
		return "Manhattan Method";
	}
}
