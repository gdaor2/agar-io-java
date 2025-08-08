package server;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import environment.Direction;
import game.Game;
import gui.BoardJComponent;
import gui.ClientBoardComponent;
import gui.PlayerSummary;

public class HumanClient { // Classe respetiva ao cliente remoto

	private ObjectInputStream in;
	private PrintWriter out;
	private Socket socket;
	private int PORT = 8789;
	private String address = "localhost";
	private ClientBoardComponent client_board;

	public HumanClient(ClientBoardComponent client_board) {
		this.client_board = client_board;
	}

	public void runClient() throws UnknownHostException, IOException {

		socket = new Socket(address, PORT);

		// Ligações
		in = new ObjectInputStream(socket.getInputStream()); // canais de objetos no sentido servidor-cliente
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true); // canais de
																											// texto no
																											// sentido
																											// cliente-servidor
		new UpdateState().start();
		try {
			while (true) {
//				out.println("DOWN");
//				Thread.sleep(1000);
//				out.println("LEFT");
//				Thread.sleep(1000);
//				out.println("UP");
//				Thread.sleep(1000);
//				out.println("RIGHT");
//				Thread.sleep(1000);
				out.println(client_board.getLastPressedDirection());
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
		}

	}

	public class UpdateState extends Thread { // Thread que atualiza o estado do tabuleiro para o cliente

		@Override
		public void run() {
			while (true) {
				try {
					PlayerSummary[][] players = (PlayerSummary[][]) in.readObject();
					System.out.println("Players");
					client_board.updateBoard(players);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}
}
