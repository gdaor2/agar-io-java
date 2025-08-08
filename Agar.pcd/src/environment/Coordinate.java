package environment;

public class Coordinate {
	public final int x; 
	public final int y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	@Override
	public boolean equals(Object obj) {
		Coordinate other = (Coordinate) obj;
		return other.x==x && other.y == y;
	}
	
	public double distanceTo(Coordinate other) { // distância entre um ponto (x,y)
		double dx = y - other.y;
		double dy = x - other.x;
		return Math.sqrt(dx * dx + dy * dy);
	}

	public Coordinate translate(Coordinate vector) { //Função que traduz um vetor de direção num ponto (x,y)
		return new Coordinate(x+vector.x, y+vector.y);
	}
}
