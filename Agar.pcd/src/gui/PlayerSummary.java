package gui;

import java.io.Serializable; // Classe enviada pela rede logo é Serializable

public class PlayerSummary implements Serializable { // Data Class para ter informação e ser enviada pela rede para o ClientBoardComponent

	private int id;
	private byte currentStrength;
	private boolean isHumanPlayer;

	public PlayerSummary(int id, byte currentStrength, boolean isHumanPlayer) {
		this.id = id;
		this.currentStrength = currentStrength;
		this.isHumanPlayer = isHumanPlayer;
	}

	public byte getCurrentStrength() {
		return currentStrength;
	}

	public int getIdentification() {
		return id;
	}

	public boolean isHumanPlayer() {
		return isHumanPlayer;
	}

}
