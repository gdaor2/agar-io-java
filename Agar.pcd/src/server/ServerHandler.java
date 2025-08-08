package server;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import environment.Direction;
import game.Game;
import game.HumanPlayer;
import gui.GameGuiMain;

public class ServerHandler extends Thread { // Implementação do Handler
	private BufferedReader in;
	private ObjectOutputStream out;
	private Socket socket;
	private GameGuiMain gameGui;
	private Game game;

	public ServerHandler(Socket socket, GameGuiMain gameGui) {
		this.socket = socket;
		this.gameGui = gameGui;
		System.out.println("Servidor iniciado...");
	}

	@Override
	public void run() {
		try {
			System.out.println("Handler connected.....");
			// Ligações
			try {
				doConnections(socket);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Cria Handler + Thread
			game = gameGui.getGame();
			HumanPlayer human = new HumanPlayer((int) Math.random() * 1000000, game);
			game.addPlayerToGame(human);
			System.out.println(" Player adicionado ");
			human.start();

			while (true) { // Faz a leitura das direções fornecidas pelo teclado pelo jogador humano
				String direction_str = in.readLine();
				switch (direction_str) {
				case "UP":
					human.setDirection(Direction.UP);
					break;
				case "DOWN":
					human.setDirection(Direction.DOWN);
					break;
				case "LEFT":
					human.setDirection(Direction.LEFT);
					break;
				case "RIGHT":
					human.setDirection(Direction.RIGHT);
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}

	void doConnections(Socket socket) throws IOException {
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		System.out.println(" Connections done ");
	}

	void sendMessages(Object o) {
		try {
			out.writeObject(o);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
