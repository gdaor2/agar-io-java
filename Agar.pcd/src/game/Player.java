package game;

import environment.Cell;
import gui.PlayerSummary;

/**
 * Represents a player.
 * 
 * @author luismota
 *
 */
public abstract class Player extends Thread { // Todos os jogadores passam a ser threads

	protected Game game;

	private int id; // identificador único do jogador

	private byte currentStrength; // energia que vai mudando
	protected byte originalStrength; // energia com que o jogador começa

	// TODO: get player position from data in game
	public Cell getCurrentCell() {
		return game.getPlayerCell(this);
	}

	public Player(int id, Game game, byte strength) {
		super();
		this.id = id;
		this.game = game;
		currentStrength = strength;
		originalStrength = strength;
	}

	public abstract boolean isHumanPlayer();

	@Override
	public String toString() {
		return "Player [id=" + id + ", currentStrength=" + currentStrength + ", getCurrentCell()=" + getCurrentCell()
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public byte getCurrentStrength() {
		return currentStrength;
	}

	public synchronized void setStrength(byte strength) {
		this.currentStrength = strength;
	}

	public int getIdentification() {
		return id;
	}

	public PlayerSummary getSummary() { // Retorna as informações do jogador que o cliente pode ver
		return new PlayerSummary(id, currentStrength, isHumanPlayer());
	}
}
