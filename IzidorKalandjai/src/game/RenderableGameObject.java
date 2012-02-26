package game;

import engine.Vector2d;

// renderelhető játékelemek ősosztálya
public abstract class RenderableGameObject extends GameObject implements Renderable
{
	// LECSERÉLENDŐ / MÓDOSÍTANDÓ ...
	// textúra azonosító
	protected int textureID;
	
	
	// konstruktor
	public RenderableGameObject(byte tileID, Vector2d position)
	{
		super(tileID, position);
	}
}
