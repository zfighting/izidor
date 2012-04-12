package game;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

import engine.Vector2d;

// pályarészek (csempék) osztálya
// ilyenekből épül fel a pálya (stage), ezeken belül szabadon mozoghat
// a játékos, tilitoli módban ezek cserélődnek fel...
public class Tile implements Renderable
{
	// a kulcsfelvételnél használt távolság - a játékos akkor veszi fel a kulcsot, ha ennél közelebb van a kulcshoz
	private static final float keyPickUpRadius = 5;
	// a tile egyedi azonosítója, a 0-s ID az ÜRES pályarészt jelenti
	private byte tileID;
	// a tile-t felépítő pályaelemek (téglalapok, háromszögek) listája
	private ArrayList<RenderableGameObject> objects;
	// a tile-on található kulcsok listája
	private ArrayList<Key> keys;
	// az adott itle-ból az egyes irányokba mely tile-okba lehet átmenni
	private ArrayList<Byte>[] reachableTiles;
	
	
	// konstruktor
	public Tile(byte tileID)
	{
		// azonosító mentése
		this.tileID = tileID;			// ide majd még valami ellenőrzés kell

		// listák létrehozása
		objects = new ArrayList<RenderableGameObject>();
		keys = new ArrayList<Key>();
		reachableTiles = (ArrayList<Byte>[]) new ArrayList[4];
	}
	
	public void addRGO(RenderableGameObject o)
	{
		objects.add(o);
	}
	
	public void addKey(Key k)
	{
		keys.add(k);
	}

	// tile renderelése
	@Override
	public void render(Graphics2D surface)
	{
		// TODO Auto-generated method stub
	}

	// kulcsok számának lekérdezése
	public int getNumberOfKeys()
	{
		return keys.size();
	}
	
	// megadja, hogy a paraméterül kapott objektum a rá ható erő hatására elhagyná-e a tile-t
	public boolean objectLeaves(RectangularRenderableGameObject object, Vector2d force)
	{
		// kérdezni...
		return false;
	}
	
	// megadja, hogy az adott tile-ból az adott irányban át lehet-e menni a destinationTileID azonosítójú tile-ba.
	public boolean canLeave(Direction direction, byte destinationTileID)
	{
		// ha az adott irányhoz tartozó listában benne van az elérni kívánt tile azonosítója, akkor át lehet rá menni
		return reachableTiles[direction.ordinal()].contains(destinationTileID);
	}
	
	// kulcs(ok) felvétele
	public void pickKey(Vector2d position)
	{
		// kulcsok bejárása iterátorral
		Iterator<Key> iterator = keys.iterator();
		while( iterator.hasNext() )
		{
			Key k = iterator.next();
			
			// távlság kiszámítása
			double distance = Vector2d.subtract(position, k.position).getLength();
			
			// ha egy bizonyos távolságon belül van, töröljük a kulcsot
			if(distance <= keyPickUpRadius)
			{
				iterator.remove();
			}
		}
	}
	
	// objektum mozgatása az adott tile-on belül a rá ható erő hatására, ütközésdetektálással
	// a visszatérési érték a paraméterül kapott objektum új pozíciója
	public Vector2d moveObject(RectangularRenderableGameObject object, Vector2d force)
	{
		// ütközésdetektálás lesz majd itt ...
		
		return object.position;
	}
}
