package game;

import java.awt.Paint;

import engine.Vector2d;

// játékos osztálya
public class Player extends RectangularRenderableGameObject
{
	// konstruktor
	public Player(byte tileID, Vector2d position, Paint paint, float width, float height)
	{
		super(tileID, position, paint, width, height);
	}
}
