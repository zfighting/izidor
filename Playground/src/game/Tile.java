package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import engine.Vector2d;

// pályarészek (csempék) osztálya
// ilyenekből épül fel a pálya (stage), ezeken belül szabadon mozoghat
// a játékos, tilitoli módban ezek cserélődnek fel...
public class Tile implements Renderable
{
	// minden tile mérete fix - ezek a magic numberek az alapján lettek kitalálva, hogy 3x3-mas pálya még kiférjen a 800x600-as ablakba
	public static final int width = 250;
	public static final int height = 170;
	// a kulcsfelvételnél használt távolság - a játékos akkor veszi fel a kulcsot, ha ennél közelebb van a kulcshoz
	public static final float pickUpRadius = 10;
	// a tile egyedi azonosítója, a 0-s ID az ÜRES pályarészt jelenti
	private byte tileID;
	// a tile-t felépítő pályaelemek (téglalapok, háromszögek) listája
	private ArrayList<RenderableGameObject> objects;
	// a tile-on található kulcsok listája
	private ArrayList<Key> keys;
	// az adott tile-ból az egyes irányokba mely tile-okba lehet átmenni
	private ArrayList<Byte>[] reachableTiles;
	
	
	// konstruktor
	@SuppressWarnings("unchecked")
	public Tile(byte tileID)
	{
		// azonosító mentése
		this.tileID = tileID;

		// listák létrehozása
		objects = new ArrayList<RenderableGameObject>();
		keys = new ArrayList<Key>();
		reachableTiles = (ArrayList<Byte>[]) new ArrayList[4];
		reachableTiles[0] = new ArrayList<Byte>();
		reachableTiles[1] = new ArrayList<Byte>();
		reachableTiles[2] = new ArrayList<Byte>();
		reachableTiles[3] = new ArrayList<Byte>();
	}
	
	// renderelhető objektumok (a tile-t felépítő elemek) hozzáadása
	public void addRenderableGameObject(RenderableGameObject object)
	{
		objects.add(object);
	}
	
	// kulcs hozzáadása a tile-hoz
	public void addKey(Key key)
	{
		keys.add(key);
	}
	
	// elérhető tile hozzáadása az ezeket tartalmazó listához
	public void addReachableTile(byte tileID, Direction direction)
	{
		reachableTiles[direction.ordinal()].add(tileID);
	}

	// tile renderelése
	@Override
	public void render(Graphics2D surface)
	{
		// az üres tile-t nem rajzoljuk ki
		if( tileID == 0 )
		{
			return;
		}
		
		// keret kirajzolása
		surface.setPaint(Color.black);
		surface.drawRect(0, 0, 250, 170);
		
		// objektumok kirajzolása
		for( RenderableGameObject object : objects )
		{
			object.render(surface);
		}
		
		// kulcsok kirajzolása
		for( Key k : keys )
		{
			k.render(surface);
		}
	}

	// kulcsok számának lekérdezése
	public int getNumberOfKeys()
	{
		return keys.size();
	}
	
	// megadja, hogy ebből a tile-ból az adott irányban át lehet-e menni a destinationTileID azonosítójú tile-ba.
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
			double distance = Vector2d.subtract(position, new Vector2d(k.position.x + k.getWidth() / 2, k.position.y + k.getHeight() / 2)).getLength();
			
			// ha egy bizonyos távolságon belül van, töröljük a kulcsot
			if(distance <= pickUpRadius)
			{
				iterator.remove();
			}
		}
	}
	
	// objektum mozgatása az adott tile-on belül a rá ható erő hatására, ütközésdetektálással
	// a visszatérési érték a paraméterül kapott objektum új pozíciója
	public Vector2d moveObject(RectangularRenderableGameObject object, Vector2d force)
	{
		// jelenlegi pozíció
		Vector2d current_position = object.position;
		
		// cél pozíció - ide mozgatná a rá ható erővektor
		Vector2d target_position = new Vector2d();
		target_position.x = current_position.x;
		target_position.y = current_position.y;
		
		// mozgatás vízszintesen
		if( force.x != 0 )
		{
			// eltoljuk
			target_position.x += force.x;
			
			// befoglaló téglalap
			Rectangle2D target_rectangle = new Rectangle2D.Float(target_position.x, target_position.y, object.getWidth(), object.getHeight());
			
			// vizsgálat, hogy a cél téglalap metszésben van-e bármelyik játékelemmel
			for( RenderableGameObject game_object : objects )
			{
				if( game_object.getClass().getSuperclass().equals(Primitive.class) )
				{
					Primitive primitive = (Primitive)game_object;
					if( primitive.polygon.intersects(target_rectangle) )
					{
						// van metszés -> vízszintesen nem mozoghat az objektum
						target_position.x = current_position.x;
						break;
					}
				}
			}
		}
		
		// mozgatás függőlegesen
		if( force.y != 0 )
		{
			// eltoljuk
			target_position.y += force.y;
			
			// befoglaló téglalap
			Rectangle2D target_rectangle = new Rectangle2D.Float(target_position.x, target_position.y, object.getWidth(), object.getHeight());
			
			// vizsgálat, hogy a cél téglalap metszésben van-e bármelyik játékelemmel
			for( RenderableGameObject game_object : objects )
			{
				if( game_object.getClass().getSuperclass().equals(Primitive.class) )
				{
					Primitive primitive = (Primitive)game_object;
					if( primitive.polygon.intersects(target_rectangle) )
					{
						// van metszés -> függőlegesen nem mozoghat az objektum
						target_position.y = current_position.y;
						
						// ha játékos objektumot mozgatunk, és az vagy felfelé vagy lefelé mozogva akadálynak ütközött,
						// akkor a rá ható eredő y komponensét nullázzuk, ezzel leállítva a gravitációs gyorsulást vagy ugrást
						if( object.getClass().equals(Player.class) )
						{
							// ha lefelé mozgott, akkor az azt jelenti, hogy talajra érkezett, tehát ezután újra tud majd ugrani
							if( force.y > 0 )
							{
								((Player)object).canJump = true;
							}
							// a függőleges irányú gyorsulás pedig mindenképp megáll
							((Player)object).force.y = 0;
						}
						
						break;
					}
				}
			}
		}
		
		
		// eredmény visszaadása
		return target_position;
	}
	
	// tileID-t adja vissza
	public byte getID()
	{
		return tileID;
	}
}
