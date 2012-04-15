package game;

import engine.Vector2d;

//helper osztály, azt tárolja, hogy a  játékos mozgatása során volt-e ütközés, és hogy hová került
public class CollisionDetectionResult
{
	public Vector2d newPosition;
	public boolean collisionX;
	public boolean collisionY;
	
	public CollisionDetectionResult(Vector2d newPosition_in, boolean collisionX_in, boolean collisionY_in)
	{
		newPosition = newPosition_in;
		collisionX = collisionX_in;
		collisionY = collisionY_in;
	}
}
