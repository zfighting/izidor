package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import engine.Vector2d;

// pályarészek (csempék) osztálya
// ilyenekből épül fel a pálya (stage), ezeken belül szabadon mozoghat
// a játékos, tilitoli módban ezek cserélődnek fel...
public class Tile implements Renderable
{
	// minden tile mérete fix - ezek a magic numberek azalapján lettek kitalálva, hogy 3x3-mas pálya még kiférjen a 800x600-as ablakba
	private static final int width = 250;
	private static final int height = 170;
	// a kulcsfelvételnél használt távolság - a játékos akkor veszi fel a kulcsot, ha ennél közelebb van a kulcshoz
	public static final float keyPickUpRadius = 5;
	// a tile egyedi azonosítója, a 0-s ID az ÜRES pályarészt jelenti
	private byte tileID;
	// a tile-t felépítő pályaelemek (téglalapok, háromszögek) listája
	private ArrayList<RenderableGameObject> objects;
	// a tile-on található kulcsok listája
	public ArrayList<Key> keys;
	
	// az adott tile-ból az egyes irányokba mely tile-okba lehet átmenni
	// 0 index = UP
	// 1 index = DOWN
	// 2 index = LEFT
	// 3 index = RIGHT
	private ArrayList<Byte>[] reachableTiles;
	
	
	// konstruktor
	@SuppressWarnings("unchecked")
	public Tile(byte tileID)
	{
		// azonosító mentése
		this.tileID = tileID;			// ide majd még valami ellenőrzés kell

		// listák létrehozása
		objects = new ArrayList<RenderableGameObject>();
		keys = new ArrayList<Key>();
		reachableTiles = (ArrayList<Byte>[]) new ArrayList[4];
		
		// Ez még kellett hozzá azért írtam bele, különben NullPointerException (I.)
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
	public void addKey(Key k)
	{
		keys.add(k);
	}
	
	public int containsKey()
	{
		return keys.size();
	}
	
	public void addReachableTile(byte tid, Direction dir)
	{
		// JRE 1.6 kompatibilis if
		// 0 index = UP
		// 1 index = DOWN
		// 2 index = LEFT
		// 3 index = RIGHT
		if (dir == Direction.UP)
		{
			reachableTiles[0].add(tid);
		}
		else if (dir == Direction.DOWN)
		{
			reachableTiles[1].add(tid);
		}
		else if (dir == Direction.LEFT)
		{
			reachableTiles[2].add(tid);
		}
		else if (dir == Direction.RIGHT)
		{
			reachableTiles[3].add(tid);
		}
		else System.out.println("ReachableTiles indexelési hiba.");
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
	
	// megadja, hogy a paraméterül kapott objektum a rá ható erő hatására elhagyná-e a tile-t
	public boolean objectLeaves(RectangularRenderableGameObject object, Vector2d force)
	{
		//az object a rá ható force általi új poziciója
		Vector2d newposition = Vector2d.add(object.position, force);
		//ha ez az új pozició nincs benne a tile-ban, akkor elhagyná a tile-t
		if(newposition.x>getWidth() || newposition.x < 0 || newposition.y < 0 || newposition.y > height)
			return true;
		

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
	public CollisionDetectionResult moveObject(RectangularRenderableGameObject object, Vector2d force)
	{
		//ütközés nélkül ide kerülne a rá ható erő hatására
		Vector2d newposition = Vector2d.add(object.position, force);
		
		// ez lesz a mozgatás eredménye (az objektum új helye)
		CollisionDetectionResult result = new CollisionDetectionResult(newposition, false, false);
		//ekkora egységenként mozgatjuk a téglalapot(rect-et)
		Vector2d delta = new Vector2d((newposition.x - object.position.x) / 10.0f, (newposition.y - object.position.y) / 10.0f);
		//object jelenlegi pozicioja
		Vector2d currentposition = object.position;
		currentposition = Vector2d.add(currentposition, delta);
		
		//egységenkénti mozgatás
		for( int i = 0; i < 10; i++ )
		{
			
			//segéd téglalap, bal felső sarka, a currentpositionban van, mérete akkora mint a Player.
			//Ezt a téglalapot használjuk fel az ütközések detektelásához.
			java.awt.geom.Rectangle2D rect = new java.awt.geom.Rectangle2D.Float(currentposition.x, currentposition.y, object.width, object.height); 
			
			currentposition = Vector2d.add(currentposition, delta);
			rect = new java.awt.geom.Rectangle2D.Float(currentposition.x, currentposition.y, object.width, object.height); 
			
			//ütközés lefelé
			if(force.y > 0)
			{
				Vector2d temp = new Vector2d(force.x, force.y);
				force.x = 0;
				Iterator<RenderableGameObject> itr = objects.iterator();
			    while (itr.hasNext())
			    {
			    	RenderableGameObject o = itr.next();
			    	//System.out.println("rectangle: " + o.toString());
			    	//megnézzük h téglalap-e
			    	if( o.getClass().equals(Rectangle.class))
			    	{
			    		//ütközés van a segédtéglalap és a pályát alkotó téglap között
			    		//o.position = Vector2d.subtract(o.position, new Vector2d(0.01f, 0.01f));
			    		Rectangle2D primitiverect = ((Rectangle)o).polygon.getBounds2D();
			    		//System.out.println(((Rectangle)o).polygon.intersects(rect));
			    		//System.out.println(((Rectangle)o).polygon.getBounds2D().getY() >= currentposition.y);
			    		
			    		if( ((Rectangle)o).polygon.intersects(rect) && ((Rectangle)o).polygon.getBounds2D().getY() >= currentposition.y)
			    		{
					    	//System.out.println("ütközés lefele: " + o.toString());
					    	result.newPosition.x = currentposition.x;
			    			result.newPosition.y = (float) ((float) 170 - ((Rectangle)o).polygon.getBounds2D().getHeight() - rect.getHeight()) - 0.01f;
			    			result.collisionY = true;
			    			
			    			// !!!!!!!!!!!!!!!
			    			currentposition = result.newPosition;
			    			rect = new java.awt.geom.Rectangle2D.Float(currentposition.x, currentposition.y, object.width, object.height); 
			    			
			    			break;
			    		
			    		}
			    	
			    	}
			
			    }
			    force.x = temp.x;
			}
			//ütközés felfelé
			if(force.y < 0)
			{
				Vector2d temp = new Vector2d(force.x, force.y);
				force.x = 0;
				Iterator<RenderableGameObject> itr = objects.iterator();
			    while (itr.hasNext())
			    {
			    	RenderableGameObject o = itr.next();
			    	//megnézzük h téglalap-e
			    	if( o.getClass().equals(Rectangle.class))
			    	{
			    		//ütközés van a segédtéglalap és a pályát alkotó téglap között
			    		if( ((Rectangle)o).polygon.intersects(rect) )
			    		{
			    			result.newPosition.x = currentposition.x;
			    			result.newPosition.y = (float) (((Rectangle)o).polygon.getBounds2D().getX()) + 0.01f;
			    			result.collisionY = true;
			    			break;
			    		
			    		}
			    	
			    	}
			
			    }
			    force.x = temp.x;
			}
			//ütközés jobbra
			if(force.x > 0)
			{
				
				Iterator<RenderableGameObject> itr = objects.iterator();
			    while (itr.hasNext())
			    {
			    	RenderableGameObject o = itr.next();
			    	//megnézzük h téglalap-e
			    	if( o.getClass().equals(Rectangle.class))
			    	{
			    		//ütközés van a segédtéglalap és a pályát alkotó téglap között
			    		if( ((Rectangle)o).polygon.intersects(rect) )
			    		{
			    			result.newPosition.y = currentposition.y;
			    			result.newPosition.x = (float) (((Rectangle)o).polygon.getBounds2D().getX() - rect.getWidth()) - 0.01f;
			    			result.collisionX = true;
			    			
			    			// !!!!!!!!!!
			    			currentposition = result.newPosition;
			    			rect = new java.awt.geom.Rectangle2D.Float(currentposition.x, currentposition.y, object.width, object.height); 
			    			
			    			break;
			    		
			    		}
			    	
			    	}

			    }	
			}
			//ütközés balra
			if(force.x < 0)
			{
				Iterator<RenderableGameObject> itr = objects.iterator();
			    while (itr.hasNext())
			    {
			    	RenderableGameObject o = itr.next();
			    	//megnézzük h téglalap-e
			    	if(o.getClass().equals(Rectangle.class))
			    	{
			    		//ütközés van a segédtéglalap és a pályát alkotó téglap között
			    		if( ((Rectangle)o).polygon.intersects(rect) )
			    		{
			    			result.newPosition.y = currentposition.y;
			    			result.newPosition.x = (float) (((Rectangle)o).polygon.getBounds2D().getWidth() + ((Rectangle)o).polygon.getBounds2D().getX()) + 0.01f;
			    			result.collisionX = true;
			    			
			    			currentposition = result.newPosition;
			    			rect = new java.awt.geom.Rectangle2D.Float(currentposition.x, currentposition.y, object.width, object.height); 
			    			
			    			break;
			    		
			    		}
			    	
			    	}
			
			    }
			}
			
			
		}
		return result; 
	}
	
	//tileID adja vissza
	public byte getID()
	{
		return tileID;
	}

	public static int getWidth()
	{
		return width;
	}
}
