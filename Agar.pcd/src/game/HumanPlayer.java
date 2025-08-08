package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

public class HumanPlayer extends Player {
	private Coordinate c;
	private Direction d = null;
	private static final byte STRENGTH = 5; // Inicialização da força de um jogador humano a 5

	public HumanPlayer(int id, Game game) {
		super(id, game, STRENGTH);
	}

	public void run() {

		try {
			while (!isInterrupted() && getCurrentStrength() > 0 && getCurrentStrength() < 10) {
				if (d != null) { // Com esta condição, os jogadores humanos ignoram o movimento se estiverem a mover-se para uma célula ocupada
					Coordinate vector = d.getVector();
					game.movePlayer(this, vector);
				}
				if (getCurrentStrength() >= 10) {
					game.getCountdown().countdown();
					break;
				}
				sleep(Game.REFRESH_INTERVAL* originalStrength); // * originalStrength
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setDirection(Direction d) { // Define uma direção para o jogador humano
		this.d = d;
	}

	@Override
	public boolean isHumanPlayer() {
		return true;
	}

}
