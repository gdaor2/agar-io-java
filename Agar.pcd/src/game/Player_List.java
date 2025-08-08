package game;

import java.util.ArrayList;
import java.util.List;

public class Player_List { // Classe que guarda numa lista todos os jogadores que entram no jogo
	private List<Player> player_list = new ArrayList<>();

	public synchronized void addPlayer(Player player) {
		player_list.add(player);
	}

	public synchronized void interruptAllPlayers() { // Método que interrompe o movimento de todos os jogadores. Útil para o CountDownLatch
		for (Player player : player_list) {
			player.interrupt();
		}
	}
}
