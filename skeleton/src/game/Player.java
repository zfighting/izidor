package game;

import java.awt.Paint;

import engine.Vector2d;

// játékos osztálya
public class Player extends RectangularRenderableGameObject
{
	// a játékosra ható erők eredője
	protected Vector2d force;
	
	
	// konstruktor
	public Player(byte tileID, Vector2d position, Paint paint, float width, float height)
	{
		super(tileID, position, paint, width, height);
		System.out.println("-> "+this.getClass().getName()+"()");
	}
	
	// játékos elhelyezése valahol a játéktéren belül
	public void moveTo(byte tileID, Vector2d position) throws InvalidTileIDException
	{
		this.setTileID(tileID);
		this.position = position;
	}
	
	public void moveTo(Vector2d position)
	{
		System.out.println("-> "+this.getClass().getName()+".moveTo("+position.toString()+")");
		this.position = position;
	}
	
	// a játékosra ható erő beállítása
	public void setForce(Vector2d force)
	{
		this.force = force;
	}
	
	// a játékosra ható erő lekérdezése
	public Vector2d getForce()
	{
		return force;
	}
	
	// erőhatás alkalmazása a játékosra
	public void applyForce(Vector2d force)
	{
		this.force = Vector2d.add(this.force, force);
	}
}
