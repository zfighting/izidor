package game;

import java.awt.Graphics2D;
import engine.Vector2d;

// kulcsok osztálya
public class Key extends RenderableGameObject
{
	// konstruktor
	public Key(byte tileID, Vector2d position)
	{
		super(tileID, position);
	}

	// kirajzolás
	@Override
	public void render(Graphics2D surface)
	{
		// TODO Auto-generated method stub
	}
}
