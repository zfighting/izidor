package game;

import java.awt.Paint;
import java.awt.Polygon;

import engine.Vector2d;

// téglalap osztály
// a pályák egyik felépítő eleme
public class Rectangle extends Primitive
{
	public Rectangle(byte tileID, Vector2d position, Paint paint, Polygon polygon)
	{
		super(tileID, position, paint, polygon);
	}
}
