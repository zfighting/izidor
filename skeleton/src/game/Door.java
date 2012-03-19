package game;

import java.awt.Graphics2D;
import java.awt.Paint;

import engine.Vector2d;

// ajtó osztály
public class Door extends RectangularRenderableGameObject
{
	// konstruktor
	public Door(byte tileID, Vector2d position, Paint paint, float width, float height)
	{
		super(tileID, position, paint, width, height);
		System.out.println("-> "+this.getClass().getName()+"()");
	}
}
