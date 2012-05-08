/**
 * 
 */
package game;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import engine.Vector2d;

// pályát reprezentáló osztály
// tile-okból áll, ezen játszik Izidor
public class Stage implements Renderable
{
	// a pályát felépítő tile-ok mátrixa
	private Tile[][] tiles;
	// a játékos megszületésének / újraszületésének helyszíne
	private SpawnPoint spawnPoint;
	// a pálya elhagyásának helyszíne
	private Door door;


	// konstruktor
	public Stage()
	{
	}
	
	// XMLReader segédosztály, tagváltozókhoz hozzáférést biztosít
	public void build(Tile[][] t, SpawnPoint sp, Door dr)
	{
		tiles = t;
		spawnPoint = sp;
		door = dr;
	}
	
	// spawnpoint lekérdezése - szükséges például a játékos objektumok elhelyezéséhez pályabetöltés után
	public SpawnPoint getSpawnPoint()
	{
		return spawnPoint;
	}
	
	// segédfüggvény, amely a paraméterül kapott Graphics2D objektumot a szintén paraméterül kapott tile-ba transzformálja
	public void translateSurfaceToTile(Graphics2D surface, byte tileID)
	{
		// tömbbeli indexek lekérdezése
		Index index = getTileIndex(tileID);
		
		// eltolás alkalmazása
		translateSurfaceToTile(surface, index.x, index.y);
	}
	// belső használatra szánt függvény - ugyanaz mint az előző, csak nem tileID-t vár, hanem tömbindexeket
	protected void translateSurfaceToTile(Graphics2D surface, int x, int y)
	{
		// margók kiszámítása (bal felső elem elhelyezése úgy, hogy a tile-ok mátrixa a képernyőn középre kerüljön)
		float margin_x = 400 - (tiles.length * Tile.width + (tiles.length - 1) * 10) / 2.f;
		float margin_y = 275 - (tiles[0].length * Tile.height + (tiles[0].length - 1) * 10) / 2.f;
		
		// eltolás alkalmazása
		surface.translate(margin_x + x * (Tile.width + 10), margin_y + y * (Tile.height + 10));
	}
	
	// a komplett pályát kirenderelő metódus
	@Override
	public void render(Graphics2D surface)
	{
		// összes tile kirajzolása 
		for( int x = 0; x < tiles.length; x++ )
		{
			for( int y = 0; y < tiles[0].length; y++ )
			{
				// transzformációs mátrix elmentése
				AffineTransform af = surface.getTransform();
				// koordinátarendszer eltolása - a tile bal felső sarkának elhelyezése a képernyőn
				translateSurfaceToTile(surface, x, y);
				// az adott tile kirenderelése
				tiles[x][y].render(surface);
				// elmentett transzformációs mátrix visszaállítása
				surface.setTransform(af);
			}
		}
		
		// ajtó kirajzolása
		// transzformációs mátrix elmentése
		AffineTransform af = surface.getTransform();
		// koordinátarendszer eltolása - a tile bal felső sarkának elhelyezése a képernyőn
		translateSurfaceToTile(surface, door.getTileID());
		// ajtó kirajzolása
		door.render(surface);
		// elmentett transzformációs mátrix visszaállítása
		surface.setTransform(af);
	}
	
	// segédfüggvény, amely megadja a paraméterül kapott tileID mátrixbeli koordinátáit
	class Index
	{
		public int x, y;
		public Index(int x, int y) { this.x = x; this.y = y; }
	}
	protected Index getTileIndex(byte tileID)
	{
		for( int x = 0; x < tiles.length; x++ )
		{
			for( int y = 0; y < tiles[x].length; y++ )
			{
				if( tiles[x][y].getID() == tileID )
				{
					return new Index(x, y);
				}
			}
		}
		return new Index(-1, -1);
	}
	
