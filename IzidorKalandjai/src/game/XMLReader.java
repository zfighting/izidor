/** XMLReader - XML betolto osztaly.
 *
 * @author Varga Istvan LFHLPF
 * @version 1.0
 * 
 */

package game;
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
		Vector2d sp_loc = new Vector2d();
		
		// Leendo door
		int	dr_id = -1;
		Vector2d dr_loc = new Vector2d();
		Paint dr_p = Color.GREEN;
		float dr_w = 10;
		float dr_h = 20;
		
		// Leendo key
		int	ky_id = -1;
		Vector2d ky_loc = new Vector2d();
		Paint ky_p = Color.RED;
		float ky_w = 10;
		float ky_h = 20;
		
		// Leendo rectangle, triangle
		Polygon vtx = null;
		int vx = 0;
		int vy = 0;
		Paint RGO_p = Color.BLACK;
			
		// Fajl megnyitasa, Document kinyerese
		File fajl = new File(System.getProperty("user.dir") + File.separator + fn);
		DocumentBuilderFactory	dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder 		db = dbf.newDocumentBuilder();
		Document 				xml = db.parse(fajl);
		
		// Aktuálisan feldolgozott node valtozoi
		Node 		akt;
		NodeList 	aktList;
		NodeList	gyList;
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
		akt = aktList.item(0);	// csak 1db lehet
		sp_id = Integer.parseInt(gyerekTagErtek("tileid", (Element)akt));
		sp_loc.x = Integer.parseInt(gyerekTagErtek("x", (Element)akt));
		sp_loc.y = Integer.parseInt(gyerekTagErtek("y", (Element)akt));
		
		// Door feldolgozas
		aktList = xml.getElementsByTagName("door");
		akt = aktList.item(0);	// csak 1db lehet
		dr_id = Integer.parseInt(gyerekTagErtek("tileid", (Element)akt));
		dr_loc.x = Integer.parseInt(gyerekTagErtek("x", (Element)akt));
		dr_loc.y = Integer.parseInt(gyerekTagErtek("y", (Element)akt));
		
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
			tl[k - 1][tid - ((k - 1) * width)] = new Tile((byte) tid);
			
			// Ha vannak benne kulcsok, akkor eltaroljuk oket
			gyList = ((Element)akt).getElementsByTagName("key");
			for (int x = 0; x < gyList.getLength(); x++)
			{
				ky_id = Integer.parseInt(gyerekTagErtek("tileid", (Element)akt));
				ky_loc.x = Integer.parseInt(gyerekTagErtek("x", (Element)gyList.item(x)));
				ky_loc.y = Integer.parseInt(gyerekTagErtek("y", (Element)gyList.item(x)));				
				tl[k - 1][tid - ((k - 1) * width)].addKey(new Key((byte)ky_id, new Vector2d(ky_loc.x, ky_loc.y), ky_p, ky_w, ky_h));
			}
			
			// Ha van benne Rectangle eltároljuk
			gyList = ((Element)akt).getElementsByTagName("rectangle");
			for (int x = 0; x < gyList.getLength(); x++)
			{
				vtx = new Polygon();
				vtxList = ((Element)gyList.item(x)).getElementsByTagName("vertex");
				for (int y = 0; y < vtxList.getLength(); y++)
				{
					vx = Integer.parseInt(gyerekTagErtek("x", (Element)vtxList.item(y)));
					vy = Integer.parseInt(gyerekTagErtek("y", (Element)vtxList.item(y)));				
					vtx.addPoint(vx, vy);
				}
				tl[k - 1][tid - ((k - 1) * width)].addRGO(new Rectangle((byte)ky_id, new Vector2d(vtx.xpoints[3], vtx.ypoints[3]), RGO_p, vtx));
			}
			
			// Ha van benne Triangle eltároljuk
			gyList = ((Element)akt).getElementsByTagName("triangle");
			for (int x = 0; x < gyList.getLength(); x++)
			{
				vtx = new Polygon();
				vtxList = ((Element)gyList.item(x)).getElementsByTagName("vertex");
				for (int y = 0; y < vtxList.getLength(); y++)
				{
					vx = Integer.parseInt(gyerekTagErtek("x", (Element)vtxList.item(y)));
					vy = Integer.parseInt(gyerekTagErtek("y", (Element)vtxList.item(y)));				
					vtx.addPoint(vx, vy);
				}
				tl[k - 1][tid - ((k - 1) * width)].addRGO(new Triangle((byte)ky_id, new Vector2d(vtx.xpoints[2], vtx.ypoints[2]), RGO_p, vtx));
			}
		}
		ret.build(tl, new SpawnPoint((byte) sp_id, sp_loc), new Door((byte) dr_id, dr_loc, dr_p, dr_w, dr_h));
		return ret;	
	}
	
	/** Ertek tag kiolvaso fugveny. 
	 * 	Kiolvassa egy Element-en beluli megadott tagbol a PCDATA-t.
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