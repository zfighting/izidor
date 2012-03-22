package game;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

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
		// játék példányosítása és futtatása
		new Game();
    }
}
