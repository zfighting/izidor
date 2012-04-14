/**
 * 
 */
package game;

import java.awt.Graphics2D;

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
	
	// a komplett pályát kirenderelő metódus
	@Override
	public void render(Graphics2D surface)
	{
		// ...
	}
	
	// segédfüggvény, amely megadja a paraméterül kapott tileID mátrixbeli koordinátáit
	class Index
	{
		public int x, y;
		public Index(int x, int y) { this.x = x; this.y = y; }
		
		public String toString()
		{return Integer.toString(x)+ Integer.toString(y);}
	}
	protected Index getTileIndex(byte tileID)
	{
		for( int x = 0; x < tiles.length; x++ )
		{
			for( int y = 0; y < tiles[x].length; y++ )
			{
				if( tiles[x][y].getID()==(tileID))   // itt javítottam, .equals() volt és nem működött, -1eket adott végig
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
		if(empty.x <0 || empty.x>xmax || empty.y<0 || empty.y>ymax)
			throw new RuntimeException();
		
		switch(direction){
		
		//ha a legalsó sorban van az üres tile, akkor nem tudunk fölfele nyílra mozgatni
		case UP: if(empty.y == ymax) canSwap = false; break;
		
		//ha a legfelső sorban van az üres tile, akkor nem tudunk a lefele nyílra mozgatni
		case DOWN: if(empty.y == 0) canSwap = false; break;
		
		//ha a legbaloldalibb oszlopban van az üres tile, akkor nem tudunk jobb nyíl hatására tilet mozgatni
		case RIGHT: if(empty.x == 0) canSwap = false; break;
		
		//ha a legjobboldalibb oszlopban van az üres tile, akkor nem tudunk a bal nyíl hatására tilet mozgatni
		case LEFT: if(empty.x == xmax) canSwap = false; break;
		
		
		}
		
		
		//canSwap = false;
		
		// ha lehetséges, felcseréljük a 2 elemet
		if( canSwap )
		{
			switch( direction )
			{
			
			case UP:  	Tile temp = tiles[empty.x][empty.y];
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
	
	
	// paraméterül kapott játékos objektum mozgatása
	public void movePlayer(Player player)
	{
		Index currenttile = getTileIndex( player.tileID );
		
		//mekkora maga a játék, 2x2-es vagy 3x3-as
		int ymax = tiles.length -1; 
		int xmax = tiles[0].length -1;
		
		// ellenőrzés, hogy a játékos elhagyná-e az aktuális tile-t
		Index currentTileIndex = getTileIndex(player.getTileID());
		boolean leavesTile = tiles[currentTileIndex.x][currentTileIndex.y].objectLeaves(player, player.getForce());
		
		// ha el akarja hagyni a tile-t...
		if( leavesTile )
		{
			
			// ha lefelé akarja elhagyni, és nincs alatta semmi, meghal (respawnol)
			//if( /* ... lefelé akar menni és nincs alatta semmi? ... */ false )
			if(player.force.y > 0)
			{
				
				if(currenttile.y == ymax || !tiles[currenttile.x][currenttile.y].canLeave(Direction.DOWN, (tiles[currenttile.x][currenttile.y + 1]).getID()))
				{
				
					// respawnol = visszakerül a spawnpoint-ra
					try
					{
						player.moveTo(spawnPoint.getTileID(), spawnPoint.position);
					}
					catch (InvalidTileIDException e)
					{
						// valami komoly hiba van a rendszerben, ha a spawnpoint az üres tile-ra van helyezve!
						e.printStackTrace();
					}
				}
				
			}
			else
			{
				// ellenőrzés, hogy az aktuális tile-t az adott irányban elhagyhatja-e a játékos
				// ehhez először meg kell határozni hogy melyik irányba menne, melyik tile van ott, majd meghívni
				// az aktuális tile canLeave metódusát ezekkel a paraméterekkel...
				Direction direction = /* ... meghatározni a force alapján ... */ Direction.LEFT;
				byte destinationTileID = /* ... meghatározni az aktuális tile indexei és a direction alapján ... */ 5;
				// ha lehetséges az áthaladás...
				if( tiles[currentTileIndex.x][currentTileIndex.y].canLeave(direction, destinationTileID) )
				{
					// a játékos átkerül a szomszédos tile szélére...
					try
					{
						player.moveTo(destinationTileID, new Vector2d(/* a direction alapján meghatározni hogy hol van a széle a tile-nak*/ 0, 0));
					}
					catch (InvalidTileIDException e)
					{
						// valami nagyon el van szarva...
						e.printStackTrace();
					}
				}
			}
		}
		else
		{
			// a játékos nem akarja elhagyni az aktuális tile-t, hanem azon belül mozog
			// megkérdezzük a tile-t, hogy hová kell kerülnie
			CollisionDetectionResult collresult = tiles[currentTileIndex.x][currentTileIndex.y].moveObject(player, player.getForce());
			//ha volt ütközés
			if(collresult.collisionY == true)
			{
				player.force.y = 0;
			}
			if(collresult.collisionX == true)
			{
				player.force.x = 0;
			}
			// játékos elhelyezése az új helyére
			player.moveTo(collresult.newPosition);
		}
	}
	
	public String toString()
	{
		
		//kulcsok száma
		int keys=0;
		for(int i=0;i <2; i++)
			for(int j=0;j <2; j++)			
				keys +=tiles[i][j].getNumberOfKeys();
		
		
		
		
		return	(
				 "Spawnpoint			: " + spawnPoint.tileID +"  "+ spawnPoint.position.x +"  "+ spawnPoint.position.x +"  "+"\n" +
				 "Door postion		: " + door.tileID +"  "+ door.position.x +"  "+ door.position.y +"\n" +
				 "Key position      	: " + "ennek van értelme?\n" +
				 "TileID_1 position 	: " + getTileIndex((byte) 0) + "\n"+
				 "TileID_2 position 	: " + getTileIndex((byte) 1) + "\n"+
				 "TileID_3 position 	: " + getTileIndex((byte) 2) + "\n"+
				 "TileID_4 position 	: " + getTileIndex((byte) 3) + "\n"+
				 "Remaining keys    	: " +  keys 	+"\n" +
				 ""
				 );
		
	}
}
