package astar.pathfinder;

public class AstarFactory {
	
	public static enum Implementation {
		ManhattanMethod, DumbMethod;
	}
	
	public static Astar getDefault() {
		return new ManhattanMethod();
	}
	
	public static Astar getAstar(Implementation impl) {
		switch(impl) {
			case ManhattanMethod:
				return new ManhattanMethod();
			case DumbMethod:
				return new DumbMethod();
			default:
				return null;
		}
	}
}
