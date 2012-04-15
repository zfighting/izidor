package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import engine.Vector2d;


// a főmenü játékállapot osztálya
public class GamePlay extends GameState
{
	// a játékon belüli lehetséges játékmódok
	private enum GameMode
	{
		// tilitoli
		SLIDING_PUZZLE,
		// ugrabugrálás
		JUMPING
	}
	// milyen módban van most a játék
	private GameMode currentMode;
	
	// lehet-e éppen játékmódot váltani - felengedték-e a space-t a legutóbbi lenyomás óta
	private boolean canChangeMode;
	
	// tilitoli módban: lehet-e éppen elemeket felcserélni (fel lettek-e engedve a megfelelő gombok)
	private boolean canSwapTiles;
	
	// játéktér hátterének képe
	private RectangularRenderableGameObject backgroundSprite;
	
	// alsó statusbar képe
	private RectangularRenderableGameObject statusBarSprite;
	
	// a statusbarra való íráshoz használt font
	private Font statusFont;
		
	// konstruktor
	GamePlay(Game game)
	{
		super(game);
		
		// a játék ugrabugrálás módban indul és lehet váltani
		currentMode = GameMode.JUMPING;
		canChangeMode = true;
		
		// tilitoli módban lehet csereberélni
		canSwapTiles = true;
		
		// betütípus létrehozása
		statusFont = new Font("Calibri", Font.BOLD, 24);
		
		// képek létrehozása, betöltése
		try
		{
			// háttér létrehozása
			File f = new File(System.getProperty("user.dir") + File.separatorChar + "res" + File.separatorChar + "backgroundSprite.png");
			BufferedImage bi;
			bi = ImageIO.read(f);
			TexturePaint background_paint = new TexturePaint(bi, new Rectangle2D.Float(0, 0, bi.getWidth(), bi.getHeight()));
			backgroundSprite = new RectangularRenderableGameObject((byte)1, new Vector2d(0, 0), background_paint, 800, 600);
			
			// statusbar képének létrehozása
			GradientPaint statusbar_paint = new GradientPaint(0, 0, new Color(0, 0, 0, 0), 0, 50, new Color(70, 70, 70), false);
			statusBarSprite = new RectangularRenderableGameObject((byte)1, new Vector2d(0, 550), statusbar_paint, 800, 50);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	// billentyűzetkezelés
	@Override
	public void handleInput()
	{
		boolean[] keys = game.getKeys();
		
		// --- játékmódváltások kezelése ---
		
		// ha tudunk játékmódot váltani
		if( canChangeMode )
		{
			// ha le van nyomva a szóköz billentyű
			if( keys[KeyEvent.VK_SPACE] )
			{
				// játékmód váltás
				currentMode = currentMode == GameMode.SLIDING_PUZZLE ? GameMode.JUMPING : GameMode.SLIDING_PUZZLE;
				// váltás letiltása
				canChangeMode = false;
			}
		}
		// ha éppen nem tudunk játékmódot váltani...
		else
		{
			// de felengedték a szóközt
			if( keys[KeyEvent.VK_SPACE] == false )
			{
				// akkor engedélyezzük a játékmód váltást
				canChangeMode = true;
			}
		}
		
		// --- tilitoli játékmód kezelése ---
		if( currentMode == GameMode.SLIDING_PUZZLE )
		{
			// ha lehet éppen cserélgetni
			if( canSwapTiles )
			{
				// W és felfele nyíl
				if( keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP] )
				{
					// tile-ok felcserélése és cserélgetés letiltása
					game.getStage().swap(Direction.UP);
					canSwapTiles = false;
				}
				// A és balra nyíl
				if( keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT] )
				{
					// tile-ok felcserélése és cserélgetés letiltása
					game.getStage().swap(Direction.LEFT);
					canSwapTiles = false;
				}
				// S és lefele nyíl
				if( keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN] )
				{
					// tile-ok felcserélése és cserélgetés letiltása
					game.getStage().swap(Direction.DOWN);
					canSwapTiles = false;
				}
				// D és jobbra nyíl
				if( keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT] )
				{
					// tile-ok felcserélése és cserélgetés letiltása
					game.getStage().swap(Direction.RIGHT);
					canSwapTiles = false;
				}
			}
			// ha éppen nem lehet cserélgetni...
			else
			{
				// de felengedték a mozgató gombokat
				if( !keys[KeyEvent.VK_W] && !keys[KeyEvent.VK_UP] &&
					!keys[KeyEvent.VK_A] && !keys[KeyEvent.VK_LEFT] &&
					!keys[KeyEvent.VK_S] && !keys[KeyEvent.VK_DOWN] &&
					!keys[KeyEvent.VK_D] && !keys[KeyEvent.VK_RIGHT] )
				{
					canSwapTiles = true;
				}				
			}
		}
		// --- ugrabugrálás játékmód kezelése ---
		else
		{
			;
		}
	}

	@Override
	public void update()
	{
		// gravitáció
		for (Player p : game.getPlayers())
		{
			p.applyForce(new Vector2d (0, 5));
			game.stage.movePlayer(p);
		}
		
	}

	@Override
	public void render(Graphics2D g)
	{
		// háttér kirajzolása
		backgroundSprite.render(g);
		
		// pálya kirajzolása
		game.getStage().render(g);
		
		// statusbar kirajzolása
		statusBarSprite.render(g);
		
		// aktuális játékmód kiírása a statusbarra
		g.setPaint(Color.black);
		g.setFont(statusFont);
		g.drawString(currentMode == GameMode.SLIDING_PUZZLE ? "Tilitoli játékmód" : "Ugrabugrálás játékmód", 10, 585);
	}

}
