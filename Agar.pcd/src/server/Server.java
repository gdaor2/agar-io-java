package server;

import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import game.Game;
import game.Player;
import gui.GameGuiMain;
import gui.PlayerSummary;

public class Server extends Thread { // Implementação do servidor

	public static final int PORT = 8789;
	private BufferedReader in;
	private PrintWriter out;
	private GameGuiMain game;
	private Socket socket;
	private List<ServerHandler> handlers = new ArrayList<>();

	public Server(GameGuiMain game) {
		this.game = game;
		new StateGame().start();
	}

	public void startServing() throws IOException {
		ServerSocket ss = new ServerSocket(PORT);
		try {
			while (true) {
				System.out.println("Server initialised...");
				socket = ss.accept();

				// Handler
				ServerHandler shandler = new ServerHandler(socket, game); // Criação do handler
				System.out.println("Servidor iniciado...");
				handlers.add(shandler); // adiciona os handlers a uma lista
				shandler.start();

			}
		} finally {
			if (ss != null)
				ss.close();
		}
	}

	public class StateGame extends Thread { // Thread que atualiza a board no servidor

		@Override
		public void run() {
			while (true) {
				PlayerSummary[][] players = game.getGame().getPlayerSummaries();
				for (ServerHandler shandler : handlers) {
					shandler.sendMessages(players);
				}
				try {
					sleep(Game.REFRESH_INTERVAL);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public static void main(String[] args) {
		try {
			GameGuiMain game = new GameGuiMain();
			game.init();
			new Server(game).startServing();

		} catch (IOException e) {

		}
	}

}
