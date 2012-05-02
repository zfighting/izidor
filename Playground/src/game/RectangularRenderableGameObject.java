package game;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import engine.Vector2d;

public class RectangularRenderableGameObject extends RenderableGameObject
{
	// tényleges szélesség és magasság - ekkora méretben jelenik meg a képernyőn 
	protected float width;
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

		// skálázás alkalmazása és renderelés
		if( paint.getClass().equals(TexturePaint.class) )
		{
			Rectangle2D r = ((TexturePaint)paint).getAnchorRect();
			surface.scale(width / r.getWidth(), height / r.getHeight());
			surface.fillRect(0, 0, (int)Math.round(r.getWidth()), (int)Math.round(r.getHeight()));
		}
		else
		{
			surface.fillRect(0, 0, Math.round(width), Math.round(height));
		}
		
		
		// elmentett transzformációs mátrix visszaállítása
		surface.setTransform(af);
	}
	
	// méretek lekérdezése
	public float getWidth()
	{
		return width;
	}
	
	public float getHeight()
	{
		return height;
	}
	
	// méretek beállítása
	public void setWidth(float width)
	{
		this.width = width;
	}
	
	public void setHeight(float height)
	{
		this.height = height;
	}
}
