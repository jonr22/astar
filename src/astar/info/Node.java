package astar.info;

public class Node {
	private Coordinate _coord = null;
	private Node _parent = null;
	private int _distance = 0;
	private int _estimatedDistance = 0;
	
	public Node() {}
	
	public Node(Coordinate coord) {
		_coord = coord;
	}
	
	public Coordinate getCoord() {
		return _coord;
	}
	
	public void setCoord(Coordinate coord) {
		_coord = coord;
	}
	
	public Node getParent() {
		return _parent;
	}
	
	public void setParent(Node parent) {
		_parent = parent;
	}
	
	public int getDistance() {
		return _distance;
	}
	
	public void setDistance(int distance) {
		_distance = distance;
	}
	
	public int getEstimatedDistance() {
		return _estimatedDistance;
	}
	
	public void setEstimatedDistance(int distance) {
		_estimatedDistance = distance;
	}	
	
	
}
