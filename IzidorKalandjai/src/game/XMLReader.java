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
		SpawnPoint sp;
		int	sp_id = -1;
		Vector2d sp_loc = new Vector2d();
		
		// Leendo door
		Door dr;
		int	dr_id = -1;
		Vector2d dr_loc = new Vector2d();
		Paint dr_p = Color.GREEN;
		float dr_w = 10;
		float dr_h = 20;
			
		// Fajl megnyitasa, Document kinyerese
		File fajl = new File(System.getProperty("user.dir") + "\\" + fn);
		DocumentBuilderFactory	dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder 		db = dbf.newDocumentBuilder();
		Document 				xml = db.parse(fajl);
		
		// Aktuálisan feldolgozott node valtozoi
		Node 		akt;
		NodeList 	aktList;
		
		// Size feldolgozas
		aktList = xml.getElementsByTagName("size");
		akt = aktList.item(0);	// csak 1db lehet
		width = Integer.parseInt(gyerekTagErtek("width", (Element)akt ));
		height = Integer.parseInt(gyerekTagErtek("height", (Element)akt ));
		
		System.out.println("width: " + width);
		System.out.println("height: " + height + "\n");
		
		// Tudjuk hanyszor hanyas, tileok felepitese
		tl = new Tile[width][height];
		
		// Spawnpoint feldolgozas
		aktList = xml.getElementsByTagName("spawnpoint");
		akt = aktList.item(0);	// csak 1db lehet
		sp_id = Integer.parseInt(gyerekTagErtek("tileid", (Element)akt));
		sp_loc.x = Integer.parseInt(gyerekTagErtek("x", (Element)akt));
		sp_loc.y = Integer.parseInt(gyerekTagErtek("y", (Element)akt));
		
		System.out.println("spawnpoint tileid: " + sp_id);
		System.out.println("spawnpoint loc: " + sp_loc.toString() + "\n");
		
		// Door feldolgozas
		aktList = xml.getElementsByTagName("door");
		akt = aktList.item(0);	// csak 1db lehet
		dr_id = Integer.parseInt(gyerekTagErtek("tileid", (Element)akt));
		dr_loc.x = Integer.parseInt(gyerekTagErtek("x", (Element)akt));
		dr_loc.y = Integer.parseInt(gyerekTagErtek("y", (Element)akt));
		
		System.out.println("door tileid: " + dr_id);
		System.out.println("door loc: " + dr_loc.toString() + "\n");
		
		// Tile feldolgozas
		aktList = xml.getElementsByTagName("tile");
		int k, j; // k: akt. tile sora, j: akt. tile oszlopa
		k = 1;
		//j = 1;
		
		// Mindegyik tile-on végigmegyünk
		for (int i = 0; i < aktList.getLength(); i++)
		{
			// Aktualis node tileid-je
			int tid;
			akt = aktList.item(i);
			tid = Integer.parseInt(gyerekTagErtek("tileid", (Element)akt));
			
			// Eldontjuk a tile tombben hova kerul
			if (tid < (k * width))
			{ // aktualis sorban van
				tl[k - 1][tid - ((k - 1) * width)] = new Tile((byte) tid);
				
			}
			else
			{
				k++;
			}
			
		}
		
		return ret;
	
		
/*	RÉGI DEMO	
 * // objektumok beolvasasa NodeList-be
		NodeList objLista = xml.getElementsByTagName("tile");
		
		// kontener az eppen vizsgalt objektumnak
		Node aktualisObj;
		
		// objektumok egyenkenti feldolgozasa
		for (int i = 0; i < objLista.getLength(); i++)
		{
			// objektum kivetele a listabol
			aktualisObj = objLista.item(i);
			// feldolgozzuk
			Element aktualisElement = (Element) aktualisObj;
			System.out.println("ID: " + ertek("id", aktualisElement));
		}*/
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
