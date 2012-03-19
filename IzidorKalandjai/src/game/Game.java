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
    }
}
