package game;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;

import engine.Vector2d;

public class RectangularRenderableGameObject extends RenderableGameObject
{
	// szélesség
	protected float width;
	// magasság
	protected float height;


	// konstruktor
	public RectangularRenderableGameObject(byte tileID, Vector2d position, Paint paint, float width, float height)
	{
		super(tileID, position, paint);
		this.width = width;
		this.height = height;
	}
	
	// rajzolás
	@Override
	public void render(Graphics2D surface)
	{
		// paint beállítása
		surface.setPaint(paint);
		
		// transzformációs mátrix elmentése
		AffineTransform af = surface.getTransform();
		
		// objektum eltolásának alkalmazása
		surface.translate(position.x, position.y);
		//surface.scale(width, height);
		surface.scale(0.4, 1.9);
		
		// objektum kirajzolása - ez OBJECT SPACE-ben történik!
		surface.fillRect(0, 0, Math.round(width), Math.round(height));
		//surface.fillRect(0, 0, 1, 1);
		
		// elmentett transzformációs mátrix visszaállítása
		surface.setTransform(af);
	}
}
