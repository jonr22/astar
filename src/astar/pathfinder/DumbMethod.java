package astar.pathfinder;

import astar.info.Coordinate;
import astar.info.Grid;

public class DumbMethod extends Astar{
	
	protected int estimateDistance(Coordinate coord) {
		return 1;
	}
	
	public String toString() {
		return "Dumb Method";
	}

}
