package game;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import engine.Vector2d;


import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

// legfőbb osztály...
public class Game extends JFrame implements KeyListener
{
	// játékra vonatkozó konstansok
	private final int windowWidth  = 800;
	private final int windowHeight = 600;
	
	// a játék aktuális állapota
	private GameStates currentState = GameStates.MAINMENU;
	
	// az egyes játékállapotokhoz tartozó objektumok
	private final Intro intro		= new Intro(this);
	private final MainMenu mainMenu = new MainMenu(this);
	private final GamePlay gamePlay = new GamePlay(this);
	private final Credits credits   = new Credits(this);
	private final Outro outro		= new Outro(this);
	
	// a pálya, amin játszunk
	private Stage stage;
	
	// a felület, amelyre renderelünk
	private RenderSurface renderSurface;
	
	// a billentyűk állapotát tároló vektor
	private boolean[] keys = new boolean[525]; 
	
	// a pályák neveit tartalmazó tömb
	protected String stageFileNames[];
	
	// a kiválasztott pálya indexe a stageFileNames tömbben
	protected int selectedStageIndex;
	
	// a háttérzenék neveit tartalmazó tömb
	protected String musicFileNames[];
	
	// a kiválasztott zene indexe a musicFileNames tömbben
	protected int selectedMusicIndex;
	
	// a mainloop folyamatos, fix időközönként való futtatásához felhasznált időzítő
	private Timer timer;
	
	// játékosok
	private ArrayList<Player> players;
	
