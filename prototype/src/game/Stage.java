/**
 * 
 */
package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

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
	
	public String getKeyPosition()
	{
		String ret = new String();
		for( int x = 0; x < tiles.length; x++ )
		{
			for( int y = 0; y < tiles[x].length; y++ )
			{
				if( tiles[x][y].containsKey() != 0 )   // itt javítottam, .equals() volt és nem működött, -1eket adott végig
				{
					for (int q = 0; q < tiles[x][y].getNumberOfKeys(); q++)
					{
						ret = ret + (tiles[x][y].keys.get(q).position.toString());
						ret += "\t";
					}
				}
			}
		}
		ret += "\n";
		return ret;
	}
	
	
	public String getTileInfo()
	{
		String ret = new String();
		int tn = 0;
		for( int x = 0; x < tiles.length; x++ )
		{
			for( int y = 0; y < tiles[x].length; y++ )
			{
				ret += "\ttilelocation = " + x + "," + y + "\t tileID = " + tiles[x][y].getID();
				ret += "\n";
			}
		}
		ret += "\n";
		return ret;
	}
	
	// a komplett pályát kirenderelő metódus
	@Override
	public void render(Graphics2D surface)
	{
		// középre kell igazítani a tile-ok mátrixát - itt lesz a bal felső elem
		float margin_x = 400 - (tiles.length * 250 + (tiles.length - 1) * 10) / 2.f;
		float margin_y = 275 - (tiles[0].length * 170 + (tiles[0].length - 1) * 10) / 2.f;
		
		// összes tile kirajzolása 
		for( int x = 0; x < tiles.length; x++ )
		{
			for( int y = 0; y < tiles[0].length; y++ )
			{
				// transzformációs mátrix elmentése
				AffineTransform af = surface.getTransform();
				// koordinátarendszer eltolása - a tile bal felső sarkának elhelyezése a képernyőn
				surface.translate(margin_x + x * 260, margin_y + y * 180);
				// az adott tile kirenderelése
				tiles[x][y].render(surface);
				// elmentett transzformációs mátrix visszaállítása
				surface.setTransform(af);
			}
		}
		
		// ajtó kirajzolása
		// meg kell határozni, hogy hol van az ajtót tartalmazó tile a mátrixunkban
		Index door_tile_index = getTileIndex(door.getTileID());
		// transzformációs mátrix elmentése
		AffineTransform af = surface.getTransform();
		// koordinátarendszer eltolása - a tile bal felső sarkának elhelyezése a képernyőn
		surface.translate(margin_x + door_tile_index.x * 260, margin_y + door_tile_index.y * 180);
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
				//játékos el akarja hagyni a tilet lefele, ha az aktuális tile amin van, a legalsó a mátrixban akkor egyértelműen meghal
				//ha nem a legalsó tileon van, akkor megnézzük, hogy az alatta lévő tile-ra átkerülhet-e, ha nem akkor is meghal
				if(currenttile.y == ymax || !tiles[currenttile.x][currenttile.y].canLeave(Direction.DOWN, (tiles[currenttile.x][currenttile.y + 1]).getID()))
				{
				
					// respawnol = visszakerül a spawnpoint-ra
					try
					{
						player.moveTo(spawnPoint.getTileID(), spawnPoint.position);
						
						//erő nullázása
						player.force.x = player.force.y = 0;
						return;
					}
					catch (InvalidTileIDException e)
					{
						// valami komoly hiba van a rendszerben, ha a spawnpoint az üres tile-ra van helyezve!
						e.printStackTrace();
					}
				}
				//lefele elhagyja a Tilet
				else
				{
					try
					{
						//átmegyünk a másik tile-ra
						player.moveTo((tiles[currenttile.x][currenttile.y + 1]).getID(), new Vector2d(player.position.x , 0));
						
						//kulcsot fel tudunk-e venni
						tiles[currenttile.x][currenttile.y + 1].pickKey(new Vector2d(player.position.x , 0));
						
						//ajtón állunk-e
						if(player.tileID == door.tileID)
						{
							double distance = Vector2d.subtract(player.position, door.position).getLength();
							if(distance <= tiles[currenttile.x][currenttile.y + 1].keyPickUpRadius)
							{
								//ha ajtón vagyunk...
							}
						}
						return;
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
				Direction direction  /* ... meghatározni a force alapján ... */ ;
				byte destinationTileID  /* ... meghatározni az aktuális tile indexei és a direction alapján ... */ ;
				
				if(player.force.y < 0)
				{
					direction = Direction.UP;
					destinationTileID = tiles[currenttile.x][currenttile.y - 1].getID();
					if(tiles[currenttile.x][currenttile.y].canLeave(Direction.UP, destinationTileID))
					{
						try
						{
							//átmegyünk a másik tile-ra
							player.moveTo(destinationTileID, new Vector2d(player.position.x , 170-player.height));
							
							//kulcsot fel tudunk-e venni
							tiles[currenttile.x][currenttile.y - 1].pickKey(new Vector2d(player.position.x , 170-player.height));
							
							//ajtón állunk-e
							if(player.tileID == door.tileID)
							{
								double distance = Vector2d.subtract(player.position, door.position).getLength();
								if(distance <= tiles[currenttile.x][currenttile.y - 1].keyPickUpRadius)
								{
									//ha ajtón vagyunk...
								}
							}
						
							
							return;
						}
						catch (InvalidTileIDException e)
						{
							// valami komoly hiba van a rendszerben, ha a spawnpoint az üres tile-ra van helyezve!
							e.printStackTrace();
						}	
					}
					else
					{
					//erő nullázása	
					player.force.x = player.force.y = 0;
					}
					
				}
				//jobbra hagyná el a tilet
				if(player.force.x > 0)
				{
					direction = Direction.RIGHT;
					destinationTileID = tiles[currenttile.x + 1][currenttile.y].getID();
					if(tiles[currenttile.x][currenttile.y].canLeave(Direction.RIGHT, destinationTileID))
					{
						try
						{
							//átmegyünk a másik tile-ra
							player.moveTo(destinationTileID, new Vector2d(0 , player.position.y));

							//kulcsot fel tudunk-e venni
							tiles[currenttile.x + 1][currenttile.y].pickKey(new Vector2d(0, player.position.y));
							
							//ajtón állunk-e
							if(player.tileID == door.tileID)
							{
								double distance = Vector2d.subtract(player.position, door.position).getLength();
								if(distance <= tiles[currenttile.x + 1][currenttile.y].keyPickUpRadius)
								{
									//ha ajtón vagyunk...
								}
							}
							return;
						}
						catch (InvalidTileIDException e)
						{
							// valami komoly hiba van a rendszerben, ha a spawnpoint az üres tile-ra van helyezve!
							e.printStackTrace();
						}	
					}
					else
					{
					//erő nullázása
					player.force.x = player.force.y = 0;
					}
				}
				
				//balra hagyná el a tilet
				if(player.force.x < 0)
				{
					direction = Direction.LEFT;
					destinationTileID = tiles[currenttile.x - 1][currenttile.y].getID();
					if(tiles[currenttile.x][currenttile.y].canLeave(Direction.LEFT, destinationTileID))
					{
						try
						{
							//átmegyünk a másik tile-ra
							player.moveTo(destinationTileID, new Vector2d(250-player.width , player.position.y));

							//kulcsot fel tudunk-e venni
							tiles[currenttile.x - 1][currenttile.y].pickKey(new Vector2d(250-player.width , player.position.y));
							
							//ajtón állunk-e
							if(player.tileID == door.tileID)
							{
								double distance = Vector2d.subtract(player.position, door.position).getLength();
								if(distance <= tiles[currenttile.x - 1][currenttile.y].keyPickUpRadius)
								{
									//ha ajtón vagyunk...
								}
							}
							return;
						}
						catch (InvalidTileIDException e)
						{
							// valami komoly hiba van a rendszerben, ha a spawnpoint az üres tile-ra van helyezve!
							e.printStackTrace();
						}	
					}
					else
					{
					//erő nullázása
					player.force.x = player.force.y = 0;
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
			//függöleges tengely mentén
			if(collresult.collisionY == true)
			{
				player.force.y = 0;
			}
			//vizszintes tengely mentén
			if(collresult.collisionX == true)
			{
				player.force.x = 0;
			}
			// játékos elhelyezése az új helyére
			player.moveTo(collresult.newPosition);
			player.force.x = 0;
			
			//kulcsot fel tudunk-e venni
			tiles[currenttile.x][currenttile.y].pickKey(collresult.newPosition);
			
			//ajtón állunk-e
			if(player.tileID == door.tileID)
			{
				double distance = Vector2d.subtract(player.position, door.position).getLength();
				if(distance <= tiles[currenttile.x][currenttile.y].keyPickUpRadius)
				{
					//ha ajtón vagyunk...
				}
			}
		}
	}
	
	public String toString()
	{
		
		//kulcsok száma
		int keys=0;
		for(int i=0; i < tiles.length; i++)
			for(int j=0; j < tiles[0].length ; j++)			
				keys +=tiles[i][j].getNumberOfKeys();
	
		
		return	(
				 "Width \t\t: " + tiles.length + "\n" +
				 "Height \t\t: " + tiles[0].length + "\n" +
				 "Spawnpoint \t: tileid = " + spawnPoint.tileID +",  x = "+ spawnPoint.position.x +",  y = "+ spawnPoint.position.x +"  "+"\n" +
				 "Door postion \t: tileid = " + door.tileID +",  x = "+ door.position.x +",  y = "+ door.position.y +"\n" +
				 "Key position(s)\t: " + getKeyPosition() +
				 "Tile information: \n" +
				 
				 getTileInfo() +
				 
				 "Remaining keys\t: " + keys + "\n"
				 );
		
	}

	public void addDoor(byte b, Vector2d position)
	{
		door = new Door(b, position, Color.BLACK, 20, 10);	
	}
	
	public void addKey(byte b, Vector2d position)
	{
		Index temp = getTileIndex(b);
		tiles[temp.x][temp.y].addKey(new Key(b, position, Color.RED, 20, 10));
	}
	
	public void addSpawnPoint(byte b, Vector2d position)
	{
		spawnPoint = new SpawnPoint(b, position);
	}
}
