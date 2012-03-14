package game;

import java.awt.Graphics2D;
import java.awt.Paint;

import engine.Vector2d;

// kulcsok osztálya
public class Key extends RectangularRenderableGameObject
{
	// konstruktor
	public Key(byte tileID, Vector2d position, Paint paint, float width, float height)
	{
		super(tileID, position, paint, width, height);
	}
}
