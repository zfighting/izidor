package game;

//import java.awt.Graphics2D;

// az egyes játékállapotokat megvalósító osztályok absztrakt ősosztálya
// minden játékállapotban szükséges függvényeket deklarálja
public abstract class GameState
{
	// referencia a játék objektumra
	protected Game game;
	
	// konstruktor
	GameState(Game game)
	{
		this.game = game;
	}
	
	// inputkezelés
	public abstract void handleInput();
	
	// frissítés
	public abstract void update();
	
	// rajzolás
	//public abstract void render(Graphics2D g);
}
