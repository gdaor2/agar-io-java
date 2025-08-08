package environment;

public enum Direction {
	UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0); // Definição das direções possíveis

	private Coordinate vector;

	Direction(int x, int y) {
		vector = new Coordinate(x, y);
	}

	public Coordinate getVector() { // Criação de um vetor de direções
		return vector;
	}
}
