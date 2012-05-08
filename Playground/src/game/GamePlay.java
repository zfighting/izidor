package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
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
		JUMPING,
		// pálya vége
		STAGE_FINISHED
	}
	// milyen módban van most a játék
	private GameMode currentMode;
	
	// statusbaron megjelenő szövegek
	private String statusStrings[] = new String[]{ "Tilitoli játékmód. Játékmód váltása: szóköz.", "Ugrabugrálás játékmód. Játékmód váltása: szóköz.", "Pálya teljesítve, szép munka! Következő pálya: enter, vissza a főmenübe: esc." };	
	
	// lehet-e éppen játékmódot váltani - felengedték-e a space-t a legutóbbi lenyomás óta
	private boolean canChangeMode;
	
	// tilitoli módban: lehet-e éppen elemeket felcserélni (fel lettek-e engedve a megfelelő gombok)
	private boolean canSwapTiles;
	
	// a statusbarra való íráshoz használt font
	private Font statusFont;
	
	// a játékosokra ható gravitációs erő
	private Vector2d gravityForce;
		
	
	// konstruktor
	GamePlay(Game game)
	{
		super(game);

		// gravitáció beállítása
		gravityForce = new Vector2d(0.f, 0.3f);
		
		// betütípus létrehozása
		statusFont = new Font("Calibri", Font.BOLD, 24);
		
		// játék inicializálása
		initState();
	}
	
	// ebbe a módba váltáskor lefutó inicializálás
	@Override
	public void initState()
	{
		// a játék ugrabugrálás módban indul és lehet váltani
		currentMode = GameMode.JUMPING;
		canChangeMode = true;
		
		// tilitoli módban lehet csereberélni
		canSwapTiles = true;
	}

	// billentyűzetkezelés
	@Override
	public void handleInput()
	{
		boolean[] keys = game.getKeys();
		
		// --- játékmódváltások kezelése, ha még nincs kipörgetve a pálya ---
		
		if( currentMode != GameMode.STAGE_FINISHED )
		{
			// ha tudunk játékmódot váltani
			if( canChangeMode )
			{
				// ha le van nyomva a szóköz billentyű
				if( keys[KeyEvent.VK_SPACE] )
				{
					// játékmód váltás
					currentMode = (currentMode == GameMode.SLIDING_PUZZLE) ? GameMode.JUMPING : GameMode.SLIDING_PUZZLE;
					// váltás letiltása
					canChangeMode = false;
					// a tilitolit is letiltjuk, hogy ha lenyomott nyíllal váltunk tilitoli módba, ne swapeljen rögtön
					canSwapTiles = false;
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
		}
		
		// inputkezelés játékmódtól függően
		switch( currentMode )
		{
			// --- tilitoli játékmód kezelése ---
			case SLIDING_PUZZLE:
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
				
				break;
			}
			// --- ugrabugrálás játékmód kezelése ---
			case JUMPING :
			{
				// minden játékosra
				for( Player p : game.getPlayers() )
				{					
					// a sebességének vízszintes komponense nullázódik: nem csúszkál, csak akkor mozog, ha kell
					p.force.x = 0;
					
					// kis gravitáció alkalmazása
					p.force = Vector2d.add(p.force, gravityForce);
					// a gravitáció nem növekedhet a végtelenségig
					if( p.force.y > 3.5f )
					{
						p.force.y = 3.5f;
					}
					
					// felfelé
					if( keys[p.controlUp] )
					{
						// ha ugorhat a játékos, ugrik
						if( p.canJump )
						{
							p.force.y += -Player.speed * 3;
							p.canJump = false;
						}
					}
					// balra
					if( keys[p.controlLeft] )
					{
						p.applyForce(new Vector2d(-Player.speed, 0));
					}
					// jobbra
					if( keys[p.controlRight] )
					{
						p.applyForce(new Vector2d(Player.speed, 0));
					}
				}
				
				break;
			}
			// --- pálya kipörgetve ---
			case STAGE_FINISHED :
			{
				// Esc-re visszamegyünk a főmenübe
				if( keys[KeyEvent.VK_ESCAPE] )
				{
					game.switchGameMode(GameStates.MAINMENU);
				}
				// enterre meg vagy a következő pályára, vagy ha ez volt az utolsó, akkor az outro állapotba
				else if( keys[KeyEvent.VK_ENTER] )
				{
					// ha nem ez volt az utolsó pálya
					if( game.selectedStageIndex < game.stageFileNames.length - 1 )
					{
						game.selectedStageIndex++;
						game.startGameOnSelectedStage();
					}
					// ha igen, akkor a Izidor megkapja Tektónia kezét :)
					else
					{
						game.switchGameMode(GameStates.OUTRO);
					}
				}
				
				break;
			}
		}
		
		// ESC-re mindenképp visszalépünk a főmenübe
		if( keys[KeyEvent.VK_ESCAPE] )
		{
			game.switchGameMode(GameStates.MAINMENU);
		}
	}

	// játékosok mozgatása
	@Override
	public void update()
	{
		// ha ugrabugrálás játékmódban vagyunk, akkor lehet ugrabugrálni
		if( currentMode == GameMode.JUMPING )
		{
			// egyúttal azt is megnézzük, hogy valamelyikük kipörgette-e a pályát
			boolean stage_finished = false;
			
			// minden játékosra
			for( Player p : game.getPlayers() )
			{
				// játékos mozgatása
	            game.getStage().movePlayer(p);
	            
	            // kipörgette a pályát?
	            stage_finished = stage_finished || game.getStage().isFinished(new Vector2d(p.position.x + p.getWidth() / 2, p.position.y + p.getHeight() / 2), p.getTileID());
			}
			
			// ha kipörgették, akkor játékmódot váltunk
			if( stage_finished )
			{
				currentMode = GameMode.STAGE_FINISHED;
			}
		}
	}

	@Override
	public void render(Graphics2D g)
	{		
		// pálya kirajzolása
		game.getStage().render(g);
		
		// játékosok kirenderelése
		for( Player p : game.getPlayers() )
		{
			// transzformációs mátrix elmentése
			AffineTransform af = g.getTransform();
			// koordinátarendszer eltolása - a tile bal felső sarkának elhelyezése a képernyőn
			game.getStage().translateSurfaceToTile(g, p.getTileID());
			// játékos kirajzolása
			p.render(g);
			// elmentett transzformációs mátrix visszaállítása
			g.setTransform(af);
		}
		
		// aktuális játékmódnak megfelelő szöveg kiírása a statusbarra
		g.setPaint(Color.black);
		g.setFont(statusFont);
		g.drawString(statusStrings[currentMode.ordinal()], 10, 585);
	}
}
