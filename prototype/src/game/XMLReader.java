/** XMLReader - XML betolto osztaly.
 *
 * @author Varga Istvan LFHLPF
 * @version 1.0
 * 
 */

package game;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import engine.Vector2d;


import java.awt.Color;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class XMLReader
{
	/** Betolto fugveny. 
	 * 	Betolti a parameterkent kapott fajlbol a stage-et.
	 * 
	 *	@param fn	Fajl eleresi helye
	 * 	@throws ParserConfigurationException 
	 * 	@throws IOException 
	 * 	@throws SAXException 
	 */
	public static Stage load(String fn) throws ParserConfigurationException, SAXException, IOException
	{
		// Visszaadando felepitett stage
		Stage ret = new Stage();
		Tile[][] tl;
		int width = 0;
		int height = 0;
		
		// Leendo spawnpoint
		int	sp_id = -1;
		Vector2d sp_loc = new Vector2d(-1, -1);
		
		// Leendo door
		int	dr_id = -1;
		Vector2d dr_loc = new Vector2d(-1, -1);
		//File door_file = new File(System.getProperty("user.dir") + File.separatorChar + "res" + File.separatorChar + "doorSprite.png");
		//BufferedImage door_image;
		//door_image = ImageIO.read(door_file);
		//TexturePaint door_paint = new TexturePaint(door_image, new Rectangle2D.Float(0, 0, door_image.getWidth(), door_image.getHeight()));
		Paint door_paint = Color.BLACK;
		float door_w = 10;
		float door_h = 20;
		
		// Leendo key objektum
		int	ky_id = -1;
		Vector2d ky_loc = new Vector2d();
		//File key_file = new File(System.getProperty("user.dir") + File.separatorChar + "res" + File.separatorChar + "keySprite.png");
		//BufferedImage key_image;
		//key_image = ImageIO.read(key_file);
		//TexturePaint key_paint = new TexturePaint(key_image, new Rectangle2D.Float(0, 0, key_image.getWidth(), key_image.getHeight()));
		Paint key_paint = Color.BLACK;
		float key_width = 20;
		float key_height = 14;
		
		// Leendo rectangle, triangle
		Polygon vtx = null;
		int vx = 0;
		int vy = 0;
		Paint RGO_p = Color.BLACK;
			
		// Fajl megnyitasa, Document kinyerese
		File fajl = new File(System.getProperty("user.dir") + File.separatorChar + fn);
		DocumentBuilderFactory	dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder 		db = dbf.newDocumentBuilder();
		Document 				xml = db.parse(fajl);
		
		// Aktuálisan feldolgozott node valtozoi
		Node 		akt;
		Node		akt2;
		Node		akt3;
		NodeList 	aktList;
		NodeList	gyList;
		NodeList	gyList2;
		NodeList	vtxList;
		
		// Size feldolgozas
		aktList = xml.getElementsByTagName("size");
		akt = aktList.item(0);	// csak 1db lehet
		width = Integer.parseInt(gyerekTagErtek("width", (Element)akt ));
		height = Integer.parseInt(gyerekTagErtek("height", (Element)akt ));
		
		// Tudjuk hanyszor hanyas, tileok felepitese
		tl = new Tile[width][height];
		
		// Spawnpoint feldolgozas
		aktList = xml.getElementsByTagName("spawnpoint");
		if (aktList.getLength() != 0)
		{
			akt = aktList.item(0);	// csak 1db lehet
			sp_id = Integer.parseInt(gyerekTagErtek("tileid", (Element)akt));
			sp_loc.x = Integer.parseInt(gyerekTagErtek("x", (Element)akt));
			sp_loc.y = Integer.parseInt(gyerekTagErtek("y", (Element)akt));
		}
		// Door feldolgozas
		aktList = xml.getElementsByTagName("door");
		if (aktList.getLength() != 0)
		{
			akt = aktList.item(0);	// csak 1db lehet
			dr_id = Integer.parseInt(gyerekTagErtek("tileid", (Element)akt));
			dr_loc.x = Integer.parseInt(gyerekTagErtek("x", (Element)akt));
			dr_loc.y = Integer.parseInt(gyerekTagErtek("y", (Element)akt));
		}
		// Tile feldolgozas
		aktList = xml.getElementsByTagName("tile");
		int k = 1; //akt. tile sora

		// Mindegyik tile-on végigmegyünk
		for (int i = 0; i < aktList.getLength(); i++)
		{
			// Aktualis node tileid-je
			int tid;
			akt = aktList.item(i);
			tid = Integer.parseInt(gyerekTagErtek("tileid", (Element)akt));
			
			// Eldontjuk a tile tombben hova kerul
			// Ha masik sorban van, akkor k novelodik, így tl[][]-ben jo helyen lesz
			if (tid >= (k * width))
				k++;	
			
			// Tile letrehozasa a tileid alapjan szamitott helyen
			// Ha a tid nagyobb mint az aktualis sor utolso elemenek tid-je, akkor uj sorba kell rakjuk
			tl[k - 1][tid - ((k - 1) * width)] = new Tile((byte) tid);
	
			// Reachable tiles kiolvasása
			gyList = ((Element)akt).getElementsByTagName("reachable_tiles");
			if (gyList.getLength() != 0)
			{
				akt2 = gyList.item(0);
					
					// UP
					gyList = ((Element)akt2).getElementsByTagName("up");
					akt3 = gyList.item(0);
				
						// Azon belül a tileid-k
						gyList2 = ((Element)akt3).getElementsByTagName("tileid");
						
						// Összeset kiszedjük
						for (int x = 0; x < gyList2.getLength(); x++)
						{
							int rtid = Integer.parseInt(gyList2.item(x).getFirstChild().getNodeValue());
							tl[k - 1][tid - ((k - 1) * width)].addReachableTile((byte)rtid, Direction.UP);
						}
						
					// DOWN
					gyList = ((Element)akt2).getElementsByTagName("down");
					akt3 = gyList.item(0);
					
						// Azon belül a tileid-k
						gyList2 = ((Element)akt3).getElementsByTagName("tileid");
							
						// Összeset kiszedjük
						for (int x = 0; x < gyList2.getLength(); x++)
						{
							int rtid = Integer.parseInt(gyList2.item(x).getFirstChild().getNodeValue());
							tl[k - 1][tid - ((k - 1) * width)].addReachableTile((byte)rtid, Direction.DOWN);
						}			
				
						// Reachable tiles kiolvasása
						gyList = ((Element)akt).getElementsByTagName("reachable_tiles");
						akt2 = gyList.item(0);
						
						
					// LEFT
					gyList = ((Element)akt2).getElementsByTagName("left");
					akt3 = gyList.item(0);
	
						// Azon belül a tileid-k
						gyList2 = ((Element)akt3).getElementsByTagName("tileid");
	
						// Összeset kiszedjük
						for (int x = 0; x < gyList2.getLength(); x++)
						{
							int rtid = Integer.parseInt(gyList2.item(x).getFirstChild().getNodeValue());
							tl[k - 1][tid - ((k - 1) * width)].addReachableTile((byte)rtid, Direction.LEFT);
						}
	
					// RIGHT
					gyList = ((Element)akt2).getElementsByTagName("right");
					akt3 = gyList.item(0);
	
						// Azon belül a tileid-k
						gyList2 = ((Element)akt3).getElementsByTagName("tileid");
	
						// Összeset kiszedjük
						for (int x = 0; x < gyList2.getLength(); x++)
						{
							int rtid = Integer.parseInt(gyList2.item(x).getFirstChild().getNodeValue());
							tl[k - 1][tid - ((k - 1) * width)].addReachableTile((byte)rtid, Direction.RIGHT);
						}					
			}
			// Ha vannak benne kulcsok, akkor eltaroljuk oket
			gyList = ((Element)akt).getElementsByTagName("key");
			for (int x = 0; x < gyList.getLength(); x++)
			{
				// Kiszedjuk az ertekeket egyenkent minden kulcsbol
				ky_id = Integer.parseInt(gyerekTagErtek("tileid", (Element)akt));
				ky_loc.x = Integer.parseInt(gyerekTagErtek("x", (Element)gyList.item(x)));
				ky_loc.y = Integer.parseInt(gyerekTagErtek("y", (Element)gyList.item(x)));		
				// Letrehozzuk a kulcsot a tileban
				tl[k - 1][tid - ((k - 1) * width)].addKey(new Key((byte)ky_id, new Vector2d(ky_loc.x, ky_loc.y), key_paint, key_width, key_height));
			}
			
			// Ha van benne Rectangle eltároljuk
			gyList = ((Element)akt).getElementsByTagName("rectangle");
			for (int x = 0; x < gyList.getLength(); x++)
			{
				// Létrehozunk egy poligont, amit feltoltunk a palya alapjan majd vertexekkel
				vtx = new Polygon();
				vtxList = ((Element)gyList.item(x)).getElementsByTagName("vertex");
				for (int y = 0; y < vtxList.getLength(); y++)
				{
					// Minden vertexen vegigmegyunk, es kiolvassuk a koordinatait, majd hozzaadjuk a teglalaphoz
					vx = Integer.parseInt(gyerekTagErtek("x", (Element)vtxList.item(y)));
					vy = Integer.parseInt(gyerekTagErtek("y", (Element)vtxList.item(y)));				
					vtx.addPoint(vx, vy);
				}
				// Letrehozzuk a teglalapot a tileban
				tl[k - 1][tid - ((k - 1) * width)].addRenderableGameObject(new Rectangle((byte)tid, new Vector2d(0, 0), RGO_p, vtx));
			}
			
			// Ha van benne Triangle eltároljuk
			gyList = ((Element)akt).getElementsByTagName("triangle");
			for (int x = 0; x < gyList.getLength(); x++)
			{
				// Létrehozunk egy poligont, amit feltoltunk a palya alapjan majd vertexekkel
				vtx = new Polygon();
				vtxList = ((Element)gyList.item(x)).getElementsByTagName("vertex");
				for (int y = 0; y < vtxList.getLength(); y++)
				{
					// Minden vertexen vegigmegyunk, es kiolvassuk a koordinatait, majd hozzaadjuk a haromszoghoz
					vx = Integer.parseInt(gyerekTagErtek("x", (Element)vtxList.item(y)));
					vy = Integer.parseInt(gyerekTagErtek("y", (Element)vtxList.item(y)));				
					vtx.addPoint(vx, vy);
				}
				// Letrehozzuk a haromszoget a tileban
				tl[k - 1][tid - ((k - 1) * width)].addRenderableGameObject(new Triangle((byte)tid, new Vector2d(0, 0), RGO_p, vtx));
			}
		}
		// Felepitjuk a stage-et, majd visszaadjuk
		ret.build(tl, new SpawnPoint((byte) sp_id, sp_loc), new Door((byte) dr_id, dr_loc, door_paint, door_w, door_h));
		return ret;	
	}
	
	/** Ertek tag kiolvaso fugveny. 
	 * 	Kiolvassa egy Element-en beluli megadott tagbol a PCDATA-t.
	 *  Csak unique gyerekre mukodik, illetve az elsot adja vissza mindig, ha tobb van.
	 * 
	 *	@param nev	Kiolvasando tag neve
	 *	@param e	Az az Element, aminek a tagjara kivancsiak vagyunk
	 */
	private static String gyerekTagErtek(String nev, Element e)
	{
		// a megadott elementten belul megkeressuk a valtozot, es visszaadjuk
		NodeList valtozo = e.getElementsByTagName(nev).item(0).getChildNodes();
		Node valtozoNode = valtozo.item(0);
		return valtozoNode.getNodeValue();
	}
}