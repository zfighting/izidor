package game;

import java.awt.Paint;
import engine.Vector2d;

// játékos osztálya
class Player extends RectangularRenderableGameObject
{
	// a játékosra ható erők eredője
	protected Vector2d force;
	
	// a játékos irányításához használt gombok
	public int controlUp, controlLeft, controlRight;
	
	// a játékos ugorhat-e éppen - amint egy játékos a levegőbe emelkedik, nem ugrohat, amíg újra talajon nem áll
	public boolean canJump = true;
	
	// a játékos objektumok vízszintes irányú mozgásának sebessége
	public static float speed = 1.8f;
	
	
	// konstruktor
	public Player(byte tileID, Vector2d position, Paint paint, float width, float height, int cUp, int cLeft, int cRight)
	{
		super(tileID, position, paint, width, height);
		
		// erővektor létrehozása
		force = new Vector2d();
		
		// irányításhoz használt gombok elmentése
		controlUp    = cUp;
		controlLeft  = cLeft;
		controlRight = cRight;
	}
	
	// játékos elhelyezése valahol a játéktéren belül
	public void moveTo(byte tileID, Vector2d position) throws InvalidTileIDException
	{
		this.setTileID(tileID);
		this.position.x = position.x;
		this.position.y = position.y;
	}
	
	public void moveTo(Vector2d position)
	{
		this.position.x = position.x;
		this.position.y = position.y;
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
