package game;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

// legfőbb osztály...
public class Game extends JFrame
{
	private RenderSurface renderSurface;
	
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
		XMLReader.loadFromXML("\\res\\XMLTeszt.xml");
	}

	// konstruktor
	public Game()
	{
		// ablakot nyitunk
		initUI("Izidor kalandjai", 640, 480);
	}
	
	// main
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