	// tilitoli módban történő nyílbillentyű leütésének lekezelése,
	// a megfelelő tile-ok felcserélése (ha lehetséges)
	// egyetlen paraméter a leütött nyílbillentyű
	public void swap(Direction direction)
	{
		// üres tile indexeinek megkeresése
		Index empty = getTileIndex((byte) 0);
		
		// ellenőrzés, hogy lehet-e cserélni (üres elem melletti indexek ellenőrzése)
		boolean canSwap;
		canSwap = true;
		
		//mekkora maga a játék, 2x2-es vagy 3x3-as
		int ymax = tiles.length -1; 
		int xmax = tiles[0].length -1;
		
		//ha az empty tile nincs benne a mátrixban
		if( empty.x <0 || empty.x>xmax || empty.y<0 || empty.y>ymax )
		{
			throw new RuntimeException();
		}
		
		switch(direction)
		{
		//ha a legalsó sorban van az üres tile, akkor nem tudunk fölfele nyílra mozgatni
		case UP: if(empty.y == ymax) canSwap = false; break;
		
		//ha a legfelső sorban van az üres tile, akkor nem tudunk a lefele nyílra mozgatni
		case DOWN: if(empty.y == 0) canSwap = false; break;
		
		//ha a legbaloldalibb oszlopban van az üres tile, akkor nem tudunk jobb nyíl hatására tilet mozgatni
		case RIGHT: if(empty.x == 0) canSwap = false; break;
		
		//ha a legjobboldalibb oszlopban van az üres tile, akkor nem tudunk a bal nyíl hatására tilet mozgatni
		case LEFT: if(empty.x == xmax) canSwap = false; break;
		}
		
		// ha lehetséges, felcseréljük a 2 elemet
		if( canSwap )
		{
			switch( direction )
			{
			case UP:    Tile temp = tiles[empty.x][empty.y];
						tiles[empty.x][empty.y] = tiles[empty.x][empty.y + 1];
						tiles[empty.x][empty.y + 1] = temp;
						break;
			
			case DOWN:  temp = tiles[empty.x][empty.y];
						tiles[empty.x][empty.y] = tiles[empty.x][empty.y - 1];
						tiles[empty.x][empty.y - 1] = temp;
						break;

			
			case RIGHT: temp = tiles[empty.x][empty.y];
						tiles[empty.x][empty.y] = tiles[empty.x - 1][empty.y];
						tiles[empty.x - 1][empty.y] = temp;
						break;

			
			case LEFT:  temp = tiles[empty.x][empty.y];
						tiles[empty.x][empty.y] = tiles[empty.x + 1][empty.y];
						tiles[empty.x + 1][empty.y] = temp;
						break;
			}
		}
	}
	
	// kipörgették-e a pályát?
	// ha a paraméterül kapott objektum az ajtó közelében van, és már nincs kulcs a tile-okon, akkor igen
	public boolean isFinished(Vector2d position, byte tileID)
	{
		// kulcsok megszámlálása
		int number_of_keys = 0;
		for( int x = 0; x < tiles.length; x++ )
		{
			for( int y = 0; y < tiles[0].length; y++ )
			{
				number_of_keys += tiles[x][y].getNumberOfKeys();
			}
		}
		
		// ha teljesülnek a feltételek
		if( (number_of_keys == 0) && (tileID == door.getTileID()) && (Vector2d.subtract(position, new Vector2d(door.position.x + door.getWidth() / 2, door.position.y + door.getHeight() / 2)).getLength() <= Tile.pickUpRadius) )
		{
			return true;
		}
		
		return false;
	}
	
