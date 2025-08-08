package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

public class AiPlayer extends Player { // thread que cria e faz mover o jogador automático

	public AiPlayer(int id, Game game, byte strength) {
		super(id, game, strength);
	}

	public void run() {

		try {
			while (!isInterrupted() && getCurrentStrength() > 0 && getCurrentStrength() < 10) {
				Direction[] dirs = Direction.values(); // Direções possíveis
				Coordinate randomVector = dirs[(int) (Math.random() * dirs.length)].getVector(); // Definição da posição
																									// // aleatória
				game.movePlayer(this, randomVector);
				if (getCurrentStrength() >= 10) { // Se a força do jogador for igual ou superior a 10. O contador desce
													// um valor. Útil para o fim do jogo
					game.getCountdown().countdown();
					break;
				}
				sleep(Game.REFRESH_INTERVAL * originalStrength); // Resolve o problema da deslocação por ciclos
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean isHumanPlayer() { // Método para verificar se é jogador humano ou não
		return false;
	}

}
