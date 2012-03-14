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
	public static void loadFromXML(String fn) throws ParserConfigurationException, SAXException, IOException
	{
		
		// Fajl megnyitasa, Document kinyerese
		File fajl = new File(System.getProperty("user.dir") + fn);
		DocumentBuilderFactory	dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder 		db = dbf.newDocumentBuilder();
		Document 				xml = db.parse(fajl);
		
		// objektumok beolvasasa NodeList-be
		NodeList objLista = xml.getElementsByTagName("tile");
		
		// konténer az éppen vizsgált objektumnak
		Node aktualisObj;
		
		// objektumok egyenkenti feldolgozasa
		for (int i = 0; i < objLista.getLength(); i++)
		{
			// objektum kivetele a listabol
			aktualisObj = objLista.item(i);
			// feldolgozzuk
			Element aktualisElement = (Element) aktualisObj;
			System.out.println("ID: " + ertek("id", aktualisElement));
		}
	}
	
	/** Ertek tag kiolvaso fugveny. 
	 * 	Kiolvassa egy Element-en beluli megadott valtozo tagjabol az erteket.
	 * 
	 *	@param nev	Kiolvasando valtozo neve
	 *	@param e	Az az Element, aminek a tagvaltozojara kivancsiak vagyunk
	 */
	private static String ertek(String nev, Element e)
	{
		// a megadott elementten belul megkeressuk a valtozot, es visszaadjuk
		NodeList valtozo = e.getElementsByTagName(nev).item(0).getChildNodes();
		Node valtozoNode = valtozo.item(0);
		return valtozoNode.getNodeValue();
	}
}
