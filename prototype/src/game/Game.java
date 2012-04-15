package game;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import engine.Vector2d;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

// legfőbb osztály...
public class Game extends JFrame implements KeyListener
{
	// játékra vonatkozó konstansok
	//private final int windowWidth  = 800;
	//private final int windowHeight = 600;
	
	// a játék aktuális állapota
	GameStates currentState = GameStates.MAINMENU;
	
	// az egyes játékállapotokhoz tartozó objektumok
	private final MainMenu mainMenu = new MainMenu(this);
	private final GamePlay gamePlay = new GamePlay(this);
	
	// a pálya, amin játszunk
	public Stage stage = new Stage();
	
	// a felület, amelyre renderelünk
	private RenderSurface renderSurface;
	
	// a billentyűk állapotát tároló vektor
	private boolean[] keys = new boolean[525]; 
	
	// a mainloop folyamatos, fix időközönként való futtatásához felhasznált időzítő
	private Timer timer;
	
	// játékosok
	private ArrayList<Player> players;
	
	
	// konstruktor
	public Game()
	{
		// keylistener létrehozása a billentyűzetkezeléshez
		this.addKeyListener(this); 
		
		// ablakot nyitunk
		//initUI("Izidor kalandjai", windowWidth, windowHeight);
		
		// a játék a főmenü játékállapotból indul
		currentState = GameStates.MAINMENU;
		
		// játékosok létrehozása
		players = new ArrayList<Player>();
		
		// időzítő létrehozása és elindítása
		//timer = new Timer();
		//timer.scheduleAtFixedRate(new MainLoop(), 20, 20);
	}
	
	public void addPlayer(byte tid, Vector2d p)
	{
		players.add(new Player(tid, p, Color.BLACK, 10, 20));
	}
	
	// ablak létrehozása
//	private void initUI(String title, int width, int height)
//	{
//		// render surface létrehozása és hozzáadása az ablakhoz
//		renderSurface = new RenderSurface(this, width, height);
//		getContentPane().add(renderSurface);
//
//		// címsor beállítása
//		setTitle(title);
//		// az ablakot ne lehessen átméretezni
//		setResizable(false);
//		// ablak képernyő közepére igazítása
//		pack();
//		setLocationRelativeTo(null);
//		// X-re záruljon be...
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
//		// láss csodát! a technika diadala
//		setVisible(true);
//	}
	
	// játék indítása a paraméterül kapott pályán
	public void startGame(String stagePath)
	{
		try
		{
			// pálya-fájl betöltése
			stage = XMLReader.load(stagePath);
			
			// váltás játék módba
			currentState = GameStates.GAMEPLAY;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// beágyazott osztály a mainloop futtatásához
	class MainLoop extends TimerTask
	{
		// az időzítő által fix időközönként meghívott függvény
		@Override
		public void run()
		{
			// a kurrens játékállapotnak megfelelő gamestate-nek adjuk a vezérlést
			switch( currentState )
			{
				// főmenüben vagyunk
				case MAINMENU :
				{
					mainMenu.handleInput();
					mainMenu.update();
					break;
				}
				
				case GAMEPLAY :
				{
					gamePlay.handleInput();
					gamePlay.update();
					break;
				}
			}
			
		}
	}
	
	// a játék kirajzolása az aktuális játékállapotnak megfelelően
	// ezt a függvényt a renderSurface rajzoló metódusa hívja meg
	public void render(Graphics2D g)
	{
		// a kapott Graphics2D objektum továbbadása az aktuális játékállapot rajzoló függvényének
		switch( currentState )
		{
			// főmenüben vagyunk
			case MAINMENU :	mainMenu.render(g); break;
			// játék módban vagyunk
			case GAMEPLAY : gamePlay.render(g); break;
		}
	}
	
	// stage lekérdezése - a gamestate-eknek kell
	public Stage getStage()
	{
		return stage;
	}
	
	// játékosok listájának lekérdezése - a gamestate-eknek kell
	public ArrayList<Player> getPlayers()
	{
		return players;
	}
	
	// a rendereléshez használt felület lekérdezése
	public RenderSurface getRenderSurface()
	{
		return renderSurface;
	}
	
	// aktuális játékállapot lekérdezése
	public GameStates getCurrentState()
	{
		return currentState;
	}

	// main függvény - az alkalmazás belépési pontja
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, InvalidTileIDException
    {		
		// játék példányosítása és futtatása
		Game proto = new Game();
		
		new Tests().TestsRun(args, proto);
		
		// ne töröld ki, thx
		// most még itt meg van hívva magában, de élesben a menülogika/endstage logika 
		// fog új stage-et létrehozni, ott majd "currentStage = XMLReader.load("stage3.xml");"
		// lesz vagy vmi ilyesmi, értelemszerűen. egy komplett felépített stage-el tér vissza.
		

		
		//XMLReader.load("res\\teststage.xml");
    }

	// billentyűleütés esemény lekezelése
	@Override
	public void keyPressed(KeyEvent e)
	{
		keys[e.getKeyCode()] = true;
	}
	
	// billentyűfelengedés esemény lekezelése
	@Override
	public void keyReleased(KeyEvent e)
	{
		keys[e.getKeyCode()] = false;
	}

	// billentyűleütés-felengedés
	@Override
	public void keyTyped(KeyEvent e)
	{
	}
	
	public boolean[] getKeys()
	{
		return keys;
	}
	
	public void globalUpdate()
	{
		this.gamePlay.update();
	}
}
