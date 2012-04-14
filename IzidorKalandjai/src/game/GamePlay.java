package game;

import java.awt.Graphics2D;


// a főmenü játékállapot osztálya
public class GamePlay extends GameState
{
	// konstruktor
	GamePlay(Game game)
	{
		super(game);
	}

	@Override
	public void handleInput()
	{
		;
	}

	@Override
	public void update()
	{
		;
	}

	@Override
	public void render(Graphics2D g)
	{
		// pálya kirajzolása
		game.getStage().render(g);
	}

}
