package astar.info;

/**
 * Node is a one way traversable tree (only up) that is used to store paths
 * between coordinates.
 * 
 * @author Jonathan Reimels
 *
 */
public class Node {
	private Coordinate _coord = null;
	private Node _parent = null;
	private int _distance = 0;
	private int _estimatedDistance = 0;
	
	/**
	 * Default Constructor
	 */
	public Node() {}
	
	/**
	 * Constructor - set coordinate
	 * @param coord - The coordinate to set for the Node
	 */
	public Node(Coordinate coord) {
		_coord = coord.clone();
	}
	
	/**
	 * Get coordinate of the Node
	 * @return Coordinate of Node
	 */
	public Coordinate getCoord() {
		return _coord;
	}
	
	/**
	 * Set coordinate of Node
	 * @param coord - The coordinate to set
	 */
	public void setCoord(Coordinate coord) {
		_coord = coord.clone();
	}
	
	/**
	 * Get parent node
	 * @return Parent Node
	 */
	public Node getParent() {
		return _parent;
	}
	
	/**
	 * Set parent of the node
	 * @param parent - Parent Node to set
	 */
	public void setParent(Node parent) {
		_parent = parent;
	}
	
	/**
	 * Get the distance to the Node
	 * @return Distance
	 */
	public int getDistance() {
		return _distance;
	}
	
	/**
	 * Set the distance to the Node
	 * @param distance - The distance to set
	 */
	public void setDistance(int distance) {
		_distance = distance;
	}
	
	/**
	 * Get the estimated distance for the node
	 * @return Estimated Distance
	 */
	public int getEstimatedDistance() {
		return _estimatedDistance;
	}
	
	/**
	 * Set the estimated distance for the node
	 * @param distance - The estimated distance to set
	 */
	public void setEstimatedDistance(int distance) {
		_estimatedDistance = distance;
	}	
	
	
}
