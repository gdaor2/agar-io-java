package game;

import java.util.Observable;
import environment.Cell;
import environment.Coordinate;
import environment.Direction;
import gui.PlayerSummary;

public class Game extends Observable {

	public static final int DIMY = 30;
	public static final int DIMX = 30;
	private static final int NUM_PLAYERS = 90;
	private static final int NUM_FINISHED_PLAYERS_TO_END_GAME = 3;
	public static final long REFRESH_INTERVAL = 400;
	public static final double MAX_INITIAL_STRENGTH = 3;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final long INITIAL_WAITING_TIME = 10000;
	private CountDownLatch countdown = new CountDownLatch(NUM_FINISHED_PLAYERS_TO_END_GAME);
	private Player_List player_list = new Player_List();

	protected Cell[][] board;

	public Game() { // Construtor que cria e limita o tabuleiro para o jogo
		board = new Cell[Game.DIMX][Game.DIMY];

		for (int x = 0; x < Game.DIMX; x++)
			for (int y = 0; y < Game.DIMY; y++)
				board[x][y] = new Cell(new Coordinate(x, y), this);

	}

	public synchronized Cell getPlayerCell(Player player) { // Procedimento sincronizado que permite obter a posição atual do jogador
		for (int x = 0; x < Game.DIMX; x++)
			for (int y = 0; y < Game.DIMY; y++)
				if (board[x][y].isOccupied())
					if (board[x][y].getPlayer().equals(player))
						return board[x][y];
		return null;
	}

	/**
	 * @param player
	 */
	public void addPlayerToGame(Player player) { // Método que adiciona um jogador a uma posição "aleatória" do tabuleiro
		Cell initialPos = getRandomCell();
		initialPos.setPlayer(player);
		player_list.addPlayer(player);
		notifyChange();
	}

	public void movePlayer(Player player, Coordinate vector) { // Método que executa o movimento dos jogadores automáticos
		Cell player_cell = player.getCurrentCell();
		if (player_cell == null)
			return;
		Coordinate new_position = player_cell.getPosition().translate(vector);
		if (new_position.x >= 0 && new_position.x < Game.DIMX)
			if (new_position.y >= 0 && new_position.y < Game.DIMY)
				board[new_position.x][new_position.y].tryMovement(player, player_cell);
	}

	public class PlayerPlacer extends Thread { // Thread para colocar em jogo e pôr a correr a sincronização das células
												// é feita no SetPlayer na classe Cell
		Player player;

		public PlayerPlacer(Player player) {
			super();
			this.player = player;
		}

		@Override
		public void run() {
			addPlayerToGame(player);
			player.start();
		}

	}

	public class EndGame extends Thread { // Thread que utiliza a estrutura de sincronização temporal CountDownLatch para terminar o jogo quando 3 jogadores chegam à pontuação máxima
		@Override
		public void run() {
			countdown.await();
			player_list.interruptAllPlayers();
		}
	}

	public CountDownLatch getCountdown() { // Retorna o valor atual do contador
		return countdown;
	}

	public void initializePlayers() { // Colocar os jogadores numa posição aleatória
		for (int i = 0; i < NUM_PLAYERS; i++) {
			byte strength = (byte) ((Math.random() * (Game.MAX_INITIAL_STRENGTH - 1)) + 1);
			Player player = new AiPlayer(i, this, strength);
			new PlayerPlacer(player).start();
		}
		new EndGame().start();
	}

	public Cell getCell(Coordinate at) { // Retorna uma posição
		return board[at.x][at.y];
	}

	public PlayerSummary[][] getPlayerSummaries() { // Retorna uma matriz de dados importante para passar pela rede para o ClientBoardComponent. Representa os dados que o cliente pode ter acesso no jogo
		PlayerSummary[][] players = new PlayerSummary[Game.DIMX][Game.DIMY];
		for (int x = 0; x < Game.DIMX; x++)
			for (int y = 0; y < Game.DIMY; y++)
				if (board[x][y].isOccupied())
					players[x][y] = board[x][y].getPlayer().getSummary();
		return players;
	}

	/**
	 * Updates GUI. Should be called anytime the game state changes
	 */
	public void notifyChange() {
		setChanged();
		notifyObservers();
	}

	public Cell getRandomCell() { // Retorna uma posição "aleatória"
		Cell newCell = getCell(new Coordinate((int) (Math.random() * Game.DIMX), (int) (Math.random() * Game.DIMY)));
		return newCell;
	}
}
