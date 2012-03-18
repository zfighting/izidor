package game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import engine.Vector2d;

// legfőbb osztály...
public class Game extends JFrame
{
	// a felület, amelyre renderelünk
	private RenderSurface renderSurface;
	
	// a mainloop folyamatos, fix időközönként való futtatásához felhasznált időzítő
	private Timer timer;
	
	
	// ablak létrehozása
	private void initUI(String title, int width, int height)
	{
		// render surface létrehozása és hozzáadása az ablakhoz
		renderSurface = new RenderSurface(width, height);
		getContentPane().add(renderSurface);

		// címsor beállítása
		setTitle(title);
		// az ablakot ne lehessen átméretezni
		setResizable(false);
		// ablak képernyő közepére igazítása
		pack();
		setLocationRelativeTo(null);
		// X-re záruljon be...
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// láss csodát! a technika diadala
		setVisible(true);
	}
	
	// XML teszt függveny, TORLENDO
	private static void XMLTeszt() throws ParserConfigurationException, SAXException, IOException
	{
		XMLReader.loadFromXML(File.separatorChar + "res" + File.separatorChar + "XMLTeszt.xml");
	}

	// konstruktor
	public Game()
	{
		// ablakot nyitunk
		initUI("Izidor kalandjai", 640, 480);
		
		// időzítő létrehozása és elindítása
		timer = new Timer();
		timer.scheduleAtFixedRate(new MainLoop(), 100, 20);
	}
	
	// beágyazott osztály a mainloop futtatásához
	class MainLoop extends TimerTask
	{
		// az időzítő által fix időközönként meghívott függvény
		@Override
		public void run()
		{
			// ----------------------- ide jön majd a mainloop, megfelelő gamestate meghívása ----------------------------
			renderSurface.repaint();	
		}
	}
	
	// main függvény - az alkalmazás belépési pontja
	public static void main(String[] args)
    {		
		// mi menő! 1 sor öcsém, EGY SOR!
		new Game();
		
		// XML teszt, TORLENDO
		try
		{
			XMLTeszt();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		//tesztesetekhez egy integer beolvasása
		int val = 0;  //ebben a válzozoban tároljuk a bekért számot
		while(val<1 || val>5){  //addig kérjük a számot amíg nem megfelelő
		String line = null;  
	    System.out.println("Type a number between 1 and 5");
	    try {
	      BufferedReader is = new BufferedReader(
	        new InputStreamReader(System.in));
	      try
		{
			line = is.readLine();
		}
		catch (IOException e) //ha nem tudunk beolvasni
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      val = Integer.parseInt(line);  //ha nem integert írtak be
	    } catch (NumberFormatException ex) {
	      System.err.println("Not a valid number: " + line);
	    }
	    if(val<1 || val>5) // ha nem megfelelő méretű a szám
	    {
	    	System.out.println("Not a valid number, it must be between 1 and 5");
	    }
		}
	    //Tests objektum létrehozása a teszteléshez
	    Tests cases = new Tests();
	    
	    switch (val)
	    {
	    //első teszteset meghívása(kulcs felvétele, majd pálya elhagyásának teszje)
	    case 1: cases.Test1(); break;
	    //második teszteset meghívása(Tilitoli tesztje)
	    case 2: cases.Test2(); break;
	    //harmadik teszteset meghívása(Két tile közötti áthaladás tesztje)
	    case 3: cases.Test3(); break;
	    //negyedik teszteset meghívása(Meghalás tesztje)
	    case 4: cases.Test4(); break;
	    //ötödik teszteset meghívása(Ütközés tesztje)
	    case 5: cases.Test5(); break;
	    
	    default: break;
	    
	    }
	    
	    
    }
}
