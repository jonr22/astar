package astar.pathfinder;

import java.util.ArrayList;
import java.util.Iterator;

import astar.info.Coordinate;
import astar.info.Grid;
import astar.info.Node;

public abstract class Astar {
	private ArrayList<Node> _closedNodes = new ArrayList<Node>();
	private ArrayList<Node> _openNodes = new ArrayList<Node>();
	private Node _currentNode = null;
	private long _runtime = -1;
	
	protected Grid _grid = null;
	
	/**
	 * Constructor
	 */
	public Astar() {}
	
	/**
	 * Set the Grid to use
	 * @param grid
	 */
	public void setGrid(Grid grid) {
		_grid = grid;
		_currentNode = new Node(_grid.getStart());
	}
	
	/** 
	 * Get the time the algorithm took to find the shortest path
	 * @return time in ms, -1 if the algorithm hasn't been run yet
	 */
	public long getRuntime() {
		return _runtime;
	}
	
	/**
	 * Find the shortest path for the set Grid
	 * @return list of coordinates for shortest path in order (not including start or end)
	 * @throws Exception
	 */
	public ArrayList<Coordinate> findPath() throws Exception { // TODO: handle case of no possible path
		long starttime = System.currentTimeMillis();
		ArrayList<Coordinate> path;
		
		// grid should not be null
		if (_grid == null) {
			System.out.println("Astar.findPath called without grid being set first");
			return null;
		}
		
		// find the last node in the tree of nodes for the shortest path
		Node small = generateNodePaths();
		
		// if small is null, then no possible path exists
		if (small == null) {
			return null;
		}
		
		// build the shortest path from the node
		path = rebuildPath(small);
		
		// set the runtime
		_runtime = System.currentTimeMillis() - starttime;
		
		return path;
	}
	
	/**
	 * Build list of coordinates from a node through all it's parents starting with root node
	 * @param node
	 * @return list of coordinates
	 */
	private ArrayList<Coordinate> rebuildPath(Node node) { // TODO: rebuild this without recursion
		ArrayList<Coordinate> list;
		
		// if we've reached the root node, return an empty ArrayList
		if (node.getParent() == null) {
			return new ArrayList<Coordinate>();
		}
		
		// recurse to get through the list of nodes from root to child and add them to the list
		list = rebuildPath(node.getParent());
		list.add(node.getCoord());
		return list;
	}
	
	/**
	 * Find the node which has a tree of parent nodes that create the shortest path
	 * @return the child most node of the shortest path
	 * @throws Exception
	 */
	private Node generateNodePaths() throws Exception {
		// while a path to the end node hasn't been found, update the list of open nodes
		while (!updateNodes()) {
			// set the current node to closed
			_closedNodes.add(_currentNode);
			_openNodes.remove(_currentNode);
			
			if (_openNodes.isEmpty()) {
				return null;
			}
			
			// find the new smallest estimated node
			_currentNode = findSmallestNode();
		}
		
		return _currentNode;
	}
	
	/**
	 * Find the node in the _openNodes list with the smallest estimated distance
	 * @return smallest node
	 */
	private Node findSmallestNode() {
		// compare everything to the first node
		Node smallest = _openNodes.get(0);
		
		// iterate through the open nodes and find the one with the smallest estimated distance
		Iterator<Node> nodeIter = _openNodes.iterator();
		while (nodeIter.hasNext()) {
			Node temp = nodeIter.next();
			if (temp.getEstimatedDistance() < smallest.getEstimatedDistance()) {
				smallest = temp;
			}
		}
		return smallest;
	}
	
