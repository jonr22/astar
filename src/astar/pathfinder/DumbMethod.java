package astar.pathfinder;

import astar.info.Coordinate;

/**
 * Simplest heuristic to use with A*, essentially makes this algorithm 
 * equivalent to Dijkstra's
 * 
 * @author Jonathan Reimels
 *
 */
public class DumbMethod extends Astar{
	
    @Override
	protected int estimateDistance(Coordinate coord) {
		return 1;
	}
	
    @Override
	public String toString() {
		return "Dumb Method";
	}

}
