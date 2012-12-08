package astar.pathfinder;

/**
 * Factory to get instance of Astar algorithm
 *
 * @author Jonathan Reimels
 *
 */
public class AstarFactory {

    /**
     * Current implementations
     */
    public static enum Implementation {
        ManhattanMethod, DiagonalMethod, EuclideanMethod, DijkstraMethod, MyMethod;
    }

    /**
     * Get default implementation
     * @return Astar instance
     */
    public static Astar getDefault() {
        return new ManhattanMethod();
    }

    /**
     * Get an instance of Astar
     * @param impl - implemenation (algorithm/heuristic) to instantiate
     * @return Astar instance
     */
    public static Astar getAstar(Implementation impl) {
        switch(impl) {
            case ManhattanMethod:
                return new ManhattanMethod();
            case DiagonalMethod:
            	return new DiagonalMethod();
            case EuclideanMethod:
                return new EuclideanMethod();
            case DijkstraMethod:
                return new DijkstraMethod();
            case MyMethod:
            	return new MyMethod();
            default:
                return null;
        }
    }
}
