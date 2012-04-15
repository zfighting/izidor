package game;

import java.awt.Paint;
import java.awt.Polygon;

import engine.Vector2d;

// háromszög osztály
// a pályák egyik felépítő eleme
public class Triangle extends Primitive
{
	public Triangle(byte tileID, Vector2d position, Paint paint, Polygon polygon)
	{
		super(tileID, position, paint, polygon);
	}
}
