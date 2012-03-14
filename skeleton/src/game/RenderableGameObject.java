package game;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;

import engine.Vector2d;

// renderelhető játékelemek ősosztálya
public abstract class RenderableGameObject extends GameObject implements Renderable
{
	// a kirajzolás során felhasznált, Paint interface-t megvalósító objektum
	// így lehet színezni (Color), színátmeneteket használni (GradientColor) és textúrázni (TexturePaint) is
	protected Paint paint;
	
	
	// konstruktor
	public RenderableGameObject(byte tileID, Vector2d position, Paint paint)
	{
		super(tileID, position);
		this.paint = paint;
	}
	
	// paint adattag lekérdezése
	public Paint getPaint()
	{
		return paint;
	}
	
	// paint adattag beállítása
	public void setPaint(Paint paint)
	{
		this.paint = paint;
	}
}
