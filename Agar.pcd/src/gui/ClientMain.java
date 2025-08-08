package gui;

import java.io.IOException;

import java.util.Observable;
import java.util.Observer;
import game.Game;
import game.HumanPlayer;
import server.HumanClient;

import javax.swing.JFrame;

public class ClientMain implements Observer { // Classe main que executa o cliente remoto
	private JFrame frame = new JFrame("pcd.io");
	private ClientBoardComponent boardGui;

	public ClientMain() {
		super();

		buildGui();

	}

	private void buildGui() {
		boardGui = new ClientBoardComponent();
		frame.add(boardGui);

		frame.setSize(800, 800);
		frame.setLocation(0, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init() {
		frame.setVisible(true);
		try {
			new HumanClient(boardGui).runClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}

	public ClientBoardComponent getGui() {
		return boardGui;
	}

	public static void main(String[] args) {
		ClientMain gui = new ClientMain();
		gui.init();

	}

}