	/**
	 * Update the list of _openNodes with new nodes and distances
	 * @return  true if path leading to end node is found
	 * @throws Exception
	 */
	private boolean updateNodes() throws Exception {
		// get a list of empty nodes adjacent to the current node
		ArrayList<Node> adjacent = generateAdjacentNodes();
		
		// iterate through the adjacent nodes
		Iterator<Node> adjIter = adjacent.iterator();
		while (adjIter.hasNext()) {
			Node node = adjIter.next();
			boolean isNewNode = false;
			int distanceToNode = Grid.MOVE_DIAGONAL; // assume diagonal, and update to lateral if needed
			
			// if we've found the end node, return true
			if (node.getCoord().isEqual(_grid.getEnd())) {
				// TODO: Don't do this calculation here, this algorithm should stop when end is added to closed nodes
				return true;
			}

			// if the distance is 0, then the node is new as this is always set on existing nodes
			if (node.getDistance() == 0) {
				isNewNode = true;
			}
			
			// if the node has the same row or column as the current node, then it's a lateral movement, otherwise it's diagonal(the default)
			if (node.getCoord().getRow() == _currentNode.getCoord().getRow() || node.getCoord().getCol() == _currentNode.getCoord().getCol()) {
				distanceToNode = Grid.MOVE_LATERAL;
			}
			
			// if the node is new, or it's distance would now be shorter
			if (isNewNode || node.getDistance() > (_currentNode.getDistance() + distanceToNode)) {
				// if the node has the same row or column as the current node, then it's a lateral movement, otherwise it's diagonal
				/*if (node.getCoord().getRow() == _currentNode.getCoord().getRow() || node.getCoord().getCol() == _currentNode.getCoord().getCol()) {
					node.setDistance(_currentNode.getDistance() + Grid.MOVE_ADJACENT);
				} else {
					node.setDistance(_currentNode.getDistance() + Grid.MOVE_DIAGONAL);
				}*/
				node.setDistance(_currentNode.getDistance() + distanceToNode);
				
				//set the nodes estimated distance (call to the method that must be implemented by the sub-class), and it's new parent
				node.setEstimatedDistance(node.getDistance() + estimateDistance(node.getCoord()));
				node.setParent(_currentNode);
				
				// add the node to the open nodes if it's new
				if (isNewNode) {
					_openNodes.add(node);
				}
			}		
		}
		return false;
	}
	
	/**
	 * Find a node with given coordinate in an ArrayList
	 * @param coord 
	 * @param nodes
	 * @return node with the coordinate
	 */
	private Node findNode(Coordinate coord, ArrayList<Node> nodes) {
		Node n;
		Iterator<Node> nodeIter = nodes.iterator();
		
		// iterate through nodes in the list
		while (nodeIter.hasNext()) {
			n = nodeIter.next();
			
			// if a node is equal to the given coordinate, return it
			if (coord.isEqual(n.getCoord())) {
				return n;
			}
		}
		
		// no equivalent nodes found
		return null;
	}
	
	/**
	 * Generate a list of nodes that are adjacent to the current node not already closed
	 * @return list of adjacent nodes
	 * @throws Exception
	 */
	private ArrayList<Node> generateAdjacentNodes() throws Exception {
		ArrayList<Node> adj = new ArrayList<Node>();
		ArrayList<Integer> rows = new ArrayList<Integer>(); // list of adjacent rows
		ArrayList<Integer> cols = new ArrayList<Integer>(); // list of adjacent columns
		Node existingNode = null;
		
		// add current row and col to lists
		rows.add(_currentNode.getCoord().getRow());
		cols.add(_currentNode.getCoord().getCol());
		
		// add rows and cols next to current row and col, if they exist
		if (_currentNode.getCoord().getRow() > 0) { rows.add(_currentNode.getCoord().getRow() - 1); }
		if (_currentNode.getCoord().getRow() < Grid.SIZE - 1) { rows.add(_currentNode.getCoord().getRow() + 1); }
		if (_currentNode.getCoord().getCol() > 0) { cols.add(_currentNode.getCoord().getCol() - 1); }
		if (_currentNode.getCoord().getCol() < Grid.SIZE - 1) { cols.add(_currentNode.getCoord().getCol() + 1); }		
		
		// loop through all (row, col) combinations
		for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
			for (int colIndex = 0; colIndex < cols.size(); colIndex++) {
				// generate the coordinate for the current position
				Coordinate coord = new Coordinate(rows.get(rowIndex), cols.get(colIndex));
				
				// check that the coordinate isn't the _currentNode, that it hasn't already been closed, and that isn't a block
				if (!_currentNode.getCoord().isEqual(coord) 
						&& findNode(coord, _closedNodes) == null 
						&& _grid.getValue(coord) != Grid.NodeType.BLOCK) {
					// if the coordinate is already open in the openNodes, then return that node, otherwise create a new node
					if ((existingNode = findNode(coord, _openNodes)) != null) {
						adj.add(existingNode);
					} else {
						adj.add(new Node(coord));
					}
				}
			}
		}
		return adj;
	}
	
	/**
	 * Method to implement
	 * It should find an estimated from a coordinate to the end node in _grid
	 * @param coord
	 * @return estimated distance
	 */
	protected abstract int estimateDistance(Coordinate coord);
}




















