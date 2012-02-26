package game;

import engine.*;

// játékbeli ojjektumok ősosztálya
public abstract class GameObject
{
	// melyik tile-on belül van ez az ojjektum
	protected byte tileID;
	// az ojjektum koordinátái tile koordinátarendszerben
	protected Vector2d position;
	
	
	// konstruktor
	public GameObject(byte tileID, Vector2d position)
	{
		this.tileID = tileID;
		this.position = position;
	}
}
