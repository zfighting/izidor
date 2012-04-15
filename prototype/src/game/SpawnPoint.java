package game;

import engine.Vector2d;

// a játékos megszületésének / újraszületésének helyszínét reprezentáló osztály
public class SpawnPoint extends GameObject
{
	// konstruktor
	public SpawnPoint(byte tileID, Vector2d position)
	{
		super(tileID, position);
	}
}