	// zenelejátszáshoz használt objektum
	private Clip backgroundMusic = null;
	
	
	// konstruktor
	public Game()
	{
		// keylistener létrehozása a billentyűzetkezeléshez
		this.addKeyListener(this); 
		
		// pályák neveinek betöltése
		loadStageFileNames();
		
		// háttérzene elindítása
		loadMusicFileNames();
		selectedMusicIndex = 0;
		playSelectedMusic();
		
		// ablakot nyitunk
		initUI("Izidor kalandjai", windowWidth, windowHeight);
		
		// a játék intróval indul
		switchGameMode(GameStates.INTRO);
		
		// játékosok létrehozása, képeik betöltése...
		players = new ArrayList<Player>();
		try
		{
			// IZIDOR hozzáadása
			File f = new File(System.getProperty("user.dir") + File.separatorChar + "res" + File.separatorChar + "izidor.png");
			BufferedImage bi;
			bi = ImageIO.read(f);
			TexturePaint player_izidor_paint = new TexturePaint(bi, new Rectangle2D.Float(0, 0, bi.getWidth(), bi.getHeight()));
			players.add(new Player((byte)0, new Vector2d(0, 0), player_izidor_paint, 14, 20, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT));
			
			// MORTIMER hozzáadása
			f = new File(System.getProperty("user.dir") + File.separatorChar + "res" + File.separatorChar + "mortimer.png");
			bi = ImageIO.read(f);
			TexturePaint player_mortimer_paint = new TexturePaint(bi, new Rectangle2D.Float(0, 0, bi.getWidth(), bi.getHeight()));
			players.add(new Player((byte)0, new Vector2d(0, 0), player_mortimer_paint, 14, 20, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_D));
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		// időzítő létrehozása és elindítása
		timer = new Timer();
		timer.scheduleAtFixedRate(new MainLoop(), 20, 20);
	}
	
	// pálya fájlok neveinek beolvasása
	public void loadStageFileNames()
	{
		// a res könyvtárban fogunk nézelődni
		File directory = new File(System.getProperty("user.dir") + File.separatorChar + "res");
		// szűrő, hogy csak az xml fájlokat kapjuk meg
		FilenameFilter filter = new FilenameFilter()
		{
			public boolean accept(File dir, String name)
			{
				return (name.endsWith(".xml"));
		    }
		};
		// szűrőnek megfelelő fájlok lekérdezése
		File[] files = directory.listFiles(filter);
		
		// fájlnevek eltárolása
		stageFileNames = new String[files.length];
		for( int i = 0; i < files.length; i++ )
		{
			stageFileNames[i] = files[i].getName();
		}
		
		// a legelső van kiválasztva
		selectedStageIndex = 0;
	}
	
	// háttérzene fájlok neveinek beolvasása
	public void loadMusicFileNames()
	{
		// a res könyvtárban fogunk nézelődni
		File directory = new File(System.getProperty("user.dir") + File.separatorChar + "res");
		// szűrő, hogy csak a wav fájlokat kapjuk meg
		FilenameFilter filter = new FilenameFilter()
		{
			public boolean accept(File dir, String name)
			{
				return (name.endsWith(".wav"));
		    }
		};
		// szűrőnek megfelelő fájlok lekérdezése
		File[] files = directory.listFiles(filter);
		
		// fájlnevek eltárolása
		if( files.length > 0 )
		{
			musicFileNames = new String[files.length];
			for( int i = 0; i < files.length; i++ )
			{
				musicFileNames[i] = files[i].getName();
			}
		}
		else
		{
			musicFileNames = null;
		}
		
		// a legelső van kiválasztva
		selectedMusicIndex = 0;
	}
	
	// a kijelölt zeneszám elindítása
	public void playSelectedMusic()
	{
		if( musicFileNames != null )
		{
			playMusic(System.getProperty("user.dir") + File.separatorChar + "res" + File.separatorChar + musicFileNames[selectedMusicIndex]);
		}
	}
	
	// paraméterül kapott zeneszám lejátszása végtelenítve
	public void playMusic(String path)
	{
		try
		{
			File f = new File(path);
			if( backgroundMusic == null )
			{
				backgroundMusic = AudioSystem.getClip();
			}
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(f);
			if( backgroundMusic.isOpen() )
			{
				backgroundMusic.close();
			}
			backgroundMusic.open(inputStream);
			backgroundMusic.setFramePosition(0);
			backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	// háttérzene leállítása
	public void stopMusic()
	{
		backgroundMusic.stop();
		backgroundMusic.close();
		backgroundMusic = null;
	}
	
	// megmondja, hogy szól-e háttérzene
	public boolean isMusicPlaying()
	{
		return (backgroundMusic != null);
	}
	
	// ablak létrehozása
	private void initUI(String title, int width, int height)
	{
		// render surface létrehozása és hozzáadása az ablakhoz
		renderSurface = new RenderSurface(this, width, height);
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
	
	// játék indítása a kijelölt pályán
	public void startGameOnSelectedStage()
	{
		startGame("res" + File.separatorChar + stageFileNames[selectedStageIndex]);
	}
	
	// játék indítása a paraméterül kapott nevű pályán
	public void startGame(String stagePath)
	{
		try
		{
			// pálya-fájl betöltése
			stage = XMLReader.load(stagePath);
			
			// játékosok inicializálása: elhelyezése a spawnpointon
			SpawnPoint sp = stage.getSpawnPoint();
			for( Player p : players )
			{
				p.moveTo(sp.getTileID(), sp.position);
				p.force.x = p.force.y = 0;
			}
			
			// váltás játék módba
			gamePlay.initState();
			currentState = GameStates.GAMEPLAY;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// játékmód váltás
	public void switchGameMode(GameStates nextState)
	{
		currentState = nextState;
		
		// az adott játékmód inicializálása
		switch( currentState )
		{
			case INTRO : intro.initState(); break;
			case MAINMENU : mainMenu.initState(); break;
			case GAMEPLAY : gamePlay.initState(); break;
			case CREDITS : credits.initState(); break;
			case OUTRO : outro.initState(); break;
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
				// intró állapot
				case INTRO :
				{
					intro.handleInput();
					intro.update();
					break;
				}
			
				// főmenüben vagyunk
				case MAINMENU :
				{
					mainMenu.handleInput();
					mainMenu.update();
					break;
				}
				
				// megy a játék
				case GAMEPLAY :
				{
					gamePlay.handleInput();
					gamePlay.update();
					break;
				}
				
				// készítők...
				case CREDITS :
				{
					credits.handleInput();
					credits.update();
					break;
				}
				
				// játék vége
				case OUTRO :
				{
					outro.handleInput();
					outro.update();
					break;
				}
			}
			
			// renderelni mindenképp kell
			renderSurface.repaint();
		}
	}
	
	// a játék kirajzolása az aktuális játékállapotnak megfelelően
	// ezt a függvényt a renderSurface rajzoló metódusa hívja meg
	public void render(Graphics2D g)
	{
		// a kapott Graphics2D objektum továbbadása az aktuális játékállapot rajzoló függvényének
		switch( currentState )
		{
			// intro
			case INTRO : intro.render(g); break;
			// főmenüben vagyunk
			case MAINMENU :	mainMenu.render(g); break;
			// játék módban vagyunk
			case GAMEPLAY : gamePlay.render(g); break;
			// készítők
			case CREDITS  : credits.render(g); break;
			// játék vége
			case OUTRO  : outro.render(g); break;
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
	
	// aktuális játékállapot lekérdezése
	public GameStates getCurrentState()
	{
		return currentState;
	}

	// main függvény - az alkalmazás belépési pontja
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, InvalidTileIDException
    {		
		// játék példányosítása és futtatása
		new Game();
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
	
	// billentyűk állapotait tároló tömb lekérdezése
	public boolean[] getKeys()
	{
		return keys;
	}
}
