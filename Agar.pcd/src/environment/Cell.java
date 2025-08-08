package environment;

import game.AiPlayer;
import game.Game;
import game.Player;

public class Cell {
	private Coordinate position;
	private Direction direction;
	private Game game;
	private Player player = null;

	public Cell(Coordinate position, Game g) {
		super();
		this.position = position;
		this.game = g;
	}

	public Coordinate getPosition() { // Retorna a posição
		return position;
	}

	public Direction getDirection() { // Retorna a direção
		return direction;
	}

	public boolean isOccupied() { // Método que verifica se a célula está ocupada
		return player != null;
	}

	public boolean isDead(Player player) { // Método que verifica se o jogador está morto
		int strength_player = player.getCurrentStrength();
		if (isOccupied() && strength_player == 0) {
			return true;
		}
		return false;
	}

	public Player getPlayer() { // Retorna o jogador
		return player;
	}

	public int getidPlayer() { // Retorn o identificador do jogador
		return player.getIdentification();
	}

	private void collision(Player player1, Player player2) { // Procedimento que implementa a colisão
		int strength_player1 = player1.getCurrentStrength();
		int strength_player2 = player2.getCurrentStrength();
		if (strength_player1 > strength_player2) {
			strength_player1 = strength_player1 + strength_player2;
			strength_player2 = 0;
		} else if (strength_player1 < strength_player2) {
			strength_player2 = strength_player1 + strength_player2;
			strength_player1 = 0;
		} else {
			if (Math.random() < 0.5) { // Vitória equiprovável
				strength_player1 = strength_player1 + strength_player2;
				strength_player2 = 0;
			} else {
				strength_player2 = strength_player1 + strength_player2;
				strength_player1 = 0;
			}
		}
		player1.setStrength(strength_player1 > 10 ? 10 : (byte) strength_player1);
		player2.setStrength(strength_player2 > 10 ? 10 : (byte) strength_player2);
	}

	public class PlayerNotifier extends Thread { // Thread autónoma para acordar o jogador parado com o interrupt e
													// sleep
		private Player player;

		public PlayerNotifier(Player player) {
			this.player = player;
		}

		@Override
		public void run() {
			try {
				sleep(Game.MAX_WAITING_TIME_FOR_MOVE);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			player.interrupt();
		}
	}

	public synchronized void tryMovement(Player movingPlayer, Cell playerCell) { // Função que vai sincronizar o
		// movimento dos jogadores
// com as células ocupadas
		if (isOccupied()) {
			if (player.getCurrentStrength() <= 0) {
				try {
					if (movingPlayer instanceof AiPlayer) {
						new PlayerNotifier(movingPlayer).start();
						wait();
					}
				} catch (InterruptedException e) {
					System.out.println("Waking up player:" + movingPlayer);
				}
			} else {
				collision(movingPlayer, player);
			}
		} else {
			playerCell.exitPlayer();
			player = movingPlayer;
			game.notifyChange();

		}

	}

	// Should not be used like this in the initial state: cell might be occupied,
	// must coordinate this operation
	public synchronized void setPlayer(Player player) { // Coordenação da colocação inicial dos jogadores entre as células
		int id_player_entering = player.getIdentification();
		while (isOccupied() || isDead(player)) {
			System.out.println(
					"The player with id " + id_player_entering + " cannot enter the game because the player with id "
							+ getidPlayer() + " is already occupying the position " + getPosition());
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.player = player;
	}

	public synchronized void exitPlayer() {
		player = null;
		notifyAll(); // acorda todas as threads que estejam em wait neste recurso partilhado
	}

}
