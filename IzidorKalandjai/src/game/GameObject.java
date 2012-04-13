package game;

import engine.*;

// játékbeli onjektumok ősosztálya
public abstract class GameObject
{
	// melyik tile-on belül van ez az ojjektum
	protected byte tileID;
	// az ojjektum koordinátái tile koordinátarendszerben
	// publikus adattag, hogy könnyű legyen vele dolgozni
	public Vector2d position;
	
	
	// konstruktor
	public GameObject(byte tileID, Vector2d position)
	{
		this.tileID = tileID;
		this.position = position;
	}
	
	// tileID lekérdezése
	public byte getTileID()
	{
		return tileID;
	}
	
	// tileID módosítása
	public void setTileID(byte tileID) throws InvalidTileIDException
	{
		// a tileID csak akkor érvényes, ha nagyobb, mint 0 (a 0-s ID-jű tile jelenti az ÜRES mezőt!)
		if( tileID > 0 )
		{
			this.tileID = tileID;
		}
		else
		{
			throw new InvalidTileIDException(tileID);
		}
	}
}
