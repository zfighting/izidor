package game;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

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
	public Primitive(byte tileID, Vector2d position, Paint paint, PrimitiveType type, Polygon polygon)
	{
		super(tileID, position, paint);
		this.type = type;
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

	// típus lekérdezése
	public PrimitiveType getType()
	{
		return type;
	}
}
