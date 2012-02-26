package game;

import java.awt.Graphics2D;
import java.awt.Polygon;

import engine.Vector2d;

// megjeleníthető primitívek osztálya
// primitív lehet: háromszög VAGY téglalap
public class Primitive extends RenderableGameObject
{
	// primitív típusa
	protected PrimitiveType type;
	// kirajzolható, fizikai számításokhoz használható, beágyazott objektum
	protected Polygon polygon;
	
	// konstruktor
	public Primitive(byte tileID, Vector2d position, PrimitiveType type, Polygon polygon)
	{
		super(tileID, position);
		this.type = type;
		this.polygon = polygon;
	}

	// kirajzolás
	@Override
	public void render(Graphics2D surface)
	{
		// TODO Auto-generated method stub
	}

	// típus lekérdezése
	public PrimitiveType getType()
	{
		return type;
	}
}
