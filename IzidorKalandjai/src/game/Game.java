package game;

import java.awt.*;
import javax.swing.*;

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
    }
}
