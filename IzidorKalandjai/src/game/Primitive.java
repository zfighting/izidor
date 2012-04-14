package game;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

import engine.Vector2d;

// megjeleníthető primitívek ősosztálya
// primitív lehet: háromszög VAGY téglalap (gyerekosztályok)
public abstract class Primitive extends RenderableGameObject
{
	// kirajzolható, fizikai számításokhoz használható, beágyazott objektum
	public Polygon polygon;
	
	
	
	// konstruktor
	public Primitive(byte tileID, Vector2d position, Paint paint, Polygon polygon)
	{
		super(tileID, position, paint);
		this.polygon = polygon;
	}

	// kirajzolás
	@Override
	public void render(Graphics2D surface)
	{
		// paint beállítása
		surface.setPaint(paint);
		
		// transzformációs mátrix elmentése
		AffineTransform af = surface.getTransform();
		
		// objektum eltolásának alkalmazása
		surface.translate(position.x, position.y);
		
		// objektum kirajzolása
		surface.fillPolygon(polygon);
		
		// elmentett transzformációs mátrix visszaállítása
		surface.setTransform(af);
	}
}