	// paraméterül kapott játékos objektum mozgatása
	public void movePlayer(Player player)
	{
		// melyik tile-ban van éppen a játékos?
		Index player_tile_index = getTileIndex(player.getTileID());
		
		// továbbadjuk a játékost az őt tartalmazó tile-nak, mozgassa az.
		// a visszaadott eredmény a játékos új pozíciója
		Vector2d new_position = tiles[player_tile_index.x][player_tile_index.y].moveObject(player, player.force);
		
		// a játékos erre a tile-ra kerül a mozgás során. kezdetben ez a mostani tile, ezt a tile.moveobject(...) nem módosítja
		byte new_tile_id = player.getTileID();
		
		// a tile-on belüli mozgatást elvégezte a tile.moveObject(...). az új pozíción azonban kilóghat a játékos az adott tile-ról
		// ebben az esetben meg kell vizsgálni, hogy az adott irányba a játékos elhagyhatja-e a tile-ját
		// így történik meg a tile-ok közötti áthaladás
		
		// befoglaló téglalap az új helyen
		Rectangle2D target_rectangle = new Rectangle2D.Float(new_position.x, new_position.y, player.getWidth(), player.getHeight());
		
		// kilóg-e felfelé
		if( target_rectangle.intersects(0, -Tile.height, Tile.width, Tile.height) )
		{
			// ha van felette tile és a felette lévő tile nem az üres tile, és át is lehet rá menni...
			if( (player_tile_index.y > 0) &&
				(tiles[player_tile_index.x][player_tile_index.y - 1].getID() != 0) &&
				(tiles[player_tile_index.x][player_tile_index.y].canLeave(Direction.UP, tiles[player_tile_index.x][player_tile_index.y - 1].getID())) )
			{
				// akkor átmegy rá
				new_tile_id = tiles[player_tile_index.x][player_tile_index.y - 1].getID();
				new_position.y = Tile.height - player.getHeight() + new_position.y;
			}
			// ellenkező esetben megakad a "plafonban"
			else
			{
				new_position.y = 0;
			}
		}
		// kilóg-e lefelé
		else if( target_rectangle.intersects(0, Tile.height, Tile.width, Tile.height) )
		{
			// ha van alatta tile és az alatta lévő tile nem az üres tile, és át is lehet rá menni...
			if( (player_tile_index.y < tiles[0].length - 1) &&
				(tiles[player_tile_index.x][player_tile_index.y + 1].getID() != 0) &&
				(tiles[player_tile_index.x][player_tile_index.y].canLeave(Direction.DOWN, tiles[player_tile_index.x][player_tile_index.y + 1].getID())) )
			{
				// akkor átmegy rá
				new_tile_id = tiles[player_tile_index.x][player_tile_index.y + 1].getID();
				new_position.y =  new_position.y + player.getHeight() - Tile.height;
			}
			// ellenkező esetben meghal, azaz respawnol
			else
			{
				new_position.x = spawnPoint.position.x;
				new_position.y = spawnPoint.position.y;
				new_tile_id = spawnPoint.getTileID();
				player.setForce(new Vector2d(0, 0));
			}
		}
		// kilóg-e balra
		else if( target_rectangle.intersects(-Tile.width, 0, Tile.width, Tile.height) )
		{
			// ha van tőle balra tile és a balra lévő tile nem az üres tile, és át is lehet rá menni...
			if( (player_tile_index.x > 0) &&
				(tiles[player_tile_index.x - 1][player_tile_index.y].getID() != 0) &&
				(tiles[player_tile_index.x][player_tile_index.y].canLeave(Direction.LEFT, tiles[player_tile_index.x - 1][player_tile_index.y].getID())) )
			{
				// akkor átmegy rá
				new_tile_id = tiles[player_tile_index.x - 1][player_tile_index.y].getID();
				new_position.x = Tile.width - player.getWidth() + new_position.x;
			}
			// ellenkező esetben megakad a falban
			else
			{
				new_position.x = 0;
			}
		}
		// kilóg-e jobbra
		else if( target_rectangle.intersects(Tile.width, 0, Tile.width, Tile.height) )
		{
			// ha van tőle jobbra tile és a jobbra lévő tile nem az üres tile, és át is lehet rá menni...
			if( (player_tile_index.x < tiles.length - 1) &&
				(tiles[player_tile_index.x + 1][player_tile_index.y].getID() != 0) &&
				(tiles[player_tile_index.x][player_tile_index.y].canLeave(Direction.RIGHT, tiles[player_tile_index.x + 1][player_tile_index.y].getID())) )
			{
				// akkor átmegy rá
				new_tile_id = tiles[player_tile_index.x + 1][player_tile_index.y].getID();
				new_position.x = new_position.x + player.getWidth() - Tile.width;
			}
			// ellenkező esetben megakad a falban
			else
			{
				new_position.x = Tile.width - player.getWidth();
			}
		}
		
		
		// most már tudjuk, hogy hová kerül a játékos, fel lehet vele vetetni a kulcsokat
		player_tile_index = getTileIndex(new_tile_id);
		tiles[player_tile_index.x][player_tile_index.y].pickKey(new Vector2d(new_position.x + player.getWidth() / 2, new_position.y + player.getHeight() / 2));
		
		
		// játékos elhelyezése az új pozíciójába
		try
		{
			player.moveTo(new_tile_id, new_position);
		}
		catch (InvalidTileIDException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
